package io.oferto.application.backend.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Product extends AbstractEntity implements Cloneable {
	public enum Family {
		PERISHABLE("Perishable"),
		ELECTRONICS("Electronic"),
	    FASHION("Fashion");

	    public final String label;

	    private Family(String label) {
	        this.label = label;
	    }
	    
	    @Override
		public String toString() {
	    	return label;
	    }
	}
    
    @NotNull
    @NotEmpty
	private String name;
    
	private String description;

	@Enumerated(EnumType.STRING)
	@NotNull	 
	private Family family;
		
	@NotNull
	private Double price;
	
	@NotNull
	private boolean active = true;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	private List<Stock> stocks = new LinkedList<>();
	  
	public Product() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
    public Family getFamily() {
        return family;
    }
    public void setFamily(Family family) {
        this.family = family;
    }
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public List<Stock> getStocks() {
		return stocks;
	}	
}
