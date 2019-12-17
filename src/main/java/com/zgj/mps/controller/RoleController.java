package com.zgj.mps.controller;

import cn.hutool.core.util.StrUtil;
import com.zgj.mps.generator.base.BaseController;
import com.zgj.mps.model.Auth;
import com.zgj.mps.model.Role;
import com.zgj.mps.model.RoleAuth;
import com.zgj.mps.model.User;
import com.zgj.mps.service.RoleAuthService;
import com.zgj.mps.service.RoleService;
import com.zgj.mps.tool.PageUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.AuthVo;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import com.zgj.mps.vo.SearchVo;
import com.zgj.mps.vo.router.VueRouter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "角色管理接口")
@RequestMapping("/role")
@Transactional
public class RoleController extends BaseController<Role, Long> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleAuthService roleAuthService;

    @Override
    public RoleService getService() {
        return roleService;
    }

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequiresPermissions("roleList")
    @RequestMapping(value = "/getRoleData", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Object getByCondition(@ModelAttribute Role role,
                                 @ModelAttribute SearchVo searchVo,
                                 @ModelAttribute PageVo pageVo) {
        if (StrUtil.isEmpty(pageVo.getSortField())) {
            pageVo.setSortField("createTime");
            pageVo.setSortOrder("desc");
        }
        return new ResultUtil<>().setData(roleService.roleData(role, searchVo, PageUtil.initPage(pageVo)));
    }

    @RequiresPermissions("role:add")
    @RequestMapping(value = "/saveRole", method = RequestMethod.POST)
    @ApiOperation(value = "保存角色")
    public Result<String> saveRole(Long id, String role, String name, String description, Short isDelete, String[] authIds) {
        List<Role> roleList = roleService.findAllByRole(role);
        if (roleList.size() > 0 && roleList.get(0).getId() != id) {
            return new ResultUtil<String>().setErrorMsg("唯一识别码已被使用");
        }
        User userSess = shiroSecurityUtil.getCurrentUser();
        User user = new User();
        user.setId(userSess.getId());
        Role role1 = new Role();
        if (id != null) {
            role1.setId(id);
            role1.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            role1.setIsDelete(isDelete);
        } else {
            role1.setIsDelete((short) 0);
            role1.setCreateTime(new Timestamp((System.currentTimeMillis())));
        }
        role1.setRole(role);
        role1.setName(name);
        role1.setDescription(description);
        role1.setUser(user);
        Role role2 = roleService.save(role1);
        roleAuthService.deleteAllByRole(role2);//删除所有权限
        //添加新权限
        if (authIds.length > 0) {
            List<RoleAuth> roleAuths = new ArrayList<>(authIds.length);
            for (String authId : authIds) {
                RoleAuth roleAuth = new RoleAuth();
                Auth auth = new Auth();
                auth.setId(Long.parseLong(authId));
                roleAuth.setAuth(auth);
                roleAuth.setRole(role2);
                roleAuth.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                roleAuth.setCreateTime(new Timestamp(System.currentTimeMillis()));
                roleAuths.add(roleAuth);
            }
            roleAuthService.saveRoleAuth(roleAuths);
        }
        return new ResultUtil<String>().setSuccessMsg("保存成功");
    }

    @RequiresPermissions("role:del")
    @RequestMapping(value = "/delRole/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除角色")
    public Result<String> delRole(@PathVariable String[] ids) {
        for (String id : ids) {
            roleService.updateIsDelete(Long.parseLong(id));
        }
        return new ResultUtil<String>().setSuccessMsg("删除成功");
    }

    @RequestMapping(value = "/getRoleAuth", method = RequestMethod.GET)
    @ApiOperation(value = "获取角色权限")
    public Map<String, Object> getRoleAuth(@RequestParam(name = "roleId", required = true) Long roleId) {
        List<AuthVo> list = roleAuthService.findAllByRoleId(roleId);
        Map<String, Object> map = new HashMap<>();
        map.put("rolePermission", list);
        return map;
    }

    @RequestMapping(value = "/getPermission", method = RequestMethod.GET)
    @ApiOperation(value = "获取可分配权限")
    public Map<String, Object> getPermission() {
        User user = shiroSecurityUtil.getCurrentUser();
        List<AuthVo> myAuth = new ArrayList<>();
        if (user.getCode().equals("0000")) {

            myAuth = roleAuthService.allAuthsData();
        } else {
            myAuth = roleAuthService.RoleAuthsData(user.getId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("permission", myAuth);
        return map;
    }

    @RequestMapping(value = "/getUserRouters", method = RequestMethod.GET)
    @ApiOperation(value = "获取可分配权限")
    public ArrayList<VueRouter<Auth>> getUserRouters() {
        User user = shiroSecurityUtil.getCurrentUser();
        return roleAuthService.getUserRouters(user.getId());
    }

    @RequestMapping(value = "/allRoles", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部角色")
    public Object allRoles() {
        List<Role> roles = roleService.findAllByIsDelete((short) 0);
        List<Map<String,Object>> list = new ArrayList<>(roles.size());
        for (Role role : roles){
            Map<String,Object> map = new HashMap<>();
            map.put("id",role.getId());
            map.put("name", role.getName());
            list.add(map);
        }
        return new ResultUtil<List<Map<String,Object>>>().setData(list);
    }
}
