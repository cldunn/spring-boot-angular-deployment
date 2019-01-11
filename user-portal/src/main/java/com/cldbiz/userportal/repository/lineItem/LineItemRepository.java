package com.cldbiz.userportal.repository.lineItem;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.repository.BaseRepository;

@Repository
public interface LineItemRepository extends BaseRepository<LineItem, Long>, LineItemRepositoryExt {

}
