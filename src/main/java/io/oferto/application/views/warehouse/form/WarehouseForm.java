package io.oferto.application.views.warehouse.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import io.oferto.application.backend.model.Warehouse;

public class WarehouseForm extends Dialog {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static enum DIALOG_RESULT {SAVE, CANEL};
		
	private DIALOG_RESULT dialogResult;
	
	private Warehouse warehouse;	
	private Binder<Warehouse> warehouseBinder = new BeanValidationBinder<Warehouse>(Warehouse.class);
	
	private FormLayout formLayout;
	
	private TextField name;
	private TextField address;
	private NumberField longitude;
	private NumberField latitude;
	
	public WarehouseForm() {
		super();	
				
		// create dialog layout
		add(createTitle(), createFormLayout(), new Hr(), createToolbarLayout());
		
		warehouseBinder.bindInstanceFields(this);
	}
	
	public DIALOG_RESULT getDialogResult() {
		return this.dialogResult;
	}
	
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
		
		warehouseBinder.readBean(warehouse);
	}
	
	public Warehouse getWarehouse() {
		return this.warehouse;
	}
	
    private Component createTitle() {
        return new H3("Warehouse form");
    }
    
    private Component createFormLayout() {
		formLayout = new FormLayout();
		formLayout.setWidthFull();
	
		formLayout.setResponsiveSteps(
	        new ResponsiveStep("1px", 1),
	        new ResponsiveStep("600px", 2),
	        new ResponsiveStep("700px", 3));
		
		// define form fields	
		name = new TextField();
		name.setId("name");
		name.setLabel("Name");
		name.setWidth("200px");				
		warehouseBinder.forField(name).withNullRepresentation("").asRequired("Name is required");
		
		address = new TextField();
		address.setId("address");
		address.setLabel("Address");
		formLayout.setColspan(address, 2);
		
		HorizontalLayout row = new HorizontalLayout();
		row.setPadding(false);
		row.setMargin(false);
		
		longitude = new NumberField();
		longitude.setId("longitude");
		longitude.setLabel("Longitude");
		longitude.setWidth("120px");		
		longitude.setErrorMessage("Longitude is required");
		warehouseBinder.forField(longitude).asRequired("Longitude is required");
		
		latitude = new NumberField();
		latitude.setId("latitude");
		latitude.setLabel("Latitude");
		latitude.setWidth("120px");		
		warehouseBinder.forField(latitude).asRequired("Latitude is required");		
		
		row.add(longitude, latitude);
		
		formLayout.add(name, address, row);
			
		return formLayout;
	}
	
	private Component createToolbarLayout() {		
		Button saveButton = new Button("Confirm", event -> {		        
			this.dialogResult = DIALOG_RESULT.SAVE;			
			
			if (warehouseBinder.writeBeanIfValid(warehouse))
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
