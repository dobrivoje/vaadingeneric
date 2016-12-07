package infra;

import authentication.config.AuthConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.shiro.subject.Subject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.superbapps.auth.IAccessAuthControl;
import static org.superbapps.utils.common.Enums.ServletOperations.AUTH_CONTROLLER;
import static org.superbapps.utils.common.Enums.ServletOperations.AUTH_SUBJECT;

public class AccessControlListener implements HttpSessionListener {

    private static final ApplicationContext CTX_AUTH = new AnnotationConfigApplicationContext(AuthConfig.class);
    private final IAccessAuthControl ACCCTRL = CTX_AUTH.getBean(IAccessAuthControl.class);

    @Override
    public void sessionCreated(HttpSessionEvent session) {
        session.getSession().setAttribute(AUTH_CONTROLLER.toString(), ACCCTRL);
        session.getSession().setAttribute(AUTH_SUBJECT.toString(), ACCCTRL.generateSubject());

        Logger.getLogger(AccessControlListener.class.getSimpleName()).log(Level.INFO,
                "Session Listener: Shiro Subject Session : {0}",
                ((Subject) session.getSession().getAttribute(AUTH_SUBJECT.toString())).getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent session) {
        try {
            Logger.getLogger(AccessControlListener.class.getSimpleName())
                    .log(Level.INFO, "Session Listener: Session Destroyed Succesfully");

            Subject subject = (Subject) session.getSession().getAttribute(AUTH_SUBJECT.toString());
            ACCCTRL.logout(subject);

            session.getSession().invalidate();
        } catch (Exception e) {
            Logger.getLogger(AccessControlListener.class.getSimpleName())
                    .log(Level.SEVERE, "Session Destroyed Error : {0}", e.getMessage());
        }
    }

}
