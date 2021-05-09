package io.oferto.application.views.stock.form;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import io.oferto.application.backend.model.Stock;
import io.oferto.application.backend.model.Stock.Status;
import io.oferto.application.backend.service.ProductService;
import io.oferto.application.backend.model.Product;

public class StockForm extends Dialog {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static enum DIALOG_RESULT {SAVE, CANEL};
	
	private ProductService productService;
	
	private DIALOG_RESULT dialogResult;
	
	private Stock stock;	
	private Binder<Stock> stockBinder = new Binder<Stock>(Stock.class);
	
	private FormLayout formLayout;
	
	private ComboBox<Product> product;
	private TextField lot;
	private TextField serialNumber;
	private DatePicker expirationDate;
	private NumberField quantity;
	private ComboBox<Status> status;
	
	public StockForm(ProductService productService) {
		super();	
		
		this.productService = productService;
		
		// create dialog layout
		add(createTitle(), createFormLayout(), new Hr(), createToolbarLayout());
		
		stockBinder.bindInstanceFields(this);
	}
	
	public DIALOG_RESULT getDialogResult() {
		return this.dialogResult;
	}
	
	public void setStock(Stock stock) {
		this.stock = stock;
		
		stockBinder.readBean(stock);
	}
	
	public Stock getStock() {
		return this.stock;
	}
	
    private Component createTitle() {
        return new H3("Stock form");
    }
    
    private List<Product> getProducts() {
    	try {
    		return productService.findAll();
		
	    } catch (Exception ex) {
	        logger.error(ex.getMessage());
	        
	        throw ex;
	    }
    }
    
    private Component createFormLayout() {
		formLayout = new FormLayout();
		
		// define form fields			
		lot = new TextField();
		lot.setId("lot");			
		lot.setLabel("Lot");		
		
		serialNumber = new TextField();
		serialNumber.setId("serial-number");	
		serialNumber.setLabel("Serial number");
				
		expirationDate = new DatePicker();
		expirationDate.setClearButtonVisible(true);
		expirationDate.setId("expiration-date");	
		expirationDate.setLabel("Expiration date");
		
		quantity = new NumberField();
		quantity.setId("quantity");
		quantity.setLabel("Quantity");
		
		status = new ComboBox<Status>();		
		status.setId("status");
		status.setLabel("Status");
		status.setItems(Status.values());
		
		product = new ComboBox<Product>();		
		product.setId("product");
		product.setItemLabelGenerator(Product::getName);
		product.setLabel("Product");
		product.setItems(getProducts());			
		
		formLayout.add(product, lot, serialNumber, quantity, status, expirationDate);
			
		return formLayout;
	}
	
	private Component createToolbarLayout() {		
		Button saveButton = new Button("Confirm", event -> {
			// retreive the product updated from form
			this.dialogResult = DIALOG_RESULT.SAVE;			
			
			try {
				stockBinder.writeBean(stock);
				
		        close();
		    } catch (ValidationException ex) {
		        ex.printStackTrace();
		        
		        logger.error(ex.getMessage());
		    }					   
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickShortcut(Key.ENTER).listenOn(this);
		saveButton.getElement().getStyle().set("margin-left", "auto");
		
		Button cancelButton = new Button("Cancel", event -> {
			this.dialogResult = DIALOG_RESULT.CANEL;
			
		    close();
		});
		
		HorizontalLayout formToolBar = new HorizontalLayout(saveButton, cancelButton);
		formToolBar.setWidthFull();
		formToolBar.getElement().getStyle().set("padding-top", "30px");
		
		return formToolBar;
	}
}
