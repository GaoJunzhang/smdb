package com.zgj.mps.integration.shiro;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zgj.mps.dao.RoleAuthRepository;
import com.zgj.mps.dao.UserRoleRepository;
import com.zgj.mps.integration.jwt.JwtToken;
import com.zgj.mps.integration.jwt.JwtUtil;
import com.zgj.mps.model.RoleAuth;
import com.zgj.mps.model.User;
import com.zgj.mps.model.UserRole;
import com.zgj.mps.model.UserStatusEnum;
import com.zgj.mps.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ShiroAuthRealm extends AuthorizingRealm {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleAuthRepository roleAuthRepository;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

//    @Override
//    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");
//        hashedCredentialsMatcher.setHashIterations(2);
//        super.setCredentialsMatcher(hashedCredentialsMatcher);
//    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String account = "" + principals.getPrimaryPrincipal();
        User user = authService.findByAccount(account);
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());

        for (UserRole userRole : userRoles) {
            List<RoleAuth> roleAuths = roleAuthRepository.findAllByRoleId(userRole.getRole().getId());
            authorizationInfo.addRole(userRole.getRole().getRole());
            for (RoleAuth roleAuth : roleAuths) {
                authorizationInfo.addStringPermission(roleAuth.getAuth().getAction());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
            throws AuthenticationException {
        String token = (String) auth.getCredentials();
        String password = null;
        String username = null;

        try {
            username = JwtUtil.getUsername(token);
            password = JwtUtil.getPassword(token);
        } catch (Exception e) {
            throw new AuthenticationException("heard的token拼写错误或者值为空");
        }
        if (username == null) {
            log.error("token无效(空''或者null都不行!)");
            throw new AuthenticationException("token无效");
        }
        User user = authService.findByAccount(username);
        if (user == null) {
            throw new AccountException("帐号或密码不正确!");
        }
        if (user.getStatus() != UserStatusEnum.ENABLE) {
            throw new DisabledAccountException("帐号被禁用!");
        }
        if (!JwtUtil.verify(token, username, password)) {
            log.error("用户名或密码错误(token无效或者与登录者不匹配)!)");
            throw new TokenExpiredException("用户名或密码错误(token无效或者与登录者不匹配)!");
        }
//        return new SimpleAuthenticationInfo(
//                user.getAccount(),
//                user.getPassword(),
//                ByteSource.Util.bytes(user.getSalt()),
//                getName());
        return new SimpleAuthenticationInfo(username, token, "my_realm");
//        String md5pwd = new Md5Hash(password,username).toHex();
//        return new SimpleAuthenticationInfo(
//                username,
//                md5pwd,
//                ByteSource.Util.bytes(username),
//                getName());
    }
}
