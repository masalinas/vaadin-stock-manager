package io.oferto.application.views.stock;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import io.oferto.application.backend.model.Stock;
import io.oferto.application.backend.model.Warehouse;
import io.oferto.application.backend.service.ProductService;
import io.oferto.application.backend.service.StockService;
import io.oferto.application.backend.service.WarehouseService;
import io.oferto.application.views.main.MainView;
import io.oferto.application.views.stock.form.StockForm;

@Route(value = "stock", layout = MainView.class)
@PageTitle("Product Manager | Stock List")
@CssImport("./views/stock/stock-view.css")
public class StockView extends VerticalLayout implements HasUrlParameter<String> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int NOTIFICATION_DEFAULT_DURATION = 5000;
	
	private WarehouseService warehouseService;
	private ProductService productService;
	private StockService stockService;
	
	private List<Stock> stock;
	private ListDataProvider<Stock> stockProvider;
	
	private HorizontalLayout toolBarLayout;
	private Button refreshStock;
	private Button addStock;
	private Grid<Stock> gridStock = new Grid<>(Stock.class);
	
	public StockView(WarehouseService warehouseService,ProductService productService, StockService stockService) {
		addClassName("stock-view");
		
		this.setSizeFull();
		this.setPadding(true);
		
		this.warehouseService = warehouseService;
		this.productService = productService;
		this.stockService = stockService;
		
		// load data from service
		loadStocks();
		
		// fill grid with data
		configureGrid();
				
		// create view layput
		createViewLayout();
	}
	
	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
	    Location location = event.getLocation();
	    QueryParameters queryParameters = location.getQueryParameters();

	    Map<String, List<String>> parametersMap = queryParameters.getParameters();
	    
	    if (parametersMap.get("warehouseId") != null) {
	    	int warehouseId = Integer.parseInt(parametersMap.get("warehouseId").get(0));
	    	
	    	getStockByWarehouse(warehouseId);
	    }
	}
	
	private void loadStocks() {
		try {
			this.stock = stockService.findAll();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			
			logger.debug(ex.getLocalizedMessage());
		}
	}
	
	private void getStockByWarehouse(int warehouseId) {
		try {
			Optional<Warehouse> warehouse = warehouseService.findById(warehouseId);
			
			if (!warehouse.isEmpty()) {
				this.stock = warehouse.get().getStocks(); 
				
				loadGrid();
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			
			logger.debug(ex.getLocalizedMessage());
		}
	}
	
	private void loadGrid() {
		stockProvider =  DataProvider.ofCollection(this.stock);
		
		gridStock.setDataProvider(stockProvider);
	}
	
	private void refreshStocks(ClickEvent e) {
		try {
			// load data from service
			loadStocks();
		 
		 	// fill grid with data
		 	loadGrid();
		} catch (Exception ex) {
	    	logger.error(ex.getMessage());
	    	
	    	Notification.show(ex.getMessage());
	    }
	}
	
	private void createViewLayout() {
		toolBarLayout = new HorizontalLayout();
		toolBarLayout.setPadding(true);
		toolBarLayout.setWidthFull();
		
		addStock = new Button("Add Stock", clickEvent -> createStockButton(clickEvent));
		
		addStock.getElement().getStyle().set("margin-right", "auto");
		addStock.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		refreshStock = new Button("Refresh Stock", clickEvent -> refreshStocks(clickEvent));		
		
		toolBarLayout.add(addStock, refreshStock);
		
		gridStock.setSizeFull();
		
		add(toolBarLayout);	 		
		add(gridStock);	
	}
	
	private void configureGrid() {
		loadGrid();
		
		gridStock.setColumns("product.name", "quantity", "expirationDate", "lot", "serialNumber", "status");
		gridStock.getColumnByKey("product.name").setFooter("Total: " + this.stock.size() + " stock lines");		
		
		gridStock.addComponentColumn(item -> updateStockButton(gridStock, item)).setHeader("");
		gridStock.addComponentColumn(item -> removeStockButton(gridStock, item)).setHeader("");
		gridStock.addThemeVariants(GridVariant.LUMO_NO_BORDER, 
								   GridVariant.LUMO_NO_ROW_BORDERS, 
								   GridVariant.LUMO_ROW_STRIPES);
	}
	
	private void createStockButton(ClickEvent e) {
		// define form dialog
    	StockForm stockForm = new StockForm(productService);
    	stockForm.setWidth("700px");
    	stockForm.setCloseOnEsc(true);			
    	stockForm.setCloseOnOutsideClick(false);
    	
    	// bind form dialog with product entity
    	stockForm.setStock(new Stock());
    	
    	// define form dialog view callback
    	stockForm.addOpenedChangeListener(event -> {
    	     if(!event.isOpened()) {	    	    	 
    	    	 if (stockForm.getDialogResult() == StockForm.DIALOG_RESULT.SAVE)	    	    	 
    	    	 	try {
    	    	 		// save product entity
    	    	 		stockService.save(stockForm.getStock());
    	    	 		
    	    	 		// refresh grid
    	    	 		refreshStocks(null);
    	    	 		
	    	    	 	Notification.show("St6ock Saved", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    	    } catch (Exception ex) {
    	    	    	logger.error(ex.getMessage());
    	    	    	
    	    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    	    }	    	    	
    	     }
    	});
    		
    	// open form dialog view
    	stockForm.open();
	}
	
	private Button updateStockButton(Grid<Stock> grid, Stock stock) {
		Button button = new Button("Update", clickEvent -> {
			// define form dialog
	    	StockForm stockForm = new StockForm(productService);
	    	stockForm.setWidth("700px");
	    	stockForm.setCloseOnEsc(true);			
	    	stockForm.setCloseOnOutsideClick(false);
	    	
	    	// bind form dialog with product entity
	    	stockForm.setStock(stock);
	    	
	    	// define form dialog view callback
	    	stockForm.addOpenedChangeListener(event -> {
	    	     if(!event.isOpened()) {	    	    	 
	    	    	 if (stockForm.getDialogResult() == StockForm.DIALOG_RESULT.SAVE)	    	    	 
	    	    	 	try {
	    	    	 		// save product entity
	    	    	 		stockService.save(stockForm.getStock());
	    	    	 		
	    	    	 		// refresh grid
	    	    	 		//loadGrid();
	    	    	 		refreshStocks(null);
	    	    	 		
		    	    	 	Notification.show("Stock Updated", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
	    	    	    } catch (Exception ex) {
	    	    	    	logger.error(ex.getMessage());
	    	    	    	
	    	    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
	    	    	    }	    	    	
	    	     }
	    	});
	    		
	    	// open form dialog view
	    	stockForm.open();
	    });
	    
	    return button;
	}
	
	private Button removeStockButton(Grid<Stock> grid, Stock stock) {
	    Button button = new Button("Remove", clickEvent -> {	        
	        try {	        
    	 		// save product entity
    	 		stockService.delete(stock);
    	 		
    	 		// refresh grid
    	 		refreshStocks(null);
    	        
	    	 	Notification.show("Stock Deleted", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    } catch (Exception ex) {
    	    	logger.error(ex.getMessage());
    	    	
    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    }	        
	    });
	    
	    button.addThemeVariants(ButtonVariant.LUMO_ERROR);
	    
	    return button;
	}
}
