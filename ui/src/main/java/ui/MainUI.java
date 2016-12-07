package ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import data.DataProvider;
import data.dummy.DummyDataProvider;
import events.DashboardEvent.BrowserResizeEvent;
import events.DashboardEvent.CloseOpenWindowsEvent;
import events.DashboardEvent.UserLoggedOutEvent;
import events.DashboardEvent.UserLoginRequestedEvent;
import events.DashboardEventBus;
import ui.views.LoginView;
import ui.views.MainView;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import events.DashboardEvent.UserLoggingEvent;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import org.apache.shiro.subject.Subject;
import org.superbapps.auth.IAccessAuthControl;
import org.superbapps.auth.roles.Roles;
import org.superbapps.utils.common.Enums.ServletOperations;
import static org.superbapps.utils.common.Enums.ServletOperations.AUTH_USERNAME;

@Theme("dashboard")
@Widgetset("ui.DashboardWidgetSet")
@Title("Vaadin app")
@SuppressWarnings("serial")
public final class MainUI extends UI {

    //<editor-fold defaultstate="collapsed" desc="Services">
    private IAccessAuthControl accessController;
    private Subject subject;

    private final DataProvider dataProvider = new DummyDataProvider();
    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

    private final ResourceBundle mainBundle = ResourceBundle.getBundle("config/appSettings");
    private final String uiPath = mainBundle.getString("UI");
    private final String appPath = mainBundle.getString("APP");

    private final ResourceBundle uiBundle = ResourceBundle.getBundle(uiPath);
    private final ResourceBundle appBundle = ResourceBundle.getBundle(appPath);
    //</editor-fold>

    private LoginView loginView;
    private MainView mainView;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        accessController = (IAccessAuthControl) vaadinRequest.getWrappedSession().getAttribute(
                ServletOperations.AUTH_CONTROLLER.toString());
        subject = (Subject) vaadinRequest.getWrappedSession().getAttribute(
                ServletOperations.AUTH_SUBJECT.toString());

        // setLocale(Locale.US);
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        doUserCheck();

        Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
            @Override
            public void browserWindowResized(final BrowserWindowResizeEvent event) {
                DashboardEventBus.post(new BrowserResizeEvent());
            }
        });
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) throws FileNotFoundException {
        if (accessController.login(subject, event.getUserName(), event.getPassword())) {
            DashboardEventBus.post(new UserLoggingEvent(true));

            WrappedSession vaadinSession = VaadinSession.getCurrent().getSession();
            vaadinSession.setAttribute(AUTH_USERNAME.toString(), event.getUserName());
            vaadinSession.setAttribute(vaadinSession.getId(), true);

        } else {
            showNotif(new Notification(
                    uiBundle.getString("LOGIN.FAILED.TITLE"),
                    uiBundle.getString("LOGIN.FAILED.CONTENT"),
                    Notification.Type.ERROR_MESSAGE)
            );
        }

        doUserCheck();
    }

    private void doUserCheck() {
        boolean authenticated = subject.isAuthenticated();

        if (!authenticated) {
            DashboardEventBus.post(new UserLoggingEvent(!authenticated));
            setContent(loginView != null ? loginView : new LoginView());
            addStyleName("loginview");
        } else {
            DashboardEventBus.post(new UserLoggingEvent(authenticated));
            setContent(mainView != null ? mainView : new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="infra">
    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        VaadinSession.getCurrent().getSession().invalidate();
        MainUI.getCurrent().getPage().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    private void showNotif(Notification notification) {
        notification.setDelayMsec(3000);
        notification.setHtmlContentAllowed(true);
        notification.show(Page.getCurrent());
    }

    public static DataProvider getDataProvider() {
        return ((MainUI) getCurrent()).dataProvider;
    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((MainUI) getCurrent()).dashboardEventbus;
    }

    public static String uiBundle(String key) {
        return ((MainUI) getCurrent()).uiBundle.getString(key);
    }

    public static String appBundle(String key) {
        return ((MainUI) getCurrent()).appBundle.getString(key);
    }

    public static MainUI get() {
        return (MainUI) UI.getCurrent();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Interfaces">
    public Subject getSubject() {
        return subject;
    }

    public boolean isPermitted(Roles permission) {
        return accessController.isPermitted(subject, permission);
    }

    public boolean hasRole(Roles role) {
        return accessController.hasRole(subject, role);
    }
    //</editor-fold>
}
