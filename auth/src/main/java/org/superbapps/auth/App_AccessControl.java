package org.superbapps.auth;

public class App_AccessControl extends ShiroAccessControl {

    public App_AccessControl() {
        super("classpath:config.ini");
    }
}
