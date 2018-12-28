package com.cldbiz.userportal.repository.invoice;

import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.repository.AbstractRepository;

public interface InvoiceRepository extends AbstractRepository<Invoice, Long>, InvoiceRepositoryExt {

}
