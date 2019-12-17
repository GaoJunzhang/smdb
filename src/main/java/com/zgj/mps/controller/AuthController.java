package com.zgj.mps.controller;

import com.zgj.mps.controller.request.LoginRequest;
import com.zgj.mps.model.Auth;
import com.zgj.mps.model.User;
import com.zgj.mps.service.AuthService;
import com.zgj.mps.service.RoleAuthService;
import com.zgj.mps.tool.CommonUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.router.VueRouter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@RestController
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RoleAuthService roleAuthService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @PostMapping(path = "/auth/login")
    public Map<String, Object> login(HttpServletRequest request, @ModelAttribute LoginRequest loginRequest) {
        return authService.login(loginRequest.getAccount(), loginRequest.getPassword(), loginRequest.getCode(), CommonUtil.getClientIP(request));
    }

    @PostMapping(path = "/auth/logout")
    public Map<String, Object> login() {
        return authService.logout();
    }

    @GetMapping(path = "/noAuth")
    public Map<String, Object> noAuth() {
        return CommonUtil.defaultResponse(9999, "Login timeout");
    }

    @GetMapping(value = "/user/info")
    public Map<String, Object> getUserInfo() {
        User user = shiroSecurityUtil.getCurrentUser();
        return authService.getUserInfo(user.getId());
    }

    @RequestMapping(value = "/user/getUserRouters", method = RequestMethod.GET)
    @ApiOperation(value = "获取可分配权限")
    public ArrayList<VueRouter<Auth>> getUserRouters() {
        User user = shiroSecurityUtil.getCurrentUser();
        return roleAuthService.getUserRouters(user.getId());
    }
    @PostMapping(path = "/auth/register")
    public Map<String, Object> register(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        return authService.register(loginRequest.getAccount(), loginRequest.getPassword(), loginRequest.getCode(), CommonUtil.getClientIP(request),loginRequest.getMobile());
    }

}
