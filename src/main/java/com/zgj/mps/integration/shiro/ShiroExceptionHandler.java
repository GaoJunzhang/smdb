package com.zgj.mps.integration.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ShiroExceptionHandler implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        ModelAndView mv = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        log.info("{}:{}", httpServletRequest.getRequestURI(), ex.getMessage());
        if (ex instanceof UnauthenticatedException) {
            mv.addObject("errCode", 9997);
            mv.addObject("errMsg", "Token error");
        } else if (ex instanceof UnauthorizedException) {
            mv.addObject("errCode", 9998);
            mv.addObject("errMsg", "Permission denied");
        } else {
            mv.addObject("errCode", 9996);
            mv.addObject("errMsg", ex.getMessage());
        }
ex.printStackTrace();
        mv.setView(view);
        return mv;
    }
}
