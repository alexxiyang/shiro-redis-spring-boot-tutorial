package org.crazycake.shiroredisspringboottutorial.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.crazycake.shiroredisspringboottutorial.model.UserInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This realm is only for tutorial
 * @author Alex Yang
 *
 */
@Repository
public class ExampleRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        // only for tutorial
        authInfo.addRoles(new ArrayList<String>(Arrays.asList("schwartz")));
        return authInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        UserInfo userInfo = new UserInfo();
        userInfo.setId(ThreadLocalRandom.current().nextInt(1, 100));
        userInfo.setUsername(usernamePasswordToken.getUsername());
        // Expect password is "123456"
        return new SimpleAuthenticationInfo(userInfo, "123456", getName());
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new SimpleCredentialsMatcher());
    }

}