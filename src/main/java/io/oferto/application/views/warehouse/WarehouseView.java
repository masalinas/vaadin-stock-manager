package io.oferto.application.views.warehouse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import io.oferto.application.backend.model.Warehouse;
import io.oferto.application.backend.service.ProductService;
import io.oferto.application.backend.service.WarehouseService;
import io.oferto.application.views.main.MainView;
import io.oferto.application.views.warehouse.form.WarehouseForm;

@Route(value = "warehouse", layout = MainView.class)
@PageTitle("Stock Manager | Warehouse List")
@CssImport("./views/warehouse/warehouse-view.css")
public class WarehouseView extends VerticalLayout {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int NOTIFICATION_DEFAULT_DURATION = 5000;
	
	private WarehouseService warehouseService;
	
	private List<Warehouse> warehouses;
	private ListDataProvider<Warehouse> warehouseProvider;
	
	private HorizontalLayout toolBarLayout;
	private Button refreshWarehouses;
	private Button addWarehouse;
	private Grid<Warehouse> gridWarehouse = new Grid<>(Warehouse.class); 
	
	public WarehouseView(WarehouseService warehouseService, ProductService productService) {
		addClassName("warehouse-view");
		
		this.setSizeFull();
		this.setPadding(true);
		
		this.warehouseService = warehouseService;
				
		// load data from service
		loadData();
		
		// fill grid with data
		configureGrid();
				
		// create view layput
		createViewLayout();
	}
	
	private void loadData() {
		try {
			this.warehouses = warehouseService.findAll();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			
			logger.debug(ex.getLocalizedMessage());
		}
	}

	private void loadGrid() {
		warehouseProvider =  DataProvider.ofCollection(this.warehouses);
		warehouseProvider.setSortOrder(Warehouse::getName, SortDirection.ASCENDING);
		
		gridWarehouse.setDataProvider(warehouseProvider);
	}
	
	private void createViewLayout() {
		toolBarLayout = new HorizontalLayout();
		toolBarLayout.setPadding(true);
		toolBarLayout.setWidthFull();
		
		addWarehouse = new Button("Add Warehouse", clickEvent -> createWarehouseButton(clickEvent));		
		addWarehouse.getElement().getStyle().set("margin-right", "auto");
		addWarehouse.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		refreshWarehouses = new Button("Refresh Warehouses", clickEvent -> refreshWarehouses(clickEvent));		
		
		toolBarLayout.add(addWarehouse, refreshWarehouses);
		
		gridWarehouse.setSizeFull();
		
		add(toolBarLayout);	 		
		add(gridWarehouse);	
	}
	
	private void configureGrid() {
		loadGrid();
		
		gridWarehouse.setColumns("name", "address");
		gridWarehouse.getColumnByKey("name").setHeader("Warehouse");
		gridWarehouse.getColumnByKey("address").setFooter("Total: " + this.warehouses.size() + " warehouses");		

		gridWarehouse.addComponentColumn(item -> updateWarehouseButton(gridWarehouse, item)).setHeader("");
		gridWarehouse.addComponentColumn(item -> removeWarehouseButton(gridWarehouse, item)).setHeader("");
		
		gridWarehouse.addThemeVariants(GridVariant.LUMO_NO_BORDER, 
									 GridVariant.LUMO_NO_ROW_BORDERS, 
									 GridVariant.LUMO_ROW_STRIPES);
	}
		
	private void refreshWarehouses(ClickEvent e) {
		try {
			// load data from service
			loadData();
		 
		 	// fill grid with data
		 	loadGrid();
		} catch (Exception ex) {
	    	logger.error(ex.getMessage());
	    	
	    	Notification.show(ex.getMessage());
	    }
	}
	
	private void createWarehouseButton(ClickEvent e) {
		// define form dialog
    	WarehouseForm warehouseForm = new WarehouseForm();
    	warehouseForm.setWidth("700px");
    	warehouseForm.setCloseOnEsc(true);			
    	warehouseForm.setCloseOnOutsideClick(false);
    	
    	// bind form dialog with product entity
    	warehouseForm.setWarehouse(new Warehouse());
    	
    	// define form dialog view callback
    	warehouseForm.addOpenedChangeListener(event -> {
    	     if(!event.isOpened()) {	    	    	 
    	    	 if (warehouseForm.getDialogResult() == WarehouseForm.DIALOG_RESULT.SAVE)	    	    	 
    	    	 	try {
    	    	 		// save product entity
    	    	 		warehouseService.save(warehouseForm.getWarehouse());
    	    	 		
    	    	 		// refresh grid
    	    	 		refreshWarehouses(null);
    	    	 		
	    	    	 	Notification.show("Product Saved", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    	    } catch (Exception ex) {
    	    	    	logger.error(ex.getMessage());
    	    	    	
    	    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    	    }	    	    	
    	     }
    	});
    		
    	// open form dialog view
    	warehouseForm.open();
	}
	
	private Button updateWarehouseButton(Grid<Warehouse> grid, Warehouse warehouse) {
	    Button button = new Button("Update", clickEvent -> {
	    	// define form dialog
	    	WarehouseForm warehouseForm = new WarehouseForm();
	    	warehouseForm.setWidth("700px");
	    	warehouseForm.setCloseOnEsc(true);			
	    	warehouseForm.setCloseOnOutsideClick(false);
	    	
	    	// bind form dialog with product entity
	    	warehouseForm.setWarehouse(warehouse);
	    	
	    	// define form dialog view callback
	    	warehouseForm.addOpenedChangeListener(event -> {
	    	     if(!event.isOpened()) {	    	    	 
	    	    	 if (warehouseForm.getDialogResult() == WarehouseForm.DIALOG_RESULT.SAVE)	    	    	 
	    	    	 	try {
	    	    	 		// save product entity
	    	    	 		warehouseService.save(warehouseForm.getWarehouse());
	    	    	 		
	    	    	 		// refresh grid
	    	    	 		//loadGrid();
	    	    	 		refreshWarehouses(null);
	    	    	 		
		    	    	 	Notification.show("Warehouse Updated", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
	    	    	    } catch (Exception ex) {
	    	    	    	logger.error(ex.getMessage());
	    	    	    	
	    	    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
	    	    	    }	    	    	
	    	     }
	    	});
	    		
	    	// open form dialog view
	    	warehouseForm.open();
	    });
	    
	    return button;
	}
	
	private Button removeWarehouseButton(Grid<Warehouse> grid, Warehouse product) {
	    Button button = new Button("Remove", clickEvent -> {	        
	        try {	        
    	 		// save product entity
    	 		warehouseService.delete(product);
    	 		
    	 		// refresh grid
    	 		refreshWarehouses(null);
    	        
	    	 	Notification.show("Warehouse Deleted", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    } catch (Exception ex) {
    	    	logger.error(ex.getMessage());
    	    	
    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    }	        
	    });
	    
	    button.addThemeVariants(ButtonVariant.LUMO_ERROR);
	    
	    return button;
	}
}
