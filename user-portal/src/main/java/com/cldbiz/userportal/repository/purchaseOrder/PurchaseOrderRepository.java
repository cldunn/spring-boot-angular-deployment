package com.cldbiz.userportal.repository.purchaseOrder;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.repository.BaseRepository;

@Repository
public interface PurchaseOrderRepository extends BaseRepository<PurchaseOrder, Long>, PurchaseOrderRepositoryExt {

}
