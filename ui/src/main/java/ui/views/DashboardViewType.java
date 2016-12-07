package ui.views;

import ui.views.dashboard.DashboardView;
import ui.views.reports.ReportsView;
import ui.views.schedule.ScheduleView;
import ui.views.transactions.TransactionsView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import static ui.MainUI.uiBundle;

public enum DashboardViewType {
    DASHBOARD(uiBundle("DashboardViewType.dashboard"), "dashboard", DashboardView.class, FontAwesome.HOME, true),
    SALES(uiBundle("DashboardViewType.sales"), "sales", DashboardView.class, FontAwesome.BAR_CHART_O, false),
    TRANSACTIONS(uiBundle("DashboardViewType.transactions"), "transactions", TransactionsView.class, FontAwesome.TABLE, false),
    REPORTS(uiBundle("DashboardViewType.reports"), "reports", ReportsView.class, FontAwesome.FILE_TEXT_O, true),
    SCHEDULE(uiBundle("DashboardViewType.schedule"), "schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false);

    private final String menuName;
    private final String viewNavigName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
            final String viewNavigName,
            final Class<? extends View> viewClass,
            final Resource icon,
            final boolean stateful) {
        this.menuName = viewName;
        this.viewNavigName = viewNavigName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getViewNavigName() {
        return viewNavigName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewMenuName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getMenuName().equals(viewMenuName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

    public static DashboardViewType getByViewNavigName(final String viewNavigName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewNavigName().equals(viewNavigName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
