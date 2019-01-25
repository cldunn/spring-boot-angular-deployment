package com.cldbiz.userportal.unit.repository.data;

import java.util.ArrayList;
import java.util.List;

import com.cldbiz.userportal.domain.LineItem;

public class LineItemData {
	
	public static LineItem getAnotherLineItem() {
		LineItem lineItem = new LineItem();
		
		lineItem.setQuantity(18L);
		
		return lineItem;
	}
	
	public static LineItem getExtraLineItem() {
		LineItem lineItem = new LineItem();
		
		lineItem.setQuantity(23L);
		
		return lineItem;

	}

	public static List<LineItem> getSomeLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		
		LineItem anotherLineItem = new LineItem();
		anotherLineItem.setQuantity(15L);
		
		LineItem extraLineItem = new LineItem();
		extraLineItem.setQuantity(16L);
		
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		return lineItems;
	}

	public static List<LineItem> getMoreLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		
		LineItem anotherLineItem = new LineItem();
		anotherLineItem.setQuantity(17L);
		
		LineItem extraLineItem = new LineItem();
		extraLineItem.setQuantity(18L);
		
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		return lineItems;
	}
}
