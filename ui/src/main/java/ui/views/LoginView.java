package ui.views;

import com.google.common.eventbus.Subscribe;
import events.DashboardEvent.UserLoginRequestedEvent;
import events.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import events.DashboardEvent.UserLoggingEvent;
import static ui.MainUI.appBundle;
import static ui.MainUI.uiBundle;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    private final Button login = new Button(uiBundle("LOGIN.BTN.LOGIN.CAPTION"));
    final TextField username = new TextField(uiBundle("LOGIN.TB.UN.CAPTION"));
    final PasswordField password = new PasswordField(uiBundle("LOGIN.TB.PASS.CAPTION"));

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification(uiBundle("LOGIN.WELCOME"));
        notification.setDescription(appBundle("APP.MOD.CONTENT"));
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
        notification.show(Page.getCurrent());
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(new CheckBox(uiBundle("LOGIN.CB.CAPTION"), true));
        return loginPanel;
    }

    @Subscribe
    public void disableLoginButton(UserLoggingEvent event) {
        login.setDisableOnClick(!event.isLogged());
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        disableLoginButton(new UserLoggingEvent(false));
        // signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        login.setClickShortcut(KeyCode.ENTER);
        username.focus();

        fields.addComponents(username, password, login);
        fields.setComponentAlignment(login, Alignment.BOTTOM_LEFT);

        login.addClickListener((final ClickEvent event) -> {
            DashboardEventBus.post(new UserLoginRequestedEvent(username.getValue().toLowerCase().trim(), password.getValue()));
        });

        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label(uiBundle("LOGIN.WELCOME"));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label(uiBundle("LOGIN.TITLE"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

}
