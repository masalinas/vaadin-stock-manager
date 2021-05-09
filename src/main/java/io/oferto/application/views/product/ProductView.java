package io.oferto.application.views.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.vaadin.flow.router.RouteAlias;

import io.oferto.application.backend.model.Product;
import io.oferto.application.backend.service.ProductService;
import io.oferto.application.views.main.MainView;
import io.oferto.application.views.product.form.ProductForm;

@RouteAlias(value = "", layout = MainView.class)
@Route(value = "products", layout = MainView.class)
@PageTitle("Product Manager | Product Master")
@CssImport("./views/product/product-view.css")
public class ProductView extends VerticalLayout {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ProductService productService;
	
	private List<Product> products;
	private ListDataProvider<Product> productProvider;
	
	private HorizontalLayout toolBarLayout;
	private Button refreshProducts;
	private Button addProduct;
	private Grid<Product> gridProduct = new Grid<>(Product.class); 
	
	public ProductView(ProductService productService) {
		addClassName("product-view");
		
		this.setSizeFull();
		this.setPadding(true);
		
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

	private void createViewLayout() {
		toolBarLayout = new HorizontalLayout();
		toolBarLayout.setPadding(true);
		toolBarLayout.setWidthFull();
		
		addProduct = new Button("Add Product", clickEvent -> {
			 
		});
		addProduct.getElement().getStyle().set("margin-right", "auto");
		addProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		refreshProducts = new Button("Refresh Products", clickEvent -> {
			// load data from service
			 loadData();
			 
			// fill grid with data
			 loadGrid();
		});		
		
		toolBarLayout.add(addProduct, refreshProducts);
		
		gridProduct.setSizeFull();
		
		add(toolBarLayout);	 		
		add(gridProduct);	
	}
	
	private void configureGrid() {
		loadGrid();
		
		gridProduct.setColumns("name", "description", "family", "price");
		gridProduct.getColumnByKey("name").setFooter("Total: " + this.products.size() + " products");		
		gridProduct.addColumn(
                new ComponentRenderer<>(
                        product -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setReadOnly(true);
                            checkbox.setValue( product.isActive());
                            
                            return checkbox;
                        }
                )
        ).setHeader("Active").setKey("active");
		
		gridProduct.addComponentColumn(item -> updateProductButton(gridProduct, item)).setHeader("");
		gridProduct.addComponentColumn(item -> removeRemoveButton(gridProduct, item)).setHeader("");
		gridProduct.addThemeVariants(GridVariant.LUMO_NO_BORDER, 
									 GridVariant.LUMO_NO_ROW_BORDERS, 
									 GridVariant.LUMO_ROW_STRIPES);
	}
	
	private void loadGrid() {
		productProvider =  DataProvider.ofCollection(this.products);
		productProvider.setSortOrder(Product::getName, SortDirection.ASCENDING);
		
		gridProduct.setDataProvider(productProvider);
	}
	
	private Button updateProductButton(Grid<Product> grid, Product item) {
	    Button button = new Button("Update", clickEvent -> {
	    	// define form dialog
	    	ProductForm productForm = new ProductForm();
	    	productForm.setWidth("700px");
	    	productForm.setCloseOnEsc(true);			
	    	productForm.setCloseOnOutsideClick(false);
	    	
	    	// bind form dialog with product entity
	    	productForm.setProduct(item);
	    	
	    	// define form dialog view callback
	    	productForm.addOpenedChangeListener(event -> {
	    	     if(!event.isOpened()) {	    	    	 
	    	    	 if (productForm.getDialogResult() == ProductForm.DIALOG_RESULT.SAVE)
	    	    		 Notification.show("Product Saved");
	    	     }
	    	});
	    		
	    	// open form dialog view
	    	productForm.open();
	    });
	    
	    return button;
	}
	
	private Button removeRemoveButton(Grid<Product> grid, Product item) {
	    @SuppressWarnings("unchecked")
	    Button button = new Button("Remove", clickEvent -> {
	        ListDataProvider<Product> dataProvider = (ListDataProvider<Product>) grid.getDataProvider();
	        
	        dataProvider.getItems().remove(item);
	        dataProvider.refreshAll();
	    });
	    button.addThemeVariants(ButtonVariant.LUMO_ERROR);
	    
	    return button;
	}
}
