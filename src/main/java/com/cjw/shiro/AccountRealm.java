package com.cjw.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.cjw.entity.User;
import com.cjw.service.UserService;
import com.cjw.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken token = (JwtToken) authenticationToken;
        String userId = jwtUtils.getClaimByToken((String) token.getPrincipal()).getSubject();
        User user = userService.getById(userId);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        if (user.getStatus() == -1) {
            throw new LockedAccountException("用户已被锁定");
        }
        UiUser uiUser = new UiUser();
        BeanUtil.copyProperties(user, uiUser);
        log.info("profile----->{}", uiUser.toString());
        return new SimpleAuthenticationInfo(uiUser, token.getCredentials(), getName());
    }
}
