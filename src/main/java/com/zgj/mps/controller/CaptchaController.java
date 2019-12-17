package com.zgj.mps.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.zgj.mps.integration.redis.RedisObjectManager;
import com.zgj.mps.tool.FcySmsUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/kaptcha/*")
@Slf4j
public class CaptchaController {

	@Autowired
	private Producer captchaProducer;

	@Autowired
	private RedisObjectManager redisObjectManager;

	@RequestMapping
	public ModelAndView getKaptchaImage(HttpServletResponse response) throws Exception {
		String capText = captchaProducer.createText();

		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		if(session != null) {
			session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		}
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return null;
	}

	@RequestMapping(value = "/sendSms",method = RequestMethod.GET)
	@ApiOperation(value = "发送短信")
	@ResponseBody
	public Result<Object> sendSms(@RequestParam(name = "mobile",required = true) String mobile){
		int code = (int) ((Math.random() * 999999) + 100);
		redisObjectManager.setObject(mobile,code+"",10*60);
		try {
			String resStr = FcySmsUtil.sendSms("【V-STU教育】验证码:"+code+"",mobile);
			System.out.println(resStr);
			JSONObject json = JSONObject.fromObject(resStr);
			if (!"1".equals(json.get("status")+"")){
				return new ResultUtil<Object>().setErrorMsg("发送失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ResultUtil<Object>().setErrorMsg("发送异常");
		}
		return new ResultUtil<Object>().setSuccessMsg("发送成功");
	}

}