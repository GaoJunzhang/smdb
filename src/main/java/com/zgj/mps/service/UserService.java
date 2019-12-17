package com.zgj.mps.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zgj.mps.controller.request.ChangePasswordRequest;
import com.zgj.mps.dao.UserMgroupRepository;
import com.zgj.mps.dao.UserRepository;
import com.zgj.mps.generator.base.BaseService;
import com.zgj.mps.model.*;
import com.zgj.mps.tool.MD5Util;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService implements BaseService<User, Long> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserMgroupRepository userMgroupRepository;

    @Override
    public UserRepository getRepository() {
        return userRepository;
    }

    public Map<String, Object> userList(long userId, String userCode, String name, String account, Short status, Integer page, Integer size, String sortOrder, String sortValue) {
        Sort sort = new Sort(sortOrder.equals("descend") ? Sort.Direction.DESC : Sort.Direction.ASC, sortValue);
        Pageable pageable = new PageRequest(page > 0 ? page - 1 : page, size, sort);

        Specification specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(cb.notEqual(r.get("id"), userId));
                predicate.getExpressions().add(cb.like(r.get("code"), userCode + "%"));
                predicate.getExpressions().add(cb.equal(r.get("isDelete"), 0));
                if (!StrUtil.isEmpty(name)) {
                    predicate.getExpressions().add(cb.like(r.get("name"), "%" + name + "%"));
                }
                if (!StrUtil.isEmpty(account)) {
                    predicate.getExpressions().add(cb.like(r.get("account"), "%" + account + "%"));
                }

                if (status != null) {
                    predicate.getExpressions().add(cb.equal(r.get("status"), status));
                }
                return predicate;
            }
        };

        Page<User> users = userRepository.findAll(specification, pageable);
        List<Map<String, Object>> usersMap = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> u = new HashMap<>();
            u.put("id", user.getId());
            u.put("name", user.getName());
            u.put("account", user.getAccount());
            u.put("status", user.getStatus().ordinal());
            Role role = userRoleService.userRole(user.getId());
            u.put("roleName", role == null ? "" : role.getName());
            u.put("loginTime", user.getLoginTime() == null ? "" : DateUtil.format(user.getLoginTime(), "yyyy-MM-dd HH:mm:ss"));
            u.put("createTime", DateUtil.format(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            u.put("avatar", user.getAvatar());
            u.put("mobile", user.getMobile());
            u.put("roleId", role == null ? "" : role.getId());
            u.put("password", user.getPassword());
            usersMap.add(u);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", page);
        map.put("totalCount", users.getTotalElements());
        map.put("data", usersMap);
        return map;
    }

    public List<String> userMgroupCodes(Long userId) {
        List<UserMgroup> userMgroups = userMgroupRepository.userMgroupsByUserAndName(userId);
        List<String> userMgroupCode = new ArrayList<>();
        for (UserMgroup userMgroup : userMgroups) {
            userMgroupCode.add(userMgroup.getMgroup().getCode());
        }

        return userMgroupCode;
    }

    public User findByAccountAndIsDelete(String acount, Short isDelete) {
        return userRepository.findByAccountAndIsDelete(acount, isDelete);
    }

    public User saveUser(Long id, String name, String account, UserEnum type, String status, String mobile, String avatar, long userId, String ip) {
        User user = null;
        if (id == null) {
            user = new User();
            user.setIsDelete((short) 0);
            user.setCreateTime(new Timestamp(System.currentTimeMillis()));
            user.setPassword(MD5Util.getPassword(account, "v123456"));
            user.setSalt(MD5Util.getMD5String(account));
        } else {
            user = userRepository.getOne(id);
        }
        User user1 = new User();
        user1.setId(userId);
        user.setUser(user1);
        user.setName(name);
        user.setAccount(account);
        user.setType(type);
        if ("1".equals(status)){
            user.setStatus(UserStatusEnum.ENABLE);
        }else {
            user.setStatus(UserStatusEnum.DISABLE);
        }
        user.setMobile(mobile);
        user.setAvatar(avatar);
        user.setCode("0000");
        user.setLoginIp(ip);
        return userRepository.save(user);
    }

    public int updatePassword(Long id){
        User user = userRepository.getOne(id);
        return userRepository.updatePassword(id,MD5Util.getPassword(user.getAccount(),"v123456"));
    }

    public int updateAvatar(Long id, String avatar){
        return userRepository.updateAvatar(id,avatar);
    }

    @Transactional
    public Result changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResultUtil().setErrorMsg("无效的用户");
        }
        if (StrUtil.isEmpty(request.getOldPassword()) || StrUtil.isEmpty(request.getNewPassword())) {
            return new ResultUtil().setErrorMsg("无效的密码");
        }
        log.info("{},{}", request.getOldPassword(), request.getNewPassword());
        String oldP = MD5Util.getPassword(user.getAccount(), request.getOldPassword());
        if (oldP.equals(user.getPassword())) {
            user.setPassword(MD5Util.getPassword(user.getAccount(),request.getNewPassword()));
            userRepository.save(user);
            return new ResultUtil<>().setData("修改成功");
        }
        return new ResultUtil().setErrorMsg("原密码错误");
    }

    public int countByIsDelete(){
        return userRepository.countByIsDelete((short)0);
    }

    public JSONArray mounthUser(int year){
        Object object = userRepository.mounthUser(year);
        return JSONUtil.parseArray(object);
    }
}
