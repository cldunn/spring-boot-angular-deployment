package com.cldbiz.userportal.repository.invoice;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.repository.BaseRepository;

@Repository
public interface InvoiceRepository extends BaseRepository<Invoice, Long>, InvoiceRepositoryExt {

}
