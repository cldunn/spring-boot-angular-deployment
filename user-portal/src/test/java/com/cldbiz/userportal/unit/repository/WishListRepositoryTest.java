package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.cldbiz.userportal.unit.repository.data.ProductData;
import com.cldbiz.userportal.unit.repository.data.WishListData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DatabaseSetup(value= {"/productData.xml", "/wishListData.xml", "/wishListProductData.xml"})
public class WishListRepositoryTest extends BaseRepositoryTest {

	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	WishListRepository wishListRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		List<WishList> wishLists =  wishListRepository.findAll();
		WishList wishList = wishLists.get(0);
		
		// clear cache to test performance
		wishListRepository.flush();
		
		// invoke existsById here
		Boolean exists = wishListRepository.existsById(wishList.getId());
		wishListRepository.flush();
		
		// check for existence
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnCount() {
		log.info("whenCountAll_thenReturnCount");
		
		// invoke countAll here
		Long wishListCount = wishListRepository.countAll();
		wishListRepository.flush();
		
		// check count
		assertThat(wishListCount).isEqualTo(TOTAL_ROWS.intValue());
	}

	@Test
	public void whenFindById_thenReturnWishList() {
		log.info("whenFindById_thenReturnWishList");
		
		// invoke countAll here
		Optional<WishList> sameWishList = wishListRepository.findById(3L);
		wishListRepository.flush();
		
		// check for wish list and related entities  
		assertThat(sameWishList.orElse(null)).isNotNull();
		assertThat(sameWishList.get().getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByIds_thenReturnWishLists() {
		log.info("whenFindByIds_thenReturnWishLists");
		
		// get all wish list ids
		List<WishList> wishLists = wishListRepository.findAll();
		List<Long> wishListIds = wishLists.stream().map(WishList::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		wishListRepository.flush();

		// invoke findByIds here
		wishLists = wishListRepository.findByIds(wishListIds);
		wishListRepository.flush();
		
		// check all wish lists were found
		assertThat(wishLists.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check that some wish lists have products
		List<WishList> wishListsWithProducts = wishLists.stream().filter(w -> !w.getProducts().isEmpty()).collect(Collectors.toList());
		assertThat(wishListsWithProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenFindAll_thenReturnAllWishLists() {
		log.info("whenFindAll_thenReturnAllWishLists");
		
		// invoke findAll here
		List<WishList> wishLists = wishListRepository.findAll();
		wishListRepository.flush();
		
		// check all wish lists returned
		assertThat(wishLists.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check that some wish lists have products
		List<WishList> wishListsWithProducts = wishLists.stream().filter(w -> !w.getProducts().isEmpty()).collect(Collectors.toList());
		assertThat(wishListsWithProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteById_thenRemoveWishList() {
		log.info("whenDeleteById_thenRemoveWishList");
		
		WishList wishList = wishListRepository.findById(1L).get();
		wishList.getProducts().size();
		
		// clear cache to test performance
		wishListRepository.flush();

		// invoke deleteById here
		wishListRepository.deleteById(wishList.getId());
		wishListRepository.flush();
		
		// check category deleted
		List<WishList> wishLists =  wishListRepository.findAll();
		assertThat(wishLists.contains(wishList)).isFalse();
		
		// check products still exist but not related
		wishList.getProducts().forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
		});
	}

	@Test
	public void whenDeleteByIds_thenRemoveWishLists() {
		log.info("whenDeleteByIds_thenRemoveWishLists");
		
		// get all wish lists
		List<WishList> wishLists =  wishListRepository.findAll();
		wishLists.forEach(c -> c.getProducts().size());
		
		// get all wish list ids
		List<Long> wishListIds = wishLists.stream().map(WishList::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		wishListRepository.flush();

		// invoke deleteByIds here
		wishListRepository.deleteByIds(wishListIds);
		wishListRepository.flush();
		
		// check all categories deleted
		long wishListCnt =  wishListRepository.countAll();
		assertThat(wishListCnt).isZero();

		// get products from any wish list
		Set<Product> relatedProducts = wishLists.stream()
				.flatMap(w -> w.getProducts().stream()).map(p -> p)
				.collect(Collectors.toSet());

		// check products still exist
		relatedProducts.forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemoveWishList() {
		log.info("whenDeleteByEntity_thenRemoveWishList");
		
		// List<WishList> wishLists =  wishListRepository.findAll();
		WishList wishList = wishListRepository.findById(3L).get(); 
		wishList.getProducts().size();
		
		// clear cache to test performance
		wishListRepository.flush();

		// invoke deleteByEntity here
		wishListRepository.deleteByEntity(wishList);
		wishListRepository.flush();
		
		// check wish list deleted
		List<WishList> wishLists =  wishListRepository.findAll();
		assertThat(wishLists.contains(wishList)).isFalse();
		
		// check products still exist
		wishList.getProducts().forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
		});
	}

	@Test
	public void whenDeleteByEntities_thenRemoveWishLists() {
		log.info("whenDeleteByEntities_thenRemoveWishLists");
		
		// get all wish lists
		List<WishList> wishLists =  wishListRepository.findAll();
		wishLists.forEach(w -> w.getProducts().size());
		
		// clear cache to test performance
		wishListRepository.flush();

		// invoke deleteByEntities here
		wishListRepository.deleteByEntities(wishLists);
		wishListRepository.flush();
		
		// check all categories deleted
		long wishListCnt =  wishListRepository.countAll();
		assertThat(wishListCnt).isZero();
		
		// get products from any wish list
		Set<Product> relatedProducts = wishLists.stream()
				.flatMap(w -> w.getProducts().stream()).map(p -> p)
				.collect(Collectors.toSet());

		// check products still exist
		relatedProducts.forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
		});
	}

	@Test
	public void whenSaveEntity_thenReturnSavedWishList() {
		log.info("whenSaveEntity_thenReturnSavedWishList");
		
		//create a new wish list
		WishList anotherWishList = WishListData.getAnotherWishList();
		
		// Add existing product to new wish list
		Product anotherProduct = ProductData.getAnotherExistingProduct(); 
		anotherWishList.getProducts().add(anotherProduct);
		
		// clear cache to test performance
		wishListRepository.flush();

		// invoke saveEntity here
		WishList savedWishList = wishListRepository.saveEntity(anotherWishList);
		wishListRepository.flush();
		
		// confirm persisted wish list returned
		assertThat(savedWishList.equals(anotherWishList));
		
		// confirmed wish list persisted
		long wishListCnt =  wishListRepository.countAll();
		assertThat(wishListCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// retrieve saved wish list from store
		Optional<WishList> rtrvWishList = wishListRepository.findById(savedWishList.getId());
		assertThat(rtrvWishList.orElse(null)).isNotNull();
		
		// check retrieved wish list matches saved wish list
		assertThat(rtrvWishList.get().equals(anotherWishList)).isTrue();
		assertThat(rtrvWishList.get().equals(savedWishList)).isTrue();
		
		// check persisted wish list products match saved wish list products
		assertThat(Boolean.FALSE.equals(rtrvWishList.get().getProducts().isEmpty()));
		assertThat(rtrvWishList.get().getProducts().stream().allMatch(p -> savedWishList.getProducts().contains(p)));
		assertThat(savedWishList.getProducts().stream().allMatch(p -> rtrvWishList.get().getProducts().contains(p)));
	}

	@Test
	public void whenSaveEntities_thenReturnSavedWishLists() {
		log.info("whenSaveEntities_thenReturnSavedWishLists");
		
		// create 1st new wish list
		WishList anotherWishList = WishListData.getAnotherWishList();
		
		// add product to 1st wish list
		Product anotherProduct = ProductData.getAnotherExistingProduct(); 
		anotherWishList.getProducts().add(anotherProduct);
		
		// create 2nd new wish list
		WishList extraWishList = WishListData.getExtraWishList();
		
		// add product to 2nd wish list
		Product extraProduct = ProductData.getAnotherExistingProduct(); 
		extraWishList.getProducts().add(extraProduct);

		// create list of new wish Lists
		List<WishList> wishLists = new ArrayList<WishList>();
		wishLists.add(anotherWishList);
		wishLists.add(extraWishList);
		
		// clear cache to test performance
		wishListRepository.flush();

		// invoke saveEntities here
		List<WishList> savedWishLists = wishListRepository.saveEntities(wishLists);
		wishListRepository.flush();
		
		// confirm new wish lists returned
		assertThat(savedWishLists.size() == 2).isTrue();
		
		// check returned wish lists match new wish lists
		assertThat(wishLists.stream().allMatch(t -> savedWishLists.contains(t))).isTrue();
		assertThat(savedWishLists.stream().allMatch(t -> wishLists.contains(t))).isTrue();
		
		// confirm new wish lists persisted
		long wishListCnt =  wishListRepository.countAll();
		assertThat(wishListCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// retrieve 1st new wish list
		Optional<WishList> rtrvAnotherWishList = wishListRepository.findById(anotherWishList.getId());
		assertThat(rtrvAnotherWishList.orElse(null)).isNotNull();
		
		// check retrieved wish list equals 1st wish list and contains its product
		assertThat(rtrvAnotherWishList.get().equals(anotherWishList)).isTrue();
		assertThat(rtrvAnotherWishList.get().getProducts().contains(anotherProduct));
		
		// retrieve 2nd new wish list
		Optional<WishList> rtrvExtaWishList = wishListRepository.findById(extraWishList.getId());
		assertThat(rtrvExtaWishList.orElse(null)).isNotNull();
		
		// check retrieved wish list equals 2nd wish list and contains its product
		assertThat(rtrvExtaWishList.get().equals(extraWishList)).isTrue();
		assertThat(rtrvExtaWishList.get().getProducts().contains(extraProduct));
	}


	@Test
	public void whenModified_thenWishListUpdated() {
		log.info("whenModified_thenWishListUpdated");
		
		// retrieve wish list
		Optional<WishList> originalWishList = wishListRepository.findById(3L);
		
		// update wish list and related entities 
		originalWishList.get().setName("UPDATED - " + originalWishList.get().getName());
		originalWishList.get().getProducts().forEach(p -> p.setName("UPDATED - " + p.getName()));
		
		// retrieve wish list again
		Optional<WishList> rtrvdWishList = wishListRepository.findById(3L);
		
		// check wish list and related entities updated without save
		assertThat(originalWishList.get().getName().equals((rtrvdWishList.get().getName())));
		rtrvdWishList.get().getProducts().forEach(p -> assertThat(originalWishList.get().getProducts().contains(p)));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// create wish list dto for search criteria
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("Weekly");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("pen");
		wishListDto.setProductDto(productDto);

		// invoke existsByDto here
		Boolean exists = wishListRepository.existsByDto(wishListDto);
		wishListRepository.flush();
		
		// check existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// create wish list dto for search criteria
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("Annual");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Pen");
		wishListDto.setProductDto(productDto);
		
		// invoke countByDto here
		long wishListCnt =  wishListRepository.countByDto(wishListDto);
		wishListRepository.flush();
		
		// check count
		assertThat(wishListCnt).isGreaterThanOrEqualTo(1L);
	}
	
	@Test
	public void whenFindByDto_thenReturnWishLists() {
		log.info("whenFindByDto_thenReturnWishLists");
		
		// create wish list dto for search criteria
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("Annual");
		
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		wishListDto.setProductDto(productDto);
		
		// invoke findByDto here
		List<WishList> wishLists = wishListRepository.findByDto(wishListDto);
		wishListRepository.flush();
		
		// check wish lists found
		assertThat(wishLists).isNotEmpty();
		
		// check results match criteria
		wishLists.forEach(wishList -> {
			assertThat(wishList.getName().equals(wishListDto.getName()));
			assertThat(wishList.getProducts().contains(product));
		});
	}

	@Test
	public void whenFindPageByDto_thenReturnWishLists() {
		log.info("whenFindPageByDto_thenReturnWishLists");
		
		// create wish list dto for search criteria
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("Annual");
		
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		wishListDto.setProductDto(productDto);
		
		// limit to first two wish lists
		wishListDto.setStart(0);
		wishListDto.setLimit(2);
		
		// invoke findPageByDto here
		List<WishList> wishLists = wishListRepository.findPageByDto(wishListDto);
		wishListRepository.flush();
		
		// assert no more than 2 records found
		assertThat(wishLists).isNotEmpty();
		assertThat(wishLists.size()).isLessThanOrEqualTo(2);
		
		// check results match criteria
		wishLists.forEach(wishList -> {
			assertThat(wishList.getName().equals(wishListDto.getName()));
			assertThat(wishList.getProducts().contains(product));
		});
	}

	@Test
	public void whenSearchByDto_thenReturnWishLists() {
		log.info("whenSearchByDto_thenReturnWishLists");
		
		// create wish list dto for search criteria
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("l");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("pen");
		wishListDto.setProductDto(productDto);
		
		// invoke searchByDto here
		List<WishList> wishLists = wishListRepository.searchByDto(wishListDto);
		wishListRepository.flush();
		
		// check wish lists found
		assertThat(wishLists).isNotEmpty();

		// check results match criteria
		wishLists.forEach(wishList -> {
			assertThat(wishList.getName().contains("l"));	
		});
		
		wishLists.forEach(wishList -> {
			assertThat(wishList.getName().contains(wishListDto.getName()));
		});
		
		Set<String> productNames = wishLists.stream()
			.flatMap(w -> w.getProducts().stream())
			.map(p -> p.getName())
			.collect(Collectors.toSet());
		
		productNames.forEach(name -> {
			assertThat(name.indexOf(productDto.getName()) > 0);
		});
	}

	@Test
	public void whenSearchPageByDto_thenReturnWishLists() {
		log.info("whenSearchPageByDto_thenReturnWishLists");
		
		// create wish list dto for search criteria
		WishListDto wishListDto = new WishListDto();
		wishListDto.setName("l");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("pen");
		wishListDto.setProductDto(productDto);

		// limit to first two wish lists
		wishListDto.setStart(0);
		wishListDto.setLimit(2);

		// invoke searchPageByDto here
		List<WishList> wishLists = wishListRepository.searchPageByDto(wishListDto);
		wishListRepository.flush();
		
		// assert no more than 2 records found
		assertThat(wishLists).isNotEmpty();
		assertThat(wishLists.size()).isLessThanOrEqualTo(2);
		
		// check results match criteria
		wishLists.forEach(wishList -> {
			assertThat(wishList.getName().contains("l"));	
		});
		
		wishLists.forEach(wishList -> {
			assertThat(wishList.getName().contains(wishListDto.getName()));
		});
		
		Set<String> productNames = wishLists.stream()
			.flatMap(w -> w.getProducts().stream())
			.map(p -> p.getName())
			.collect(Collectors.toSet());
		
		productNames.forEach(name -> {
			assertThat(name.indexOf(productDto.getName()) > 0);
		});
	}
}
