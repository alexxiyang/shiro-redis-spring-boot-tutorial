package org.crazycake.shiroredisspringboottutorial.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.crazycake.shiroredisspringboottutorial.model.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private static final transient Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm){

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        checkSession(session);

        if (!currentUser.isAuthenticated()) {
            currentUser.login(new UsernamePasswordToken(loginForm.getUsername(), loginForm.getPassword()));
        }

        checkAuthorization(currentUser);

        return "<h1>Hello " + loginForm.getUsername() + "</h1><p>session: " + session.getId() + "<br><a href='/logout'>Logout</a>";
    }

    @GetMapping("/logout")
    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "You've logout";
    }

    private void checkSession(Session session) {
        // Try to set value to redis-based session
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (!value.equals("aValue")) {
            log.info("Cannot retrieved the correct value! [" + value + "]");
        }
    }

    private void checkAuthorization(Subject currentUser) {
        // say who they are:
        // print their identifying principal (in this case, a username):
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        if ( currentUser.hasRole( "schwartz" ) ) {
            log.info("May the Schwartz be with you!" );
        } else {
            log.info( "Hello, mere mortal." );
        }
    }

}
