package org.superbapps.auth;

import org.apache.shiro.subject.Subject;

/**
 * Simple interface for authentication and authorization checks.
 */
public interface IAccessAuthControl {

    /**
     * Create a new subject from the service
     * @return 
     */
    Subject generateSubject();

    boolean login(Subject subject, String username, String password);

    void logout(Subject subject);

    boolean authenticated(Subject subject);

    boolean hasRole(Subject subject, String role);

    boolean hasRole(Subject subject, Enum role);

    boolean isPermitted(Subject subject, String permission);

    boolean isPermitted(Subject subject, Enum permission);

}
