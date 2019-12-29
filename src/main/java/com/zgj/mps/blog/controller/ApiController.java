package com.zgj.mps.blog.controller;

import com.zgj.mps.blog.model.Article;
import com.zgj.mps.blog.service.EsArticleService;
import com.zgj.mps.tool.PageUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import com.zgj.mps.vo.SearchVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webapi")
@CrossOrigin(origins = "*",maxAge = 3600)
public class ApiController {

    @Autowired
    private EsArticleService esArticleService;

    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取全部")
    public Result<Object> getAllByPage(@RequestParam(required = false) Integer type,
                                       @RequestParam(required = false) String key,
                                       @ModelAttribute SearchVo searchVo,
                                       @ModelAttribute PageVo pageVo) {

        Page<Article> es = esArticleService.findByConfition(type, key, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Object>().setData(es);
    }
}
