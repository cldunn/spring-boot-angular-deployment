package com.cldbiz.userportal.repository.lineItem;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.AbstractRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class LineItemRepositoryImpl extends AbstractRepositoryImpl<LineItem, LineItemDto, Long> implements LineItemRepositoryExt {

	private static final Logger LOGGER = LoggerFactory.getLogger(LineItemRepositoryImpl.class);
	
	@Override
	public Boolean existsByDto(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		
		DynBooleanBuilder<QLineItem, LineItemDto> builder = searchByCriteria(lineItemDto, predicates);
		
		return jpaQueryFactory.selectFrom(lineItem).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		
		DynBooleanBuilder<QLineItem, LineItemDto> builder = searchByCriteria(lineItemDto, predicates);
		
		return jpaQueryFactory.selectFrom(lineItem).where(builder.asPredicate()).fetchCount();
	}

	@Override
	public List<LineItem> findByIds(List<Long> lineItemIds) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.where(lineItem.id.in(lineItemIds))
				.fetch();
	}

	@Override
	public List<LineItem> findAll() {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.fetch();
	}

	@Override
	public List<LineItem> findByDto(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = findByCriteria(lineItemDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.where(builder.asPredicate())
				.fetch();

	}

	@Override
	public List<LineItem> findPageByDto(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = findByCriteria(lineItemDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(lineItemDto))
				.offset(lineItemDto.getStart().intValue())
				.limit(lineItemDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public List<LineItem> searchByDto(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = searchByCriteria(lineItemDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<LineItem> searchPageByDto(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = searchByCriteria(lineItemDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(lineItemDto))
				.offset(lineItemDto.getStart().intValue())
				.limit(lineItemDto.getLimit().intValue())
				.fetch();
	}


	protected DynBooleanBuilder<QLineItem, LineItemDto> findByCriteria(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		
		DynBooleanBuilder<QLineItem, LineItemDto> builder = new DynBooleanBuilder<QLineItem, LineItemDto>();
		builder = builder.findPredicate(lineItem, lineItemDto, predicates);

		if (lineItemDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(lineItem.product, lineItemDto.getProductDto(), predicates).asPredicate();
			builder.and(byProductPredicate);
		}

		return builder;
	}
	
	protected DynBooleanBuilder<QLineItem, LineItemDto> searchByCriteria(LineItemDto lineItemDto, Predicate... predicates) {
		QLineItem lineItem = QLineItem.lineItem;
		
		DynBooleanBuilder<QLineItem, LineItemDto> builder = new DynBooleanBuilder<QLineItem, LineItemDto>();
		builder = builder.searchPredicate(lineItem, lineItemDto, predicates);

		if (lineItemDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(lineItem.product, lineItemDto.getProductDto(), predicates).asPredicate();
			builder.and(byProductPredicate);
		}

		return builder;
	}

	@Override
	public OrderSpecifier[] sortBy(LineItemDto lineItemDto) {
		PathBuilder pb = new PathBuilder<QLineItem>(QLineItem.class, "lineItem");
		return sortOrderOf(pb, lineItemDto);
	}

}
