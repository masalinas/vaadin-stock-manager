package com.example.application.views.product.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.example.application.backend.model.Product;

public class ProductForm extends Dialog {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private FormLayout formLayout;
	private Product product;
	
	public ProductForm() {
		super();
		
		// create main layout
		add(createFormLayout(), new Hr(), createToolbarLayout());
	}
	
	public Product getProduct() {
		return this.product;
	}
	
	private Component createFormLayout() {
		formLayout = new FormLayout();		
		
		// define form fields
		TextField nameField = new TextField();
		nameField.setId("name");		
		nameField.setAutofocus(true);	
				
		formLayout.addFormItem(nameField, "Name");
		
		return formLayout;
	}
	
	private Component createToolbarLayout() {		
		Button saveButton = new Button("Confirm", event -> {
			// retreive the product updated from form
			this.product = new Product();
			
		    close();
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickShortcut(Key.ENTER).listenOn(this);
		saveButton.getElement().getStyle().set("margin-left", "auto");
		
		Button cancelButton = new Button("Cancel", event -> {			
		    close();
		});
		
		HorizontalLayout formToolBar = new HorizontalLayout(saveButton, cancelButton);
		formToolBar.setWidthFull();
		formToolBar.getElement().getStyle().set("padding-top", "30px");
		
		return formToolBar;
	}
}
