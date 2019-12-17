package com.zgj.mps.service;

import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Constants;
import com.zgj.mps.dao.UserRepository;
import com.zgj.mps.integration.jwt.JwtToken;
import com.zgj.mps.integration.jwt.JwtUtil;
import com.zgj.mps.integration.redis.RedisObjectManager;
import com.zgj.mps.model.*;
import com.zgj.mps.tool.CommonUtil;
import com.zgj.mps.tool.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleAuthService roleAuthService;

    @Autowired
    private RedisObjectManager redisObjectManager;

    public User findByAccount(String account) {
        if (StrUtil.isEmpty(account))
            return null;

        List<User> userList = userRepository.findAllByAccountAndIsDelete(account, (short) 0);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    public Map<String, Object> login(String account, String password, String code, String ip) {
        if (StrUtil.isNotEmpty(code)){
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            String kaptcha = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (!StrUtil.isEmpty(kaptcha) && kaptcha.equals(code) && !StrUtil.isEmpty(code)) {
                User user = findByAccount(account);
                session.setAttribute("account", user.getAccount());
                session.setAttribute("name", user.getName());
                session.setAttribute("userId", user.getId());
                session.setAttribute("userCode", user.getCode());
                Map<String, Object> res = CommonUtil.defaultResponse(0, "");
                res.put("token", currentUser.getSession().getId());
                userRepository.updateUserLogin(user.getId(), ip);
            } else {
                return CommonUtil.defaultResponse(1, "验证码错误");
            }
        }
        if (StrUtil.isEmpty(account) || StrUtil.isEmpty(password)) {
            return CommonUtil.defaultResponse(1, "用户名为空");
        }
        User user = findByAccount(account);
        if (user == null){
            return CommonUtil.defaultResponse(1, "账号不存在");
        }
        if (!user.getPassword().equals(MD5Util.getPassword(account,password))){
            return CommonUtil.defaultResponse(1, "密码错误");
        }
        String token = JwtUtil.sign(account, password);
        Map<String, Object> res = CommonUtil.defaultResponse(0, "");
        res.put("token", token);
        JwtToken jwtToken = new JwtToken(token);
        userRepository.updateUserLogin(user.getId(), ip);
        return res;
    }

    public Map<String, Object> register(String account, String password, String code, String ip, String mobile) {
        if (StrUtil.isEmpty(account) || StrUtil.isEmpty(password)) {
            return CommonUtil.defaultResponse(1, "用户名为空");
        }
        Subject currentUser = SecurityUtils.getSubject();
        String errMsg = "unknown";
        if (userRepository.findAllByAccountAndIsDelete(account, (short) 0).size() > 0) {
            return CommonUtil.defaultResponse(1, "用户名已经存在");
        }
        if (userRepository.findByMobile(mobile).size()>0){
            return CommonUtil.defaultResponse(1, "该手机号已注册");
        }
//        Session session = currentUser.getSession();
//        String kaptcha = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
//        if (StrUtil.isEmpty(kaptcha) && kaptcha.equals(code) && !StrUtil.isEmpty(code)) {
//            return CommonUtil.defaultResponse(1, "验证码错误");
//        }
        String rSmsCode = (String) redisObjectManager.getObject(mobile);
        if (rSmsCode == null) {
            return CommonUtil.defaultResponse(1, "短信验证码已失效");
        }
        if (!rSmsCode.equals(code)) {
            return CommonUtil.defaultResponse(1, "短信验证码错误");
        }
        User user = new User();
        user.setAccount(account);
        user.setPassword(MD5Util.getPassword(account, password));
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setLoginIp(ip);
        user.setIsDelete((short) 0);
        user.setMobile(mobile);
        user.setName(account);
        user.setSalt(password);
        user.setCode(rSmsCode);
        user.setStatus(UserStatusEnum.ENABLE);
        user.setType(UserEnum.DEFAULT);
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
        System.out.println(user.getId());
        System.out.println(user.getId());
        System.out.println(user.getId());
        System.out.println(user.getId());
        userRoleService.savaUserRole(user.getId(),(long)6);
        return CommonUtil.defaultResponse(0, "注册成功");
    }

    public Map<String, Object> logout() {
        SecurityUtils.getSubject().logout();
        return CommonUtil.defaultResponse(0, "");
    }

    @Transactional
    public Map<String, Object> getUserInfo(Long userId) {
        User user = userRepository.getOne(userId);
        if (user == null) {
            return CommonUtil.defaultResponse(0, "无效的用户");
        }

        Map<String, Object> map = CommonUtil.defaultResponse(0, "");
        map.put("name", user.getName());
        map.put("avatar", user.getAvatar());
        try {
            map.put("role", userRoleService.getUserRoleAuths(userId));
            map.put("menus", roleAuthService.getUserRouters(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("getUserInfo:{}", map);
        return map;
    }
}
