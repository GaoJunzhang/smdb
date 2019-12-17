package com.zgj.mps.tool;

import com.zgj.mps.model.User;
import com.zgj.mps.service.AuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShiroSecurityUtil {
    @Autowired
    private AuthService authService;

    public User getCurrentUser(){
        Subject subject = SecurityUtils.getSubject();
        return authService.findByAccount((String) subject.getPrincipal());
    }
}
