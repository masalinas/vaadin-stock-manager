package io.oferto.application.views.product.form;

import java.util.Arrays;
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
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import io.oferto.application.backend.model.Product;
import io.oferto.application.backend.model.Product.Family;

public class ProductForm extends Dialog {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static enum DIALOG_RESULT {SAVE, CANEL};
	
	private DIALOG_RESULT dialogResult;
	
	private Product product;	
	private Binder<Product> productBinder = new Binder<Product>(Product.class);
	
	private FormLayout formLayout;
	
	private TextField name;
	private TextField description;
	private ComboBox<Family> family;
	private NumberField price;
	private Checkbox active;
	
	/*public class DoubleToIntegerConverter implements Converter<Double, Integer> {		
		private static final long serialVersionUID = 1L;

		@Override
		    public Result<Integer> convertToModel(Double aDouble, ValueContext valueContext) {				
		        return Result.ok(aDouble.intValue());				
		    }

		    @Override
		    public Double convertToPresentation(Integer integer, ValueContext valueContext) {
		    	return integer.doubleValue();
		    }
	}*/
	
	public ProductForm() {
		super();
							
		// create dialog layout
		add(createTitle(), createFormLayout(), new Hr(), createToolbarLayout());
		
		// bind dialog form
		/*productBinder.forField(price)
			.asRequired()
			.withConverter(new DoubleToIntegerConverter()).bind("price");*/
				
		productBinder.bindInstanceFields(this);
	}
	
	public DIALOG_RESULT getDialogResult() {
		return this.dialogResult;
	}
	
	public void setProduct(Product product) {
		this.product = product;
		
		productBinder.setBean(product);
	}
	
	public Product getProduct() {
		return this.product;
	}
	
    private Component createTitle() {
        return new H3("Product form");
    }
    
	private Component createFormLayout() {
		formLayout = new FormLayout();
		
		// define form fields		
		name = new TextField();
		name.setId("name");			
		name.setLabel("Name");
		name.setAutofocus(true);
		
		description = new TextField();
		description.setId("description");	
		description.setLabel("Description");
		description.getElement().getStyle().set("margin-left", "auto");
				
		family = new ComboBox<Family>();		
		family.setId("family");
		family.setLabel("Family");
		family.setItems(Family.values());
		
		price = new NumberField();
		price.setId("price");
		price.setLabel("Price");
		price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
		price.setPrefixComponent(new Icon(VaadinIcon.EURO));
		
		active = new Checkbox();
		active.setId("active");
		active.setLabel("Active");
		
		// define Layout
		/*HorizontalLayout rowLayout01 = new HorizontalLayout();
		rowLayout01.add(nameField, activeField);
		
		HorizontalLayout rowLayout02 = new HorizontalLayout();
		rowLayout02.add(descriptionField);
		
		HorizontalLayout rowLayout03 = new HorizontalLayout();
		rowLayout01.add(familyField, priceField);*/
		
		formLayout.add(name, description, family, price, active);
			
		return formLayout;
	}
	
	private Component createToolbarLayout() {		
		Button saveButton = new Button("Confirm", event -> {
			// retreive the product updated from form
			this.dialogResult = DIALOG_RESULT.SAVE;
			this.product = new Product();
			
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
