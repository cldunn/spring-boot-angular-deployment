package com.cldbiz.userportal.repository.lineItem;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface LineItemRepository extends AbstractRepository<LineItem, Long>, LineItemRepositoryExt {

}
