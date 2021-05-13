package io.oferto.application.backend.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Warehouse extends AbstractEntity {
	@NotNull
	private String name;
	
	private String address;
	
	@NotNull
	private Double longitude;
	
	@NotNull
	private Double latitude;
	
	@OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
	private List<Stock> stocks = new LinkedList<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public List<Stock> getStocks() {
		return stocks;
	}	
}
