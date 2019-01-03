package com.cldbiz.userportal.repository.invoice;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface InvoiceRepository extends AbstractRepository<Invoice, Long>, InvoiceRepositoryExt {

}
