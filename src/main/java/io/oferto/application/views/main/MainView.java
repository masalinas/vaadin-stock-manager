package io.oferto.application.views.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;

import io.oferto.application.security.SecurityConfiguration;
import io.oferto.application.views.about.AboutView;
import io.oferto.application.views.dashboard.DashboardView;
import io.oferto.application.views.main.MainView;
import io.oferto.application.views.product.ProductView;
import io.oferto.application.views.stock.StockView;
import io.oferto.application.views.warehouse.WarehouseView;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.CssImport;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "stock-manager", shortName = "stock-manager", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
@CssImport("lumo-css-framework/all-classes.css")
@CssImport("./views/main/main-view.css")
@Theme(value = Lumo.class)
public class MainView extends AppLayout {

	//private UserDetails userDetails;
    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
                  
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));                
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        
        viewTitle = new H1();
        
        layout.add(viewTitle);
 
        //layout.add(new Avatar());
        layout.add(createAvatarMenu());
        
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        
        HorizontalLayout logoLayout = new HorizontalLayout();
        
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "stock-manager logo"));
        logoLayout.add(new H2("Stock Manager"));
        layout.add(logoLayout, menu);
        
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        
        return tabs;
    }

    private Component[] createMenuItems() {    	    	
    	List<Tab> tabs = new ArrayList<Tab>();
    	
    	tabs.add(createTab("Dashboard", DashboardView.class));
    	if (SecurityConfiguration.isAdmin())
    		tabs.add(createTab("Warehouse", WarehouseView.class));
    	tabs.add(createTab("Product Master", ProductView.class));
    	tabs.add(createTab("Stock List", StockView.class));
    	tabs.add(createTab("About", AboutView.class));
    	    	
    	return tabs.toArray(new Tab[tabs.size()]);
    }
    
    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private Component createAvatarMenu() {    	
    	// get security context    	
    	Avatar avatar = new Avatar();    	
    	avatar.setName(SecurityConfiguration.getUserDetails().getUsername());
    	
    	ContextMenu contextMenu = new ContextMenu();
    	contextMenu.setOpenOnClick(true);
    	contextMenu.setTarget(avatar);
    	
    	contextMenu.addItem("Profile", e -> {    	        	
    	});

    	contextMenu.addItem("Logout", e -> {
    		contextMenu.getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
    	});
    	
    	return avatar;
    }
    
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        
        return title == null ? "" : title.value();
    }
}
