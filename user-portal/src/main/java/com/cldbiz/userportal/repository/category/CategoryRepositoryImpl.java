package com.cldbiz.userportal.repository.category;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.Category;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCategory;
import com.cldbiz.userportal.domain.QContact;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CategoryDto;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.AbstractRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class CategoryRepositoryImpl extends AbstractRepositoryImpl<Category, CategoryDto, Long> implements CategoryRepositoryExt {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRepositoryImpl.class);
	
	@Override
	public Boolean existsByDto(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = searchByCriteria(categoryDto, predicates);
		
		return jpaQueryFactory.selectFrom(category).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = searchByCriteria(categoryDto, predicates);
		
		return jpaQueryFactory.selectFrom(category).where(builder.asPredicate()).fetchCount();
	}
	
	@Override
	public List<Category> findByIds(List<Long> categoryIds) {
		QCategory category = QCategory.category;
		
		return jpaQueryFactory.selectFrom(category)
				.where(category.id.in(categoryIds))
				.fetch();
	}
	
	@Override
	public List<Category> findAll() {
		QCategory category = QCategory.category;
		
		return jpaQueryFactory.selectFrom(category).fetch();
	}
	
	@Override
	public List<Category> findByDto(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = findByCriteria(categoryDto, predicates);
		
		/*
		DynBooleanBuilder<QCategory, CategoryDto> builder = new DynBooleanBuilder<QCategory, CategoryDto>();
		builder = builder.findPredicate(category, categoryDto);

		if (categoryDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(category.products.any(), categoryDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}
		*/
		
		return jpaQueryFactory.selectFrom(category)
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Category> findPageByDto(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = findByCriteria(categoryDto, predicates);
		
		/*
		DynBooleanBuilder<QCategory, CategoryDto> builder = new DynBooleanBuilder<QCategory, CategoryDto>();
		builder = builder.findPredicate(category, categoryDto);

		if (categoryDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(category.products.any(), categoryDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}
		*/
		return jpaQueryFactory.selectFrom(category)
				.where(builder.asPredicate())
				.orderBy(sortBy(categoryDto))
				.offset(categoryDto.getStart().intValue())
				.limit(categoryDto.getLimit().intValue())
				.fetch();

	}

	@Override
	public List<Category> searchByDto(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = searchByCriteria(categoryDto, predicates);
		
		/*
		DynBooleanBuilder<QCategory, CategoryDto> builder = new DynBooleanBuilder<QCategory, CategoryDto>();
		builder = builder.searchPredicate(category, categoryDto);

		if (categoryDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(category.products.any(), categoryDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}
		*/
		return jpaQueryFactory.selectFrom(category)
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Category> searchPageByDto(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = searchByCriteria(categoryDto, predicates);
		
		/*
		DynBooleanBuilder<QCategory, CategoryDto> builder = new DynBooleanBuilder<QCategory, CategoryDto>();
		builder = builder.searchPredicate(category, categoryDto);

		if (categoryDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(category.products.any(), categoryDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}
		*/
		return jpaQueryFactory.selectFrom(category)
				.where(builder.asPredicate())
				.orderBy(sortBy(categoryDto))
				.offset(categoryDto.getStart().intValue())
				.limit(categoryDto.getLimit().intValue())
				.fetch();
	}

	protected DynBooleanBuilder<QCategory, CategoryDto> findByCriteria(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = new DynBooleanBuilder<QCategory, CategoryDto>();
		builder = builder.findPredicate(category, categoryDto);

		if (categoryDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(category.products.any(), categoryDto.getProductDto(), predicates).asPredicate();
			builder.and(byProductPredicate);
		}

		return builder;
	}
	
	protected DynBooleanBuilder<QCategory, CategoryDto> searchByCriteria(CategoryDto categoryDto, Predicate... predicates) {
		QCategory category = QCategory.category;
		
		DynBooleanBuilder<QCategory, CategoryDto> builder = new DynBooleanBuilder<QCategory, CategoryDto>();
		builder = builder.searchPredicate(category, categoryDto);

		if (categoryDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(category.products.any(), categoryDto.getProductDto(), predicates).asPredicate();
			builder.and(byProductPredicate);
		}

		return builder;
	}


	@Override
	public OrderSpecifier[] sortBy(CategoryDto categoryDto) {
		PathBuilder pb = new PathBuilder<QCategory>(QCategory.class, "category");
		return sortOrderOf(pb, categoryDto);
	}

}
