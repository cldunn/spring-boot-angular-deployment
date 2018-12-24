package com.cldbiz.userportal.repository.product;

import java.util.List;

import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class ProductRepositoryImpl extends BaseRepositoryImpl<Product, ProductDto, Long> implements ProductRepositoryExt {

	@Override
	public List<Product> findAll() {
		QProduct product = QProduct.product;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.fetch();
	}

	@Override
	public List<Product> findAllById(List<Long> productIds) {
		QProduct product = QProduct.product;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(product.id.in(productIds))
				.fetch();
	}

	@Override
	public List<Product> findByDto(ProductDto productDto) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = new DynBooleanBuilder<QProduct, ProductDto>();
		Predicate predicate = builder.findPredicate(product, productDto).asPredicate();

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(predicate)
				.fetch();
	}

	@Override
	public List<Product> findPageByDto(ProductDto productDto) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = new DynBooleanBuilder<QProduct, ProductDto>();
		Predicate predicate = builder.findPredicate(product, productDto).asPredicate();

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(predicate)
				.orderBy(sortBy(productDto))
				.offset(productDto.getStart().intValue())
				.limit(productDto.getLimit().intValue())
				.fetch();
	}

	public Long countSearchByDto(ProductDto productDto) {
		QProduct product = QProduct.product;

		DynBooleanBuilder<QProduct, ProductDto> builder = new DynBooleanBuilder<QProduct, ProductDto>();
		Predicate predicate = builder.searchPredicate(product, productDto).asPredicate();

		return jpaQueryFactory.selectFrom(product)
				.where(predicate)
				.fetchCount();
	}
	

	@Override
	public List<Product> searchByDto(ProductDto productDto) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = new DynBooleanBuilder<QProduct, ProductDto>();
		Predicate predicate = builder.searchPredicate(product, productDto).asPredicate();
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(predicate)
				.fetch();
	}

	@Override
	public List<Product> searchPageByDto(ProductDto productDto) {
		QProduct product = QProduct.product;
		
		DynBooleanBuilder<QProduct, ProductDto> builder = new DynBooleanBuilder<QProduct, ProductDto>();
		Predicate predicate = builder.searchPredicate(product, productDto).asPredicate();
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(product)
				.where(predicate)
				.orderBy(sortBy(productDto))
				.offset(productDto.getStart().intValue())
				.limit(productDto.getLimit().intValue())
				.fetch();
		
	}

	@Override
	public OrderSpecifier[] sortBy(ProductDto productDto) {
		PathBuilder pb = new PathBuilder<QProduct>(QProduct.class, "product");
		return sortOrderOf(pb, productDto);
	}

}
