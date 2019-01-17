package com.cldbiz.userportal.unit.repository.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.cldbiz.userportal.domain.Invoice;

public class InvoiceData {
	
	public static  List<Invoice> getSomeInvoices() {
		List<Invoice> invoices = new ArrayList<Invoice>();
		
		Invoice anotherInvoice = new Invoice();
		anotherInvoice.setInvoiceNbr("27");
		anotherInvoice.setDueDate(LocalDate.now());
		anotherInvoice.setStatus("PENDING");
		
		Invoice extraInvoice = new Invoice();
		extraInvoice.setInvoiceNbr("28");
		extraInvoice.setDueDate(LocalDate.now());
		extraInvoice.setStatus("PENDING");
		
		invoices.add(anotherInvoice);
		invoices.add(extraInvoice);
		
		return invoices;
	}

	public static List<Invoice> getMoreInvoices() {
		List<Invoice> invoices = new ArrayList<Invoice>();
		
		Invoice anotherInvoice = new Invoice();
		anotherInvoice.setInvoiceNbr("420");
		anotherInvoice.setDueDate(LocalDate.now());
		anotherInvoice.setStatus("PAST DUE");
		
		Invoice extraInvoice = new Invoice();
		extraInvoice.setInvoiceNbr("421");
		extraInvoice.setDueDate(LocalDate.now());
		extraInvoice.setStatus("COMPLETED");
		
		invoices.add(anotherInvoice);
		invoices.add(extraInvoice);
		
		return invoices;
	}


}
