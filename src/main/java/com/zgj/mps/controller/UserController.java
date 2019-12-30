package com.zgj.mps.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zgj.mps.controller.request.ChangePasswordRequest;
import com.zgj.mps.generator.base.BaseController;
import com.zgj.mps.model.User;
import com.zgj.mps.model.UserEnum;
import com.zgj.mps.service.UserRoleService;
import com.zgj.mps.service.UserService;
import com.zgj.mps.tool.CommonUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
@Transactional
public class UserController extends BaseController<User, Long> {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserService getService() {
        return userService;
    }

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequiresPermissions("userList")
    @GetMapping(path = "/userListData")
    public Result<Map<String, Object>> userList(String name, String account, Short status,
                                                @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "descend") String sortOrder, @RequestParam(defaultValue = "createTime") String sortField) {
        User user = shiroSecurityUtil.getCurrentUser();
        return new ResultUtil().setData(userService.userList(user.getId(), user.getCode(), name, account, status, pageNo, pageSize, sortOrder, sortField));
    }

    @RequiresPermissions("user:add")
    @GetMapping(path = "/user")
    public Map<String, Object> user() {
        log.info("user");

        return CommonUtil.defaultResponse(1, "user");
    }

    @PostMapping(path = "/saveUser")
    public Object saveUser(HttpServletRequest request,
                           Long id, String avatar,
                           @RequestParam(name = "name", required = true) String name,
                           @RequestParam(name = "account", required = true)String account,
                           @RequestParam(name = "status", required = true)String status,
                           @RequestParam(name = "mobile", required = true)String mobile,
                           @RequestParam(name = "roleId", required = true)Long roleId) {
        User user = shiroSecurityUtil.getCurrentUser();
        log.info("user");
        try {
            User u = userService.saveUser(id,name,account,UserEnum.DEFAULT,status,mobile,avatar,user.getId(),CommonUtil.getClientIP(request));
            userRoleService.deleteByUserId(u.getId());
            userRoleService.savaUserRole(u.getId(),roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultUtil<>().setErrorMsg("保存失败");
        }
        return new ResultUtil<>().setSuccessMsg("保存成功");
    }

    @RequiresPermissions("user:del")
    @RequestMapping(path = "/delUser/{ids}", method = RequestMethod.DELETE)
    public Object delUser(@PathVariable String[] ids) {
        for (String id: ids){
            userService.delete(Long.parseLong(id));
        }
        return new ResultUtil<>().setSuccessMsg("修改成功");
    }

    @RequestMapping(value = "/resetPassword/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public Object resetPassword(@PathVariable String[] ids){
        for (String id: ids){
            userService.updatePassword(Long.parseLong(id));
        }
        return new ResultUtil<>().setSuccessMsg("修改成功");
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户密码")
    public Object changePassword(HttpSession session, @RequestBody ChangePasswordRequest request) {
        long userId = (long) session.getAttribute("userId");
        return userService.changePassword(userId, request);
    }

    @PostMapping(value = "/changeTheme")
    @ApiOperation(value = "修改系统主题风格")
    public Object changeTheme(HttpSession session, String navTheme, String theme) {
        long userId = (long) session.getAttribute("userId");
        User user = userService.get(userId);
        if (StrUtil.isNotEmpty(navTheme)) {
            user.setNavTheme(navTheme);
        }
        if (StrUtil.isNotEmpty(theme)) {
            user.setThemeColor(theme);
        }
        userService.update(user);
        return new ResultUtil<>().setSuccessMsg("修改成功");
    }

    @GetMapping(value = "/sumUser")
    @ApiOperation(tags = "获取用户总数", value = "sumUsers")
    public Result<Integer> sumUser(){
        return new ResultUtil<Integer>().setData(userService.countByIsDelete());
    }

    @GetMapping(value = "/mounthUsers")
    @ApiOperation(tags = "月用户数", value = "月用户数")
    public Object mounthUsers(){
        System.out.println(DateUtil.year(new Date()));
        return new ResultUtil<>().setData(userService.mounthUser(DateUtil.year(new Date())));
    }
}
