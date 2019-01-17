package com.cldbiz.userportal.unit.repository.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cldbiz.userportal.domain.PurchaseOrder;

public class PurchaseOrderData {
	
	public static List<PurchaseOrder> getSomePurchaseOrders() {
		List<PurchaseOrder> puchaseOrders = new ArrayList<PurchaseOrder>();
		
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
		anotherPurchaseOrder.setOrderIdentifier("EOM-27");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(true);
		anotherPurchaseOrder.setStatus("RECIEVED");
		
		PurchaseOrder extraPurchaseOrder = new PurchaseOrder();
		extraPurchaseOrder.setOrderIdentifier("PIA-28");
		extraPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		extraPurchaseOrder.setInvoiced(false);
		extraPurchaseOrder.setStatus("SHIPPED");
		
		puchaseOrders.add(anotherPurchaseOrder);
		puchaseOrders.add(extraPurchaseOrder);
		
		return puchaseOrders;
	}

	public static List<PurchaseOrder> getMorePurchaseOrders() {
		List<PurchaseOrder> puchaseOrders = new ArrayList<PurchaseOrder>();
		
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
		anotherPurchaseOrder.setOrderIdentifier("NET30-270");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(true);
		anotherPurchaseOrder.setStatus("RECIEVED");
		
		PurchaseOrder extraPurchaseOrder = new PurchaseOrder();
		extraPurchaseOrder.setOrderIdentifier("NET30-228");
		extraPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		extraPurchaseOrder.setInvoiced(false);
		extraPurchaseOrder.setStatus("SHIPPED");
		
		puchaseOrders.add(anotherPurchaseOrder);
		puchaseOrders.add(extraPurchaseOrder);
		
		return puchaseOrders;
	}
}
