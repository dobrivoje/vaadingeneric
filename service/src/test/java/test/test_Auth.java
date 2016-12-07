package test;

import authentication.config.AuthConfig;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.superbapps.auth.IAccessAuthControl;
import org.superbapps.auth.roles.Roles;

public class test_Auth {

    ApplicationContext context;
    IAccessAuthControl auth;
    String command;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(AuthConfig.class);
        auth = context.getBean(IAccessAuthControl.class);
    }

    @Test
    public void test() {
        Subject s1 = auth.generateSubject();
        auth.login(s1, "cbs", "...");
        System.err.println("sessID : " + s1.getSession().getId());
        System.err.println("is authenticated ? " + auth.authenticated(s1));

        Subject s2 = auth.generateSubject();
        auth.login(s2, "cbs", "...");
        System.err.println("sessID : " + s2.getSession().getId());
        System.err.println("is authenticated ? " + auth.authenticated(s2));
        
        Subject s3 = auth.generateSubject();
        auth.login(s3, "root", "...");
        System.err.println("sessID : " + s3.getSession().getId());
        System.err.println("is authenticated ? " + auth.authenticated(s3));
        

        System.err.println("-----------------Roles--------------------------");

        for (Roles r : Roles.values()) {
            if (auth.hasRole(s1, r)) {
                System.err.println(s1.getPrincipal() + " HAS ROLE : " + r);
            }

            if (auth.hasRole(s2, r)) {
                System.err.println(s2.getPrincipal() + " HAS ROLE : " + r);
            }
            
            if (auth.hasRole(s3, r)) {
                System.err.println(s3.getPrincipal() + " HAS ROLE : " + r);
            }
            

        }
    }

}
