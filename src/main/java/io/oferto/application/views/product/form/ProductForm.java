package io.oferto.application.views.product.form;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import io.oferto.application.backend.model.Product;
import io.oferto.application.backend.model.Product.Family;
import io.oferto.application.backend.model.Warehouse;
import io.oferto.application.backend.service.WarehouseService;

public class ProductForm extends Dialog {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static enum DIALOG_RESULT {SAVE, CANEL};
		
	private DIALOG_RESULT dialogResult;
	
	private WarehouseService warehouseService;
	
	private Product product;	
	private Binder<Product> productBinder = new BeanValidationBinder<Product>(Product.class);
	
	private FormLayout formLayout;
	
	private ComboBox<Warehouse> warehouse;
	private TextField name;
	private TextField description;
	private ComboBox<Family> family;
	private NumberField price;
	private Checkbox active;
		
	public ProductForm(WarehouseService warehouseService) {
		super();
					
		this.warehouseService = warehouseService;
		
		// create dialog layout
		add(createTitle(), createFormLayout(), new Hr(), createToolbarLayout());
		
		productBinder.bindInstanceFields(this);
	}
	
	public DIALOG_RESULT getDialogResult() {
		return this.dialogResult;
	}
	
	public void setProduct(Product product) {
		this.product = product;
		
		productBinder.readBean(product);
	}
	
	public Product getProduct() {
		return this.product;
	}
	
    private List<Warehouse> getWarehouses() {
    	try {
    		return warehouseService.findAll();
		
	    } catch (Exception ex) {
	        logger.error(ex.getMessage());
	        
	        throw ex;
	    }
    }
	  
    private Component createTitle() {
        return new H3("Product form");
    }
    
	private Component createFormLayout() {
		formLayout = new FormLayout();
		formLayout.setWidthFull();
		
		formLayout.setResponsiveSteps(
	        new ResponsiveStep("1px", 1),
	        new ResponsiveStep("600px", 2),
	        new ResponsiveStep("700px", 3));
		
		// define form fields
		HorizontalLayout row01 = new HorizontalLayout();
		row01.setPadding(false);
		row01.setMargin(false);
		
		warehouse = new ComboBox<Warehouse>();		
		warehouse.setId("warehouse");
		warehouse.setItemLabelGenerator(Warehouse::getName);
		warehouse.setLabel("Warehouse");
		warehouse.setItems(getWarehouses());
		warehouse.setAutofocus(true);
		warehouse.setWidth("300px");		
		productBinder.forField(warehouse).asRequired("Warehouse is required");
		
		active = new Checkbox();
		active.setId("active");
		active.setLabel("Active");
		active.getElement().getStyle().set("margin-left", "auto");
		
		row01.add(warehouse, active);
		row01.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		formLayout.setColspan(row01, 2);		
		
		HorizontalLayout row02 = new HorizontalLayout();
		row02.setPadding(false);
		row02.setMargin(false);
		
		name = new TextField();
		name.setId("name");			
		name.setLabel("Name");
		name.setWidth("300px");		
		productBinder.forField(name).withNullRepresentation("").asRequired("Name is required");
		
		row02.add(name);
				
		description = new TextField();
		description.setId("description");	
		description.setLabel("Description");
		formLayout.setColspan(description, 2);
				
		HorizontalLayout row04 = new HorizontalLayout();
		row04.setPadding(false);
		row04.setMargin(false);
		
		family = new ComboBox<Family>();		
		family.setId("family");
		family.setLabel("Family");
		family.setItems(Family.values());
		productBinder.forField(family).asRequired("Family is required");
		
		price = new NumberField();
		price.setId("price");
		price.setLabel("Price");
		price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
		price.setPrefixComponent(new Icon(VaadinIcon.EURO));
		productBinder.forField(price).asRequired("Price is required");
		
		row04.add(family, price);
		
		formLayout.add(row01, row02, description, row04);
			
		return formLayout;
	}
	
	private Component createToolbarLayout() {		
		Button saveButton = new Button("Confirm", event -> {
			// retreive the product updated from form
			this.dialogResult = DIALOG_RESULT.SAVE;			
						
			if (productBinder.writeBeanIfValid(product))
				close();	
								   
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
