package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Category;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.domain.WishList;
import com.cldbiz.userportal.dto.CategoryDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.WishListDto;
import com.cldbiz.userportal.repository.product.ProductRepository;
import com.cldbiz.userportal.repository.wishList.WishListRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/productData.xml", "/wishListData.xml", "/wishListProductData.xml"})
public class WishListRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(WishListRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	WishListRepository wishListRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		List<WishList> wishLists =  wishListRepository.findAll();
		WishList wishList = wishLists.get(0);
		
		Boolean exists = wishListRepository.existsById(wishList.getId());
		wishListRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		Long wishListCount =  wishListRepository.countAll();
		wishListRepository.flush();
		
		assertThat(wishListCount).isEqualTo(TOTAL_ROWS.intValue());
	}

	@Test
	public void whenFindById_thenReturnWishList() {
		Optional<WishList> sameWishList = wishListRepository.findById(3L);
		wishListRepository.flush();
		
		assertThat(sameWishList.orElse(null)).isNotNull();
		assertThat(sameWishList.get().getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByIds_thenReturnWishLists() {
		List<WishList> wishLists = wishListRepository.findAll();
		List<Long> wishListIds = wishLists.stream().map(WishList::getId).collect(Collectors.toList());
		
		wishLists = wishListRepository.findByIds(wishListIds);
		wishListRepository.flush();
		
		assertThat(wishLists.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(wishLists.get(0).getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindAll_thenReturnAllWishLists() {
		List<WishList> wishLists = wishListRepository.findAll();
		wishListRepository.flush();
		
		assertThat(wishLists.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(wishLists.get(0).getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteById_thenRemoveWishList() {
		List<WishList> wishLists =   wishListRepository.findAll();
		WishList wishList = wishLists.get(0);
		wishList.getProducts().size();
		
		wishListRepository.deleteById(wishList.getId());
		wishListRepository.flush();
		
		wishLists =  wishListRepository.findAll();

		assertThat(wishLists.contains(wishList)).isFalse();
		
		List<Product> deletedProducts = wishList.getProducts().stream()
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.size()).isEqualTo(wishList.getProducts().size());
	}

	@Test
	public void whenDeleteByIds_thenRemoveWishLists() {
		List<WishList> wishLists =  wishListRepository.findAll();
		wishLists.forEach(c -> c.getProducts().size());
		
		List<Long> wishListIds = wishLists.stream().map(WishList::getId).collect(Collectors.toList());
		
		wishListRepository.deleteByIds(wishListIds);
		wishListRepository.flush();
		
		long wishListCnt =  wishListRepository.countAll();
		
		assertThat(wishListCnt).isZero();

		List<Product> deletedProducts = wishLists.stream()
				.flatMap(c -> c.getProducts().stream())
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteByEntity_thenRemoveWishList() {
		List<WishList> wishLists =  wishListRepository.findAll();
		WishList wishList = wishLists.get(0);
		wishList.getProducts().size();
		
		wishListRepository.deleteByEntity(wishList);
		wishListRepository.flush();
		
		wishLists =  wishListRepository.findAll();

		assertThat(wishLists.contains(wishList)).isFalse();
		
		List<Product> deletedProducts = wishList.getProducts().stream()
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.size()).isEqualTo(wishList.getProducts().size());
	}

	@Test
	public void whenDeleteByEntities_thenRemoveWishLists() {
		List<WishList> wishLists =  wishListRepository.findAll();
		wishLists.forEach(w -> w.getProducts().size());
		
		wishListRepository.deleteByEntities(wishLists);
		wishListRepository.flush();
		
		long wishListCnt =  wishListRepository.countAll();
		assertThat(wishListCnt).isZero();
		
		List<Product> deletedProducts = wishLists.stream()
			.flatMap(c -> c.getProducts().stream())
			.filter(p -> productRepository.findById(p.getId()) != null )
			.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenSaveEntity_thenReturnSavedWishList() {
		WishList anotherWishList = getAnotherWishList();
		Product anotherProduct = getAnotherProduct(); 
		
		anotherWishList.getProducts().add(anotherProduct);
		
		WishList savedWishList = wishListRepository.saveEntity(anotherWishList);
		wishListRepository.flush();
		
		assertThat(savedWishList.equals(anotherWishList)).isTrue();
		
		long wishListCnt =  wishListRepository.countAll();
		assertThat(wishListCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<WishList> rtrvWishList = wishListRepository.findById(savedWishList.getId());
		assertThat(rtrvWishList.orElse(null)).isNotNull();
		assertThat(rtrvWishList.get().equals(anotherWishList)).isTrue();
		assertThat(rtrvWishList.get().equals(savedWishList)).isTrue();
		
		assertThat(savedWishList.getProducts().contains(anotherProduct));
	}

	@Test
	public void whenSaveEntities_thenReturnSavedCategoriess() {
		WishList anotherWishList = getAnotherWishList();
		Product anotherProduct = getAnotherProduct(); 

		anotherWishList.getProducts().add(anotherProduct);
		
		WishList extraWishList = getExtraWishList();
		Product extraProduct = getExtraProduct(); 

		extraWishList.getProducts().add(extraProduct);

		List<WishList> wishLists = new ArrayList<WishList>();
		wishLists.add(anotherWishList);
		wishLists.add(extraWishList);
		
		List<WishList> savedWishLists = wishListRepository.saveEntities(wishLists);
		wishListRepository.flush();
		
		assertThat(savedWishLists.size() == 2).isTrue();
		
		assertThat(wishLists.stream().allMatch(t -> savedWishLists.contains(t))).isTrue();
		assertThat(savedWishLists.stream().allMatch(t -> wishLists.contains(t))).isTrue();
		
		long wishListCnt =  wishListRepository.countAll();
		assertThat(wishListCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<WishList> rtrvAnotherWishList = wishListRepository.findById(anotherWishList.getId());
		assertThat(rtrvAnotherWishList.orElse(null)).isNotNull();
		assertThat(rtrvAnotherWishList.get().equals(anotherWishList)).isTrue();
		assertThat(rtrvAnotherWishList.get().getProducts().contains(anotherProduct));
		
		Optional<WishList> rtrvExtaWishList = wishListRepository.findById(extraWishList.getId());
		assertThat(rtrvExtaWishList.orElse(null)).isNotNull();
		assertThat(rtrvExtaWishList.get().equals(extraWishList)).isTrue();
		assertThat(rtrvExtaWishList.get().getProducts().contains(extraProduct));
	}


	@Test
	public void whenModified_thenWishListUpdated() {
		Optional<WishList> originalWishList = wishListRepository.findById(3L);
		originalWishList.get().setName("UPDATED - " + originalWishList.get().getName());
		originalWishList.get().getProducts().forEach(p -> p.setName("UPDATED - " + p.getName()));
		
		Optional<WishList> rtrvdWishList = wishListRepository.findById(3L);
		assertThat(originalWishList.get().getName().equals((rtrvdWishList.get().getName())));
		rtrvdWishList.get().getProducts().forEach(p -> assertThat(originalWishList.get().getProducts().contains(p)));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("Weekly");
		
		Boolean exists = wishListRepository.existsByDto(wishListDto);
		wishListRepository.flush();
		
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		WishListDto wishListDto = new WishListDto();
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Pen");
		wishListDto.setProductDto(productDto);
		
		long wishListCnt =  wishListRepository.countByDto(wishListDto);
		wishListRepository.flush();
		
		assertThat(wishListCnt).isGreaterThanOrEqualTo(2L);
	}
	

	@Test
	public void whenFindByDto_thenReturnWishLists() {
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("Weekly");
		
		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		wishListDto.setProductDto(productDto);
		
		List<WishList> wishLists = wishListRepository.findByDto(wishListDto);
		wishListRepository.flush();
		
		assertThat(wishLists).isNotEmpty();
		
		Optional<WishList> wishList = wishLists.stream().findFirst();
		
		assertThat(wishList.orElse(null)).isNotNull();
		assertThat(wishList.get().getName().equals("Weekly")).isTrue();
		assertThat(wishList.get().getProducts().contains(product));
	}

	@Test
	public void whenFindPageByDto_thenReturnWishLists() {
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("Annual");
		
		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		wishListDto.setProductDto(productDto);
		
		wishListDto.setStart(0);
		wishListDto.setLimit(2);
		
		List<WishList> wishLists = wishListRepository.findPageByDto(wishListDto);
		wishListRepository.flush();
		
		assertThat(wishLists).isNotEmpty();
		assertThat(wishLists.size()).isLessThanOrEqualTo(2);
		assertThat(wishLists.get(0).getName().equals("Annual")).isTrue();
		assertThat(wishLists.get(0).getProducts().contains(product));
	}

	@Test
	public void whenSearchByDto_thenReturnWishLists() {
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("l");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("pen");
		wishListDto.setProductDto(productDto);
		
		List<WishList> wishLists = wishListRepository.searchByDto(wishListDto);
		wishListRepository.flush();
		
		assertThat(wishLists).isNotEmpty();
		assertThat(wishLists.get(0).getName().contains("l"));
		assertThat(wishLists.get(0).getProducts().get(0).getName().contains("pen"));
	}

	@Test
	public void whenSearchPageByDto_thenReturnWishLists() {
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("l");
		wishListDto.setStart(0);
		wishListDto.setLimit(2);
		
		ProductDto productDto = new ProductDto();
		productDto.setName("pen");
		wishListDto.setProductDto(productDto);

		List<WishList> wishLists = wishListRepository.searchPageByDto(wishListDto);
		wishListRepository.flush();
		
		assertThat(wishLists).isNotEmpty();
		assertThat(wishLists.size()).isLessThanOrEqualTo(2);
		assertThat(wishLists.stream().filter(wl -> wl.getName().contains("l")).collect(Collectors.toList()).size()).isEqualTo(wishLists.size());
		assertThat(wishLists.stream().flatMap(wl -> wl.getProducts().stream()).anyMatch(p -> p.getName().contains("pen")));
	}

	private WishList getAnotherWishList() {
		WishList wishList = new WishList();
		
		wishList.setName("Monthly");
		
		return wishList;
	}
	
	private WishList getExtraWishList() {
		WishList wishList = new WishList();
		
		wishList.setName("Technology");
		
		return wishList;
	}

	private Product getAnotherProduct() {
		Optional<Product> product = productRepository.findById(1L);
		return product.orElse(null);
	}

	private Product getExtraProduct() {
		Optional<Product> product = productRepository.findById(3L);
		return product.orElse(null);
	}

}
