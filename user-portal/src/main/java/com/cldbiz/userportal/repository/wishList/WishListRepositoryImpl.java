package com.cldbiz.userportal.repository.wishList;

import java.util.List;

import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QWishList;
import com.cldbiz.userportal.domain.WishList;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.WishListDto;
import com.cldbiz.userportal.repository.AbstractRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class WishListRepositoryImpl extends AbstractRepositoryImpl<WishList, WishListDto, Long> implements WishListRepositoryExt {

	@Override
	public List<WishList> findAll() {
		QWishList wishList = QWishList.wishList;
		
		return jpaQueryFactory.selectFrom(wishList).fetch();
	}

	/* rename to findAllByIds */
	@Override
	public List<WishList> findAllById(List<Long> wishListIds) {
		QWishList wishList = QWishList.wishList;
		
		return jpaQueryFactory.selectFrom(wishList)
				.where(wishList.id.in(wishListIds))
				.fetch();
	}

	@Override
	public List<WishList> findByDto(WishListDto wishListDto) {
		QWishList wishList = QWishList.wishList;
		
		DynBooleanBuilder<QWishList, WishListDto> builder = new DynBooleanBuilder<QWishList, WishListDto>();
		builder = builder.findPredicate(wishList, wishListDto);

		if (wishListDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(wishList.products.any(), wishListDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}

		return jpaQueryFactory.selectFrom(wishList)
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<WishList> findPageByDto(WishListDto wishListDto) {
		QWishList wishList = QWishList.wishList;
		
		DynBooleanBuilder<QWishList, WishListDto> builder = new DynBooleanBuilder<QWishList, WishListDto>();
		builder = builder.findPredicate(wishList, wishListDto);

		if (wishListDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.findPredicate(wishList.products.any(), wishListDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}

		return jpaQueryFactory.selectFrom(wishList)
				.where(builder.asPredicate())
				.orderBy(sortBy(wishListDto))
				.offset(wishListDto.getStart().intValue())
				.limit(wishListDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public Long countSearchByDto(WishListDto wishListDto) {
		QWishList wishList = QWishList.wishList;
		
		DynBooleanBuilder<QWishList, WishListDto> builder = new DynBooleanBuilder<QWishList, WishListDto>();
		builder = builder.searchPredicate(wishList, wishListDto);

		if (wishListDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(wishList.products.any(), wishListDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}

		return jpaQueryFactory.selectFrom(wishList)
				.where(builder.asPredicate())
				.fetchCount();
	}

	@Override
	public List<WishList> searchByDto(WishListDto wishListDto) {
		QWishList wishList = QWishList.wishList;
		
		DynBooleanBuilder<QWishList, WishListDto> builder = new DynBooleanBuilder<QWishList, WishListDto>();
		builder = builder.searchPredicate(wishList, wishListDto);

		if (wishListDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(wishList.products.any(), wishListDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}

		return jpaQueryFactory.selectFrom(wishList)
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<WishList> searchPageByDto(WishListDto wishListDto) {
		QWishList wishList = QWishList.wishList;
		
		DynBooleanBuilder<QWishList, WishListDto> builder = new DynBooleanBuilder<QWishList, WishListDto>();
		builder = builder.searchPredicate(wishList, wishListDto);

		if (wishListDto.getProductDto() != null) {
			DynBooleanBuilder<QProduct, ProductDto> byProductBuilder = new DynBooleanBuilder<QProduct, ProductDto>();
			Predicate byProductPredicate = byProductBuilder.searchPredicate(wishList.products.any(), wishListDto.getProductDto()).asPredicate();
			builder.and(byProductPredicate);
		}

		return jpaQueryFactory.selectFrom(wishList)
				.where(builder.asPredicate())
				.orderBy(sortBy(wishListDto))
				.offset(wishListDto.getStart().intValue())
				.limit(wishListDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public OrderSpecifier[] sortBy(WishListDto wishListDto) {
		PathBuilder pb = new PathBuilder<QWishList>(QWishList.class, "wishList");
		return sortOrderOf(pb, wishListDto);
	}

}
