package com.example.application.views.stock;

import java.util.List;

import com.example.application.backend.model.Stock;
import com.example.application.backend.service.StockService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "stock", layout = MainView.class)
@PageTitle("Stock List")
public class StockView extends VerticalLayout {
	private StockService stockService;
	
	private List<Stock> stock;
	private ListDataProvider<Stock> stockProvider;
	
	private HorizontalLayout toolBarLayout;
	private Button refreshStock;
	private Button addStock;
	private Grid<Stock> gridStock = new Grid<>(Stock.class);
	
	public StockView(StockService stockService) {
		this.setSizeFull();
		this.setPadding(true);
		
		this.stockService = stockService;
		
		// load data from service
		loadData();
		
		// fill grid with data
		configureGrid();
				
		// create view layput
		createViewLayout();
	}
	
	private void loadData() {
		try {
			this.stock = stockService.findAll();
			//Notification.show("SamplePerson details stored.");
		}
		catch(Exception ex) {
			ex.printStackTrace();			
		}
	}
	
	private void createViewLayout() {
		toolBarLayout = new HorizontalLayout();
		toolBarLayout.setPadding(true);
		toolBarLayout.setWidthFull();
		
		addStock = new Button("Add Stock", clickEvent -> {
			 
		});
		addStock.getElement().getStyle().set("margin-right", "auto");
		addStock.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		refreshStock = new Button("Refresh Stock", clickEvent -> {
			// load data from service
			 loadData();
			 
			// fill grid with data
			 loadGrid();
		});		
		
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
	
	private void loadGrid() {
		stockProvider =  DataProvider.ofCollection(this.stock);
		
		gridStock.setDataProvider(stockProvider);
		//gridProduct.getDataProvider().refreshAll();
	}
	
	private Button updateStockButton(Grid<Stock> grid, Stock item) {
	    @SuppressWarnings("unchecked")
	    Button button = new Button("Update", clickEvent -> {
	        /*ListDataProvider<Product> dataProvider = (ListDataProvider<Product>) grid.getDataProvider();
	        
	        dataProvider.getItems().remove(item);	        
	        dataProvider.refreshAll();*/
	    });
	    
	    return button;
	}
	
	private Button removeStockButton(Grid<Stock> grid, Stock item) {
	    @SuppressWarnings("unchecked")
	    Button button = new Button("Remove", clickEvent -> {
	        ListDataProvider<Stock> dataProvider = (ListDataProvider<Stock>) grid.getDataProvider();
	        
	        dataProvider.getItems().remove(item);
	        dataProvider.refreshAll();
	    });
	    button.addThemeVariants(ButtonVariant.LUMO_ERROR);
	    
	    return button;
	}
}
