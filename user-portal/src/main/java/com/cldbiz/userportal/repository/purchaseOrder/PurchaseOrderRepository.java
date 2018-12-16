package com.cldbiz.userportal.repository.purchaseOrder;

import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.repository.AbstractRepository;

public interface PurchaseOrderRepository extends AbstractRepository<PurchaseOrder, Long>, PurchaseOrderRepositoryExt {

}
