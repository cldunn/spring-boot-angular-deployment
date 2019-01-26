package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Category;
import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.dto.CategoryDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.category.CategoryRepository;
import com.cldbiz.userportal.repository.lineItem.LineItemRepository;
import com.cldbiz.userportal.repository.product.ProductRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.CategoryData;
import com.cldbiz.userportal.unit.repository.data.ProductData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/purchaseOrderData.xml", "/productData.xml", "/lineItemData.xml", "/categoryData.xml", "/categoryProductData.xml"})  
public class ProductRepositoryTest  extends BaseRepositoryTest {

	private static final Long TOTAL_ROWS = 3L;
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	LineItemRepository lineItemRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		List<Product> products = productRepository.findAll();
		Product product = products.get(0);
		
		// clear cache to test performance
		productRepository.flush();

		// invoke existsById here
		Boolean exists = productRepository.existsById(product.getId());
		productRepository.flush();
		
		// check for existence
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnCount() {
		log.info("whenCountAll_thenReturnCount");
		
		// invoke countAll here
		long productCnt = productRepository.countAll();
		productRepository.flush();
		
		// check count
		assertThat(productCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnProduct() {
		log.info("whenFindById_thenReturnProduct");
		
		// invoke findById here
		Optional<Product> sameProduct = productRepository.findById(3L);
		productRepository.flush();
		
		// check for product and related entities  
		assertThat(sameProduct.orElse(null)).isNotNull();
		assertThat(sameProduct.get().getCategories().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByIds_thenReturnProducts() {
		log.info("whenFindByIds_thenReturnProducts");
		
		// get all product ids
		List<Product> products = productRepository.findAll();
		List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		productRepository.flush();

		// invoke findByIds here
		products = productRepository.findByIds(productIds);
		productRepository.flush();
		
		// check all products were found
		assertThat(products.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check that some products are in a categories
		List<Product> categorizedProducts = products.stream().filter(p -> !p.getCategories().isEmpty()).collect(Collectors.toList());
		assertThat(categorizedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenFindAll_thenReturnAllProducts() {
		log.info("whenFindAll_thenReturnAllProducts");
		
		// invoke findAll here
		List<Product> products = productRepository.findAll();
		productRepository.flush();
		
		// check all products returned
		assertThat(products.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check some products are in categories
		List<Product> categorizedProducts = products.stream().filter(p -> !p.getCategories().isEmpty()).collect(Collectors.toList());
		assertThat(categorizedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteById_thenRemoveProduct() {
		log.info("whenDeleteById_thenRemoveProduct");
		
		Product product = productRepository.findById(1L).get();
		
		// find the line items using this product
		ProductDto productDto = new ProductDto(product);
		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(productDto);
		
		// delete the line items using this product, prevent FK violation
		List<LineItem> lineItems = lineItemRepository.findByDto(lineItemDto);
		List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());
		lineItemRepository.deleteByIds(lineItemIds);
		
		// remove relationship between product and categories
		Set<Category> categories = new HashSet<Category>();
		for(Category category: product.getCategories()) {
			category.getProducts().remove(product);
			categories.add(category);
		}
		product.getCategories().clear();
		
		// clear cache to test performance
		productRepository.flush();

		// invoke deleteById here
		productRepository.deleteById(product.getId());
		productRepository.flush();
		
		// check product deleted
		List<Product> products = productRepository.findAll();
		assertThat(products.contains(product)).isFalse();
		
		// check categories still exist
		categories.forEach(category -> {
			assertThat(categoryRepository.existsById(category.getId()));
		});
	}

	@Test
	public void whenDeleteByIds_thenRemoveProducts() {
		log.info("whenDeleteByIds_thenRemoveProducts");
		
		// get all product ids
		List<Product> products = productRepository.findAll();
		List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
		
		products.stream().forEach(product -> {
			/* find the line items using this product (uni-directional) */
			ProductDto productDto = new ProductDto(product);
			LineItemDto lineItemDto = new LineItemDto();
			lineItemDto.setProductDto(productDto);
			
			List<LineItem> lineItems = lineItemRepository.findByDto(lineItemDto);
			List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());

			/* delete the line items using this product */
			lineItemRepository.deleteByIds(lineItemIds);
		});
	
		// remove relationships between products and categories
		Set<Category> categories = new HashSet<Category>();
		products.forEach(product -> {
			for(Category category: product.getCategories()) {
				category.getProducts().remove(product);
				categories.add(category);
			}
			product.getCategories().clear();
		});
		
		// clear cache to test performance
		productRepository.flush();

		// invoke deleteByIds here
		productRepository.deleteByIds(productIds);
		productRepository.flush();
		
		// check all products deleted
		long productCnt = productRepository.countAll();
		assertThat(productCnt).isZero();

		// check categories still exist
		categories.forEach(category -> {
			assertThat(categoryRepository.existsById(category.getId()));
		});
	}


	@Test
	public void whenDeleteByEntity_thenRemoveProduct() {
		log.info("whenDeleteByEntity_thenRemoveProduct");
		
		Product product = productRepository.findById(3L).get();
		
		/* find the line items using this product */
		ProductDto productDto = new ProductDto(product);
		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(productDto);
		
		/* delete the line items using this product */
		List<LineItem> lineItems = lineItemRepository.findByDto(lineItemDto);
		List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());
		lineItemRepository.deleteByIds(lineItemIds);
		
		// remove relationship between product and categories
		Set<Category> categories = new HashSet<Category>();
		for(Category category: product.getCategories()) {
			category.getProducts().remove(product);
			categories.add(category);
		}
		product.getCategories().clear();
		
		// clear cache to test performance
		productRepository.flush();

		// invoke deleteByEntity here
		productRepository.deleteByEntity(product);
		productRepository.flush();
		
		// check product deleted
		List<Product> products = productRepository.findAll();
		assertThat(products.contains(product)).isFalse();

		// check categories still exist
		categories.forEach(category -> {
			assertThat(categoryRepository.existsById(category.getId()));
		});

	}

	@Test
	public void whenDeleteByEntities_thenRemoveProducts() {
		log.info("whenDeleteByEntities_thenRemoveProducts");
		
		// get all products
		List<Product> products = productRepository.findAll();
		
		products.stream().forEach(product -> {
			/* find the line items using this product */
			ProductDto productDto = new ProductDto(product);
			LineItemDto lineItemDto = new LineItemDto();
			lineItemDto.setProductDto(productDto);
			
			/* delete the line items using this product */
			List<LineItem> lineItems = lineItemRepository.findByDto(lineItemDto);
			List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());
			lineItemRepository.deleteByIds(lineItemIds);
		});

		// remove relationships between products and categories
		Set<Category> categories = new HashSet<Category>();
		products.forEach(product -> {
			for(Category category: product.getCategories()) {
				category.getProducts().remove(product);
				categories.add(category);
			}
			product.getCategories().clear();
		});

		// clear cache to test performance
		productRepository.flush();

		// invoke deleteByEntities here
		productRepository.deleteByEntities(products);
		productRepository.flush();
		
		// check all products deleted
		long productCnt = productRepository.countAll();
		assertThat(productCnt).isZero();
		
		// check categories still exist
		categories.forEach(category -> {
			assertThat(categoryRepository.existsById(category.getId()));
		});
	}

	@Test
	public void whenSaveEntity_thenReturnSavedProduct() {
		log.info("whenSaveEntity_thenReturnSavedProduct");
		
		// create a new product
		Product anotherProduct = ProductData.getAnotherProduct();
		
		// add new product to categories
		Set<Category> someCategories = CategoryData.getSomeExistingCategories(); 
		anotherProduct.setCategories(someCategories);
		someCategories.forEach(category -> category.getProducts().add(anotherProduct));
		
		// clear cache to test performance
		productRepository.flush();

		// invoke saveEntity here
		Product savedProduct = productRepository.saveEntity(anotherProduct);
		productRepository.flush();
		
		// confirm persisted product returned
		assertThat(savedProduct.equals(anotherProduct)).isTrue();
		
		// confirmed product persisted
		long productCnt = productRepository.countAll();
		assertThat(productCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// retrieve saved product from store
		Optional<Product> rtrvProduct = productRepository.findById(savedProduct.getId());
		assertThat(rtrvProduct.orElse(null)).isNotNull();
		
		// check retrieved product matches saved product
		assertThat(rtrvProduct.get().equals(anotherProduct)).isTrue();
		assertThat(rtrvProduct.get().equals(savedProduct)).isTrue();
		
		
		// check persisted product categories match saved product categories
		assertThat(Boolean.FALSE.equals(rtrvProduct.get().getCategories().isEmpty()));
		assertThat(rtrvProduct.get().getCategories().stream().allMatch(t -> someCategories.contains(t)));
		assertThat(someCategories.stream().allMatch(t -> rtrvProduct.get().getCategories().contains(t)));
	}

	@Test
	public void whenSaveEntities_thenReturnSavedProducts() {
		log.info("whenSaveEntities_thenReturnSavedProducts");
		
		// create a new 1st product
		Product anotherProduct = ProductData.getAnotherProduct();

		// add new 1st product to some categories
		Set<Category> someCategories = CategoryData.getSomeExistingCategories(); 
		anotherProduct.setCategories(someCategories);
		someCategories.forEach(category -> category.getProducts().add(anotherProduct));

		// create a new 2nd product
		Product extraProduct = ProductData.getExtraProduct();
		
		// add new 2nd product to some categories
		Set<Category> moreCategories = CategoryData.getMoreExistingCategories();
		extraProduct.setCategories(moreCategories);
		moreCategories.forEach(category -> category.getProducts().add(extraProduct));
		
		// create array of new products
		List<Product> products = new ArrayList<Product>();
		products.add(anotherProduct);
		products.add(extraProduct);
		
		// clear cache to test performance
		productRepository.flush();

		// invoke saveEntities here
		List<Product> savedProducts = productRepository.saveEntities(products);
		productRepository.flush();
		
		// confirm new products returned
		assertThat(savedProducts.size() == 2).isTrue();
		
		// check returned products match new products
		assertThat(products.stream().allMatch(t -> savedProducts.contains(t)));
		assertThat(savedProducts.stream().allMatch(t -> products.contains(t)));
		
		// confirm new products persisted
		long productCnt = productRepository.countAll();
		assertThat(productCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// retrieve 1st new product
		Optional<Product> rtrvAnotherProduct = productRepository.findById(anotherProduct.getId());
		assertThat(rtrvAnotherProduct.orElse(null)).isNotNull();
		
		// check 1st new product matches retrieved product
		assertThat(rtrvAnotherProduct.get().equals(anotherProduct)).isTrue();
		
		// check retrieved products categories match 1st new product categories
		assertThat(Boolean.FALSE.equals(rtrvAnotherProduct.get().getCategories().isEmpty()));
		assertThat(rtrvAnotherProduct.get().getCategories().stream().allMatch(t -> someCategories.contains(t)));
		assertThat(someCategories.stream().allMatch(t -> rtrvAnotherProduct.get().getCategories().contains(t)));

		// retrieve 2nd new product
		Optional<Product> rtrvExtaProduct = productRepository.findById(extraProduct.getId());
		assertThat(rtrvExtaProduct.orElse(null)).isNotNull();
		
		// check 2nd new product matches retrieved product
		assertThat(rtrvExtaProduct.get().equals(extraProduct)).isTrue();
		
		// check retrieved product categories match 2nd new product categories
		assertThat(Boolean.FALSE.equals(rtrvExtaProduct.get().getCategories().isEmpty()));
		assertThat(rtrvExtaProduct.get().getCategories().stream().allMatch(t -> moreCategories.contains(t)));
		assertThat(moreCategories.stream().allMatch(t -> rtrvExtaProduct.get().getCategories().contains(t)));
	}

	@Test
	public void whenModified_thenProductUpdated() {
		log.info("whenModified_thenProductUpdated");
		
		// retrieve product
		Optional<Product> originalProduct = productRepository.findById(3L);
		
		// update product and related entities
		originalProduct.get().setDescription("UPDATED - " + originalProduct.get().getDescription());
		originalProduct.get().getCategories().forEach(category -> {
			category.setName("UPDATED - " + category.getName());
		});
		
		// retrieve product again 
		Optional<Product> rtrvdProduct = productRepository.findById(3L);
		
		// check product and related entities updated without save
		assertThat(originalProduct.get().getDescription().equals((rtrvdProduct.get().getDescription())));
		assertThat(originalProduct.get().getCategories().stream().allMatch(t -> rtrvdProduct.get().getCategories().contains(t)));
		assertThat(rtrvdProduct.get().getCategories().stream().allMatch(t -> originalProduct.get().getCategories().contains(t)));
	}


	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// create product dto for search criteria
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		productDto.setCategoryDto(categoryDto);
		
		// invoke existsByDto here
		Boolean exists = productRepository.existsByDto(productDto);
		productRepository.flush();
		
		// check existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// create product dto for search criteria
		ProductDto productDto = new ProductDto();
		productDto.setDescription("pack");
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		productDto.setCategoryDto(categoryDto);

		// invoke countByDto here
		long productCnt =  productRepository.countByDto(productDto);
		productRepository.flush();
		
		// check count
		assertThat(productCnt).isGreaterThanOrEqualTo(2L);
	}

	@Test
	public void whenFindByDto_thenReturnProducts() {
		log.info("whenFindByDto_thenReturnProducts");
		
		// create product dto for search criteria
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		productDto.setCategoryDto(categoryDto);

		// invoke findByDto here
		List<Product> products = productRepository.findByDto(productDto);
		productRepository.flush();
		
		// check products found
		assertThat(products).isNotEmpty();
		
		// check results match criteria
		products.forEach(product -> {
			assertThat(product.getName().equals(productDto.getName()));
			assertThat(product.getCategories().stream().map(c -> c.getName())
				.collect(Collectors.toList()).contains(categoryDto.getName()));
		});
	}

	@Test
	public void whenFindPageByDto_thenReturnProducts() {
		log.info("whenFindPageByDto_thenReturnProducts");
		
		// create product dto for search criteria
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		productDto.setCategoryDto(categoryDto);

		// limit to first two products
		productDto.setStart(0);
		productDto.setLimit(2);
		
		// invoke findPageByDto here
		List<Product> products = productRepository.findPageByDto(productDto);
		productRepository.flush();
		
		// assert no more than 2 records found
		assertThat(products).isNotEmpty();
		assertThat(products.size()).isLessThanOrEqualTo(2);
		
		// check results match criteria
		products.forEach(product -> {
			assertThat(product.getName().equals(productDto.getName()));
			assertThat(product.getCategories().stream().map(c -> c.getName())
				.collect(Collectors.toList()).contains(categoryDto.getName()));
		});
	}

	@Test
	public void whenSearchByDto_thenReturnProducts() {
		log.info("whenSearchByDto_thenReturnProducts");
		
		// create product dto for search criteria
		ProductDto productDto = new ProductDto();
		productDto.setDescription("pack");
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writ");
		productDto.setCategoryDto(categoryDto);

		// invoke searchByDto here
		List<Product> products = productRepository.searchByDto(productDto);
		productRepository.flush();
		
		// check products found
		assertThat(products).isNotEmpty();
		
		// check results match criteria
		products.forEach(product -> {
			assertThat(product.getDescription().contains(productDto.getDescription()));
			assertThat(product.getCategories().stream()
				.map(c -> c.getName())
				.filter(n -> n.contains(categoryDto.getName()))
				.collect(Collectors.toList()).size()).isGreaterThanOrEqualTo(1);
		});
	}

	@Test
	public void whenSearchPageByDto_thenReturnProducts() {
		log.info("whenSearchPageByDto_thenReturnProducts");
		
		// create product dto for search criteria
		ProductDto productDto = new ProductDto();
		productDto.setDescription("pack");
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writ");
		productDto.setCategoryDto(categoryDto);

		// limit to first two products
		productDto.setStart(0);
		productDto.setLimit(2);
		
		// invoke searchPageByDto here
		List<Product> products = productRepository.searchPageByDto(productDto);
		productRepository.flush();
		
		// assert no more than 2 records found
		assertThat(products).isNotEmpty();
		assertThat(products.size()).isLessThanOrEqualTo(2);
		
		// check results match criteria
		products.forEach(product -> {
			assertThat(product.getDescription().contains(productDto.getDescription()));
			assertThat(product.getCategories().stream()
				.map(c -> c.getName())
				.filter(n -> n.contains(categoryDto.getName()))
				.collect(Collectors.toList()).size()).isGreaterThanOrEqualTo(1);
		});
	}
}
