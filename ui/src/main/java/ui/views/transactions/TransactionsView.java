package ui.views.transactions;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import org.vaadin.maddon.FilterableListContainer;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import ui.MainUI;
import ui.components.MovieDetailsWindow;
import domain.Transaction;
import infra.events.DashboardEvent.BrowserResizeEvent;
import infra.events.DashboardEvent.TransactionReportEvent;
import infra.events.DashboardEventBus;
import ui.views.DashboardViewType;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import static ui.MainUI.appBundle;
import static ui.MainUI.uiBundle;

@SuppressWarnings({"serial", "unchecked"})
public final class TransactionsView extends VerticalLayout implements View {

    private final Table table;
    private Button createReport;
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "dd.MM.yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = {"country", "city",
        "theater", "room", "title", "seats"};

    public TransactionsView() {
        setSizeFull();
        addStyleName("transactions");
        DashboardEventBus.register(this);

        addComponent(buildToolbar());

        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label(uiBundle("TransactionsView.Title"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        createReport = buildCreateReport();
        HorizontalLayout tools = new HorizontalLayout(buildFilter(),
                createReport);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Button buildCreateReport() {
        final Button createReport = new Button(uiBundle("TransactionsView.CreateReport"));
        createReport.setDescription(uiBundle("TransactionsView.description"));
        createReport.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                createNewReportFromSelection();
            }
        });
        createReport.setEnabled(false);
        return createReport;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(final TextChangeEvent event) {
                Filterable data = (Filterable) table.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId,
                            final Item item) {

                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("country", item,
                                event.getText())
                                || filterByProperty("city", item,
                                        event.getText())
                                || filterByProperty("title", item,
                                        event.getText());

                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("country")
                                || propertyId.equals("city")
                                || propertyId.equals("title")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        filter.setInputPrompt("Filter");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                filter.setValue("");
                ((Filterable) table.getContainerDataSource())
                        .removeAllContainerFilters();
            }
        });
        return filter;
    }

    private Table buildTable() {
        final Table table2 = new Table() {
            @Override
            protected String formatPropertyValue(final Object rowId,
                    final Object colId, final Property<?> property) {
                String result = super.formatPropertyValue(rowId, colId,
                        property);
                if (colId.equals("time")) {
                    result = DATEFORMAT.format(((Date) property.getValue()));
                } else if (colId.equals("price")) {
                    if (property != null && property.getValue() != null) {
                        return "$" + DECIMALFORMAT.format(property.getValue());
                    } else {
                        return "";
                    }
                }
                return result;
            }
        };
        table2.setSizeFull();
        table2.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table2.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table2.addStyleName(ValoTheme.TABLE_COMPACT);
        table2.setSelectable(true);

        table2.setColumnCollapsingAllowed(true);
        table2.setColumnCollapsible("time", false);
        table2.setColumnCollapsible("price", false);

        table2.setColumnReorderingAllowed(true);
        table2.setContainerDataSource(new TempTransactionsContainer(MainUI
                .getDataProvider().getRecentTransactions(200)));
        table2.setSortContainerPropertyId("time");
        table2.setSortAscending(false);

        table2.setColumnAlignment("seats", Align.RIGHT);
        table2.setColumnAlignment("price", Align.RIGHT);

        table2.setVisibleColumns("time", "country", "city", "theater", "room",
                "title", "seats", "price");
        table2.setColumnHeaders("Time", "Country", "City", "Theater", "Room",
                "Title", "Seats", "Price");

        table2.setFooterVisible(true);
        table2.setColumnFooter("time", "Total");

        table2.setColumnFooter("price",
                "$"
                + DECIMALFORMAT.format(MainUI.getDataProvider()
                        .getTotalSum()));

        // Allow dragging items to the reports menu
        table2.setDragMode(TableDragMode.MULTIROW);
        table2.setMultiSelect(true);

        table2.addActionHandler(new TransactionsActionHandler());

        table2.addValueChangeListener((final ValueChangeEvent event) -> {
            if (table2.getValue() instanceof Set) {
                Set<Object> val = (Set<Object>) table2.getValue();
                createReport.setEnabled(val.size() > 0);
            }
        });
        table2.setImmediate(true);

        return table2;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (table.isColumnCollapsed(propertyId) == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    public void browserResized(final BrowserResizeEvent event) {
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        if (defaultColumnsVisible()) {
            for (String propertyId : DEFAULT_COLLAPSIBLE) {
                table.setColumnCollapsed(propertyId, Page.getCurrent()
                        .getBrowserWindowWidth() < 800);
            }
        }
    }

    private boolean filterByProperty(final String prop, final Item item,
            final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    void createNewReportFromSelection() {
        UI.getCurrent().getNavigator()
                .navigateTo(DashboardViewType.REPORTS.getViewNavigName());
        DashboardEventBus.post(new TransactionReportEvent(
                (Collection<Transaction>) table.getValue()));
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TransactionsActionHandler implements Handler {

        private final Action report = new Action(uiBundle("TransactionsView.CreateReport"));
        private final Action discard = new Action(uiBundle("TransactionsView.Discard"));
        private final Action details = new Action(uiBundle("TransactionsView.MovieDetails"));

        @Override
        public void handleAction(final Action action, final Object sender,
                final Object target) {
            if (action == report) {
                createNewReportFromSelection();
            } else if (action == discard) {
                Notification.show(appBundle("APP.NOTIMPLEMENTED"));
            } else if (action == details) {
                Item item = ((Table) sender).getItem(target);
                if (item != null) {
                    Long movieId = (Long) item.getItemProperty("movieId")
                            .getValue();
                    MovieDetailsWindow.open(MainUI.getDataProvider()
                            .getMovie(movieId), null, null);
                }
            }
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return new Action[]{details, report, discard};
        }
    }

    private class TempTransactionsContainer extends
            FilterableListContainer<Transaction> {

        public TempTransactionsContainer(
                final Collection<Transaction> collection) {
            super(collection);
        }

        // This is only temporarily overridden until issues with
        // BeanComparator get resolved.
        @Override
        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            final Object sortContainerPropertyId = propertyId[0];
            Collections.sort(getBackingList(), (final Transaction o1, final Transaction o2) -> {
                int result = 0;

                switch (sortContainerPropertyId.toString()) {
                    case "time":
                        result = o1.getTime().compareTo(o2.getTime());
                        break;
                    case "country":
                        result = o1.getCountry().compareTo(o2.getCountry());
                        break;
                    case "city":
                        result = o1.getCity().compareTo(o2.getCity());
                        break;
                    case "theater":
                        result = o1.getTheater().compareTo(o2.getTheater());
                        break;
                    case "room":
                        result = o1.getRoom().compareTo(o2.getRoom());
                        break;
                    case "title":
                        result = o1.getTitle().compareTo(o2.getTitle());
                        break;
                    case "seats":
                        result = new Integer(o1.getSeats()).compareTo(o2.getSeats());
                        break;
                    case "price":
                        result = new Double(o1.getPrice()).compareTo(o2.getPrice());
                        break;
                }

                if (!sortAscending) {
                    result *= -1;
                }
                return result;
            });
        }

    }

}
