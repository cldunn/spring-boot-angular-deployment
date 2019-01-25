package com.cldbiz.userportal.unit.repository.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.repository.purchaseOrder.PurchaseOrderRepository;

@Component
public class PurchaseOrderData {
	
	private static PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	public PurchaseOrderData(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}
	
	public static PurchaseOrder getAnotherExistingPurchaseOrder() {
		Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(1L);

		return purchaseOrder.orElse(null);
	}

	public static  PurchaseOrder getExtraExistingPurchaseOrder() {
		Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(2L);

		return purchaseOrder.orElse(null);
	}

	public static PurchaseOrder getAnotherPurchaseOrder() {
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
	
		anotherPurchaseOrder.setOrderIdentifier("EOM-92");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(true);
		anotherPurchaseOrder.setStatus("SHIPPED");
		
		return anotherPurchaseOrder;
	}

	public static  PurchaseOrder getExtraPurchaseOrder() {
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
		
		anotherPurchaseOrder.setOrderIdentifier("NET30-512");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(false);
		anotherPurchaseOrder.setStatus("SHIPPED");
		
		return anotherPurchaseOrder;
	}

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
