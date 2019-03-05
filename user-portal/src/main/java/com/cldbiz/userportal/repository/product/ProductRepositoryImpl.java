package com.cldbiz.userportal.repository.product;

import java.util.List;

import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCategory;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CategoryDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.AbstractDaoImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class ProductRepositoryImpl extends AbstractDaoImpl<Product, ProductDto, Long> implements ProductRepositoryExt {

	@Override
	public Boolean existsByDto(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = searchByCriteria(productDto, predicates);
		
		return jpaQueryFactory.selectFrom(product).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = searchByCriteria(productDto, predicates);
		
		return jpaQueryFactory.selectFrom(product).where(builder.asPredicate()).fetchCount();
	}
	
	@Override
	public List<Product> findByIds(List<Long> productIds) {
		QProduct product = QProduct.product;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(product.id.in(productIds))
				.fetch();
	}

	@Override
	public List<Product> findAll() {
		QProduct product = QProduct.product;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product).fetch();
	}

	/* TODO Rework search criteria ... */
	@Override
	public List<Product> findByDto(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = findByCriteria(productDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Product> findPageByDto(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = findByCriteria(productDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(builder.asPredicate())
				.orderBy(sortBy(productDto))
				.offset(productDto.getStart().intValue())
				.limit(productDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public List<Product> searchByDto(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = searchByCriteria(productDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Product> searchPageByDto(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = searchByCriteria(productDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(builder.asPredicate())
				.orderBy(sortBy(productDto))
				.offset(productDto.getStart().intValue())
				.limit(productDto.getLimit().intValue())
				.fetch();
		
	}

	protected DynBooleanBuilder<QProduct, ProductDto> findByCriteria(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = new DynBooleanBuilder<QProduct, ProductDto>();
		builder = builder.findPredicate(product, productDto, predicates);

		if (productDto.getCategoryDto() != null) {
			DynBooleanBuilder<QCategory, CategoryDto> byCategoryBuilder = new DynBooleanBuilder<QCategory, CategoryDto>();
			Predicate byCategoryPredicate = byCategoryBuilder.findPredicate(product.categories.any(), productDto.getCategoryDto(), predicates).asPredicate();
			builder.and(byCategoryPredicate);
		}

		return builder;
	}
	
	protected DynBooleanBuilder<QProduct, ProductDto> searchByCriteria(ProductDto productDto, Predicate... predicates) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = new DynBooleanBuilder<QProduct, ProductDto>();
		builder = builder.searchPredicate(product, productDto, predicates);

		if (productDto.getCategoryDto() != null) {
			DynBooleanBuilder<QCategory, CategoryDto> byCategoryBuilder = new DynBooleanBuilder<QCategory, CategoryDto>();
			Predicate byCategoryPredicate = byCategoryBuilder.searchPredicate(product.categories.any(), productDto.getCategoryDto(), predicates).asPredicate();
			builder.and(byCategoryPredicate);
		}

		return builder;
	}

	@Override
	public OrderSpecifier[] sortBy(ProductDto productDto) {
		PathBuilder pb = new PathBuilder<QProduct>(QProduct.class, "product");
		return sortOrderOf(pb, productDto);
	}

}
