package io.oferto.application.views.stock.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;

import io.oferto.application.backend.model.Stock;

public class StockForm extends Dialog {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static enum DIALOG_RESULT {SAVE, CANEL};
	
	private DIALOG_RESULT dialogResult;
	
	private Stock stock;
	
	private FormLayout formLayout;
	
	public StockForm() {
		super();				
	}
	
	public DIALOG_RESULT getDialogResult() {
		return this.dialogResult;
	}
	
	public void setStock(Stock stock) {
		this.stock = stock;
		
		//productBinder.readBean(product);
	}
	
	public Stock getStock() {
		return this.stock;
	}
}
