package io.oferto.application.views.warehouse.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import io.oferto.application.backend.model.Warehouse;

public class WarehouseForm extends Dialog {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static enum DIALOG_RESULT {SAVE, CANEL};
		
	private DIALOG_RESULT dialogResult;
	
	private Warehouse warehouse;	
	private Binder<Warehouse> warehouseBinder = new Binder<Warehouse>(Warehouse.class);
	
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
		
		// define form fields			
		name = new TextField();
		name.setId("name");			
		name.setLabel("Name");		
		
		address = new TextField();
		address.setId("address");	
		address.setLabel("Address");
						
		longitude = new NumberField();
		longitude.setId("longitude");
		longitude.setLabel("Longitude");

		latitude = new NumberField();
		latitude.setId("latitude");
		latitude.setLabel("Latitude");
		
		formLayout.add(name, address, longitude, latitude);
			
		return formLayout;
	}
	
	private Component createToolbarLayout() {		
		Button saveButton = new Button("Confirm", event -> {
			// retreive the product updated from form
			this.dialogResult = DIALOG_RESULT.SAVE;			
			
			try {
				warehouseBinder.writeBean(warehouse);
				
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
