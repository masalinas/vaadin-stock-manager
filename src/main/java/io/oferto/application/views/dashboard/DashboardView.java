package io.oferto.application.views.dashboard;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.addon.leaflet4vaadin.LeafletMap;
import com.vaadin.addon.leaflet4vaadin.controls.LeafletControl.ControlPosition;
import com.vaadin.addon.leaflet4vaadin.controls.ScaleControl;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.DefaultMapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.MapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.ui.marker.Marker;
import com.vaadin.addon.leaflet4vaadin.types.LatLng;
import com.vaadin.addon.leaflet4vaadin.types.LatLngBounds;

import io.oferto.application.backend.model.Warehouse;
import io.oferto.application.backend.service.StockService;
import io.oferto.application.backend.service.WarehouseService;
import io.oferto.application.views.main.MainView;

//@Route(value = "", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Stock Manager | Dashboard")
@CssImport("./views/dashboard/dashboard-view.css")
public class DashboardView extends VerticalLayout {
	private WarehouseService warehouseService;
	private StockService stockService;
	
	private List<Warehouse> warehouses;
	private LeafletMap warehouseMap;
	
	public DashboardView(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
		
		this.setSizeFull();
		this.setPadding(false);
				
		// set warehouse map
		MapOptions options = new DefaultMapOptions();
				
		//options.setCenter(getCenterMap());
		options.setZoom(5);
		warehouseMap = new LeafletMap(options );
		warehouseMap.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");		
		
		ScaleControl scaleControl = new ScaleControl();
		scaleControl.setMaxWidth(500);
		scaleControl.setPosition(ControlPosition.bottomleft);
		scaleControl.addTo(warehouseMap);
		
		add(warehouseMap);
		
		// get all warehouses 
		getWarehouses();
		
		// fill warehouse markers
		locateWarehouses();
	}
	
	private void getWarehouses() {
		try {
			 warehouses = warehouseService.findAll();
		} catch (Exception ex) {
			Notification.show(ex.getLocalizedMessage());
		}	
	}
	
	private void locateWarehouses() {	
		List<LatLng> latLngs = new ArrayList<LatLng>();
		
		try {			 
			 for (Warehouse warehouse : warehouses) {				 
				 Marker marker = new Marker(new LatLng(warehouse.getLatitude(), warehouse.getLongitude()));
				 
				 marker.bindTooltip(warehouse.getName());
				 marker.setAttribution(warehouse.getId().toString());
				 marker.onClick((event) -> {					 
					 //Notification.show(marker.getAttribution());
					 showWarehouseStock(marker.getAttribution());
				 });
				 
				 marker.addTo(warehouseMap);
				 latLngs.add(marker.getLatLng());
		     }	
			 
			 // fit maximun bound
			 warehouseMap.fitBounds(new LatLngBounds(latLngs));
		} catch (Exception ex) {
			Notification.show(ex.getLocalizedMessage());
		}		
	}	
	
	private void showWarehouseStock(String warehouseId) {		
		Map<String, List<String>> queryParameters = new HashMap<String, List<String>>()
		{
		    {
		        put("warehouseId",Arrays.asList(warehouseId));
		    }
		};
		
		UI.getCurrent().navigate("stock", new QueryParameters(queryParameters));
	}
}
