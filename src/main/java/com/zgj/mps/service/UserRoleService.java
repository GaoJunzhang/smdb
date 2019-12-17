package com.zgj.mps.service;

import com.zgj.mps.dao.UserRoleRepository;
import com.zgj.mps.model.Role;
import com.zgj.mps.model.User;
import com.zgj.mps.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    RoleAuthService roleAuthService;

    public Object getUserRoleAuths(long userId) {
        HashMap<String, Object> roleAuth = new HashMap<>();
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        if (userRoles != null && userRoles.size() > 0) {
            UserRole userRole = userRoles.get(0);
            roleAuth.put("name", userRole.getRole().getName());
            roleAuth.put("permissions", roleAuthService.getRoleAuths(userRole.getRole().getId()));
        }

        return roleAuth;
    }

    public Role userRole(long userId) {
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        if (userRoles != null && userRoles.size() > 0) {
            return userRoles.get(0).getRole();
        }
        return null;
    }

    public void savaUserRole(long userId, long roleId) {
        UserRole userRole = new UserRole();
        User user = new User();
        Role role = new Role();
        user.setId(userId);
        role.setId(roleId);
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userRoleRepository.save(userRole);
    }

    public void deleteByUserId(long userId) {
        userRoleRepository.deleteByUserId(userId);
    }
}
