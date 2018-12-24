package com.cldbiz.userportal.repository.lineItem;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class LineItemRepositoryImpl extends BaseRepositoryImpl<LineItem, LineItemDto, Long> implements LineItemRepositoryExt {

	private static final Logger LOGGER = LoggerFactory.getLogger(LineItemRepositoryImpl.class);
	
	/* TODO: Remove from interface, findByDto(new *Dto) should be the same */
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
	public List<LineItem> findAllById(List<Long> lineItemIds) {
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
	public List<LineItem> findByDto(LineItemDto lineItemDto) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = new DynBooleanBuilder<QLineItem, LineItemDto>();
		builder = builder.findPredicate(lineItem, lineItemDto);

		if (lineItemDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(lineItem.product, lineItemDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.where(builder.asPredicate())
				.fetch();

	}

	@Override
	public List<LineItem> findPageByDto(LineItemDto lineItemDto) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = new DynBooleanBuilder<QLineItem, LineItemDto>();
		builder = builder.findPredicate(lineItem, lineItemDto);

		if (lineItemDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(lineItem.product, lineItemDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}
		
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
	public Long countSearchByDto(LineItemDto lineItemDto) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = new DynBooleanBuilder<QLineItem, LineItemDto>();
		builder = builder.searchPredicate(lineItem, lineItemDto);

		if (lineItemDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(lineItem.product, lineItemDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}

		return jpaQueryFactory.selectFrom(lineItem)
				.where(builder.asPredicate())
				.fetchCount();

	}

	@Override
	public List<LineItem> searchByDto(LineItemDto lineItemDto) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = new DynBooleanBuilder<QLineItem, LineItemDto>();
		builder = builder.searchPredicate(lineItem, lineItemDto);

		if (lineItemDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(lineItem.product, lineItemDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(lineItem)
				.innerJoin(lineItem.product, product).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<LineItem> searchPageByDto(LineItemDto lineItemDto) {
		QLineItem lineItem = QLineItem.lineItem;
		QProduct product = QProduct.product;

		DynBooleanBuilder<QLineItem, LineItemDto> builder = new DynBooleanBuilder<QLineItem, LineItemDto>();
		builder = builder.searchPredicate(lineItem, lineItemDto);

		if (lineItemDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(lineItem.product, lineItemDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}

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
	public OrderSpecifier[] sortBy(LineItemDto lineItemDto) {
		PathBuilder pb = new PathBuilder<QLineItem>(QLineItem.class, "lineItem");
		return sortOrderOf(pb, lineItemDto);
	}

}
