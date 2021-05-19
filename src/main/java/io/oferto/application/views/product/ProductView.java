package io.oferto.application.views.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import io.oferto.application.backend.model.Product;
import io.oferto.application.backend.service.ProductService;
import io.oferto.application.backend.service.WarehouseService;
import io.oferto.application.views.main.MainView;
import io.oferto.application.views.product.form.ProductForm;
import io.oferto.application.components.AuthenticatedButton;
import io.oferto.application.security.SecurityConfiguration;

@Route(value = "products", layout = MainView.class)
@PageTitle("Stock Manager | Product Master")
@CssImport("./views/product/product-view.css")
public class ProductView extends VerticalLayout {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int NOTIFICATION_DEFAULT_DURATION = 5000;
	
	private WarehouseService warehouseService;
	private ProductService productService;
	
	private List<Product> products;
	private ListDataProvider<Product> productProvider;
	
	private HorizontalLayout toolBarLayout;
	private Button refreshProducts;
	private AuthenticatedButton addProduct;
	private Grid<Product> gridProduct = new Grid<>(Product.class); 
	
	public ProductView(WarehouseService warehouseService, ProductService productService) {
		addClassName("product-view");
		
		this.setSizeFull();
		this.setPadding(true);
		
		this.warehouseService = warehouseService;
		this.productService = productService;
		
		// load data from service
		loadData();
		
		// fill grid with data
		configureGrid();
				
		// create view layput
		createViewLayout();
	}
	
	private void loadData() {
		try {
			this.products = productService.findAll();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			
			logger.debug(ex.getLocalizedMessage());
		}
	}

	private void loadGrid() {
		productProvider =  DataProvider.ofCollection(this.products);
		productProvider.setSortOrder(Product::getName, SortDirection.ASCENDING);
		
		gridProduct.setDataProvider(productProvider);
	}
	
	private void createViewLayout() {
		toolBarLayout = new HorizontalLayout();
		toolBarLayout.setPadding(true);
		toolBarLayout.setWidthFull();
		
		addProduct = new AuthenticatedButton("Add Product", clickEvent -> createProductButton(clickEvent));		
		addProduct.getElement().getStyle().set("margin-right", "auto");
		addProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		refreshProducts = new Button("Refresh Products", clickEvent -> refreshProducts(clickEvent));		
		
		toolBarLayout.add(addProduct, refreshProducts);
		
		gridProduct.setSizeFull();
		
		add(toolBarLayout);	 		
		add(gridProduct);	
	}
	
	private void configureGrid() {
		loadGrid();
		
		gridProduct.setSizeFull();
		gridProduct.setColumns("warehouse.name", "name", "description", "family", "price");
		gridProduct.getColumnByKey("warehouse.name").setFlexGrow(0).setWidth("200px").setHeader("Warehouse").setFooter("Total: " + this.products.size() + " products");
		gridProduct.getColumnByKey("name").setFlexGrow(0).setWidth("200px").setHeader("Name");
		gridProduct.getColumnByKey("description").setFlexGrow(1).setHeader("Description");
		gridProduct.getColumnByKey("family").setFlexGrow(0).setWidth("150px").setHeader("Family");
		gridProduct.getColumnByKey("price").setFlexGrow(0).setWidth("100px").setHeader("Price");
		gridProduct.addColumn(
                new ComponentRenderer<>(
                        product -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setReadOnly(true);
                            checkbox.setValue( product.isActive());
                            
                            return checkbox;
                        }
                )
        ).setHeader("Active").setKey("active").setFlexGrow(0).setWidth("80px").setHeader("Active");
		
		if (SecurityConfiguration.isAdmin()) {
			gridProduct.addComponentColumn(item -> updateProductButton(gridProduct, item)).setFlexGrow(0).setWidth("120px").setHeader("");
			gridProduct.addComponentColumn(item -> removeRemoveButton(gridProduct, item)).setFlexGrow(0).setWidth("120px").setHeader("");
		}
		
		gridProduct.addThemeVariants(GridVariant.LUMO_NO_BORDER, 
									 GridVariant.LUMO_NO_ROW_BORDERS, 
									 GridVariant.LUMO_ROW_STRIPES);
	}
		
	private void refreshProducts(ClickEvent e) {
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
	
	private void createProductButton(ClickEvent e) {
		// define form dialog
    	ProductForm productForm = new ProductForm(this.warehouseService);
    	productForm.setWidth("700px");
    	productForm.setCloseOnEsc(true);			
    	productForm.setCloseOnOutsideClick(false);
    	
    	// bind form dialog with product entity
    	productForm.setProduct(new Product());
    	
    	// define form dialog view callback
    	productForm.addOpenedChangeListener(event -> {
    	     if(!event.isOpened()) {	    	    	 
    	    	 if (productForm.getDialogResult() == ProductForm.DIALOG_RESULT.SAVE)	    	    	 
    	    	 	try {
    	    	 		// save product entity
    	    	 		productService.save(productForm.getProduct());
    	    	 		
    	    	 		// refresh grid
    	    	 		refreshProducts(null);
    	    	 		
	    	    	 	Notification.show("Product Saved", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    	    } catch (Exception ex) {
    	    	    	logger.error(ex.getMessage());
    	    	    	
    	    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    	    }	    	    	
    	     }
    	});
    		
    	// open form dialog view
    	productForm.open();
	}
	
	private Button updateProductButton(Grid<Product> grid, Product product) {
		AuthenticatedButton button = new AuthenticatedButton("Update", clickEvent -> {
	    	// define form dialog
	    	ProductForm productForm = new ProductForm(this.warehouseService);
	    	productForm.setWidth("700px");
	    	productForm.setCloseOnEsc(true);			
	    	productForm.setCloseOnOutsideClick(false);
	    	
	    	// bind form dialog with product entity
	    	productForm.setProduct(product);
	    	
	    	// define form dialog view callback
	    	productForm.addOpenedChangeListener(event -> {
	    	     if(!event.isOpened()) {	    	    	 
	    	    	 if (productForm.getDialogResult() == ProductForm.DIALOG_RESULT.SAVE)	    	    	 
	    	    	 	try {
	    	    	 		// save product entity
	    	    	 		productService.save(productForm.getProduct());
	    	    	 		
	    	    	 		// refresh grid
	    	    	 		refreshProducts(null);
	    	    	 		
		    	    	 	Notification.show("Product Updated", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
	    	    	    } catch (Exception ex) {
	    	    	    	logger.error(ex.getMessage());
	    	    	    	
	    	    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
	    	    	    }	    	    	
	    	     }
	    	});
	    		
	    	// open form dialog view
	    	productForm.open();
	    });
	    
	    return button;
	}
	
	private Button removeRemoveButton(Grid<Product> grid, Product product) {
	    Button button = new Button("Remove", clickEvent -> {	        
	        try {	        
    	 		// save product entity
    	 		productService.delete(product);
    	 		
    	 		// refresh grid
    	 		refreshProducts(null);
    	        
	    	 	Notification.show("Product Deleted", NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    } catch (Exception ex) {
    	    	logger.error(ex.getMessage());
    	    	
    	    	Notification.show(ex.getMessage(), NOTIFICATION_DEFAULT_DURATION, Notification.Position.TOP_END);
    	    }	        
	    });
	    
	    button.addThemeVariants(ButtonVariant.LUMO_ERROR);
	    
	    return button;
	}
}
