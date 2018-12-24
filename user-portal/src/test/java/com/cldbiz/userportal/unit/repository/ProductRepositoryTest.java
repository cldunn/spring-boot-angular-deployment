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
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.product.ProductRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/productData.xml"})  // TODO:  add "/lineItemData.xml", "/categoryData.xml", "/categoryProductData.xml"
public class ProductRepositoryTest  extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	ProductRepository productRepository;
	
	@Test
	public void whenCount_thenReturnCount() {
		long productCnt = productRepository.count();
		productRepository.flush();
		
		assertThat(productCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemoveProduct() {
		List<Product> products = productRepository.findAll();
		Product product = products.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		
		/* TODO: Develop LineItem.findByDto to return lineItmes with related product */
		/* TODO: delete lineItems with product before deleting product */
		
		productRepository.delete(product);
		productRepository.flush();
		
		products = productRepository.findAll();
		
		assertThat(products.contains(product)).isFalse();

		// TODO check categories still exists
	}

	@Test
	public void whenDeleteAll_thenRemoveAllProducts() {
		List<Product> products = productRepository.findAll();
		
		/* TODO: Develop LineItem.findByDto to return lineItmes with related product */
		/* TODO: delete lineItems for each product before deleting products */

		productRepository.deleteAll(products);
		productRepository.flush();
		
		long productCnt = productRepository.count();

		assertThat(productCnt).isZero();
		
		products.forEach(product -> {
			// check categories still exist but without products
		});
	}

	@Test
	public void whenDeleteById_thenRemoveProduct() {
		List<Product> products = productRepository.findAll();
		Product product = products.get(0);
		
		/* TODO: Develop LineItem.findByDto to return lineItmes with related product */
		/* TODO: delete lineItems for each product before deleting products */

		productRepository.deleteById(product.getId());
		productRepository.flush();
		
		products = productRepository.findAll();

		assertThat(products.contains(product)).isFalse();
		
		// check category still exist but without product
	}
	
	@Test
	public void whenDeleteByIds_thenRemoveAllProducts() {
		List<Product> products = productRepository.findAll();

		/* TODO: Develop LineItem.findByDto to return lineItmes with related product */
		/* TODO: delete lineItems for each product before deleting products */

		List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
		
		productRepository.deleteByIds(productIds);
		productRepository.flush();
		
		long productCnt = productRepository.count();

		assertThat(productCnt).isZero();

		products.forEach(product -> {
			// check categories still exist but without products
		});

	}

	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Product> products = productRepository.findAll();
		Product product = products.get(0);
		
		Boolean exists = productRepository.existsById(product.getId());
		productRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenFindAll_thenReturnAllProducts() {
		List<Product> products = productRepository.findAll();
		productRepository.flush();
		
		assertThat(products.size()).isEqualTo(TOTAL_ROWS.intValue());
		// Assert that at least one product belongs to a category
	}

	@Test
	public void whenFindAllById_thenReturnAllProducts() {
		List<Product> products = productRepository.findAll();
		List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
		
		products = productRepository.findAllById(productIds);
		productRepository.flush();
		
		assertThat(products.size()).isEqualTo(TOTAL_ROWS.intValue());
		// Assert that at least one product belongs to a category
	}

	@Test
	public void whenFindById_thenReturnProduct() {
		Optional<Product> sameProduct = productRepository.findById(3L);
		productRepository.flush();
		
		assertThat(sameProduct.orElse(null)).isNotNull();
		// Assert that the product belongs to a category
	}

	@Test
	public void whenSave_thenReturnSavedProduct() {
		Product anotherProduct = getAnotherProduct();
		List<Category> someCategories = getSomeCategories(); // TODO: implement getting categories for real
		anotherProduct.setCategories(someCategories);
		
		Product savedProduct = productRepository.save(anotherProduct);
		productRepository.flush();
		
		assertThat(savedProduct.equals(anotherProduct)).isTrue();
		
		long productCnt = productRepository.count();
		assertThat(productCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Product> rtrvProduct = productRepository.findById(savedProduct.getId());
		assertThat(rtrvProduct.orElse(null)).isNotNull();
		assertThat(rtrvProduct.get().equals(anotherProduct)).isTrue();
		assertThat(rtrvProduct.get().equals(savedProduct)).isTrue();
		
		// assertThat(Boolean.FALSE.equals(rtrvProduct.get().getCategories().isEmpty()));
		// assertThat(rtrvProduct.get().getCategories().stream().allMatch(t -> someCategories.contains(t))).isTrue();
		// assertThat(someCategories.stream().allMatch(t -> rtrvProduct.get().getCategories().contains(t))).isTrue();
	}

	@Test
	public void whenModified_thenProductUpdated() {
		Optional<Product> originalProduct = productRepository.findById(3L);
		originalProduct.get().setDescription("UPDATED - " + originalProduct.get().getDescription());
		// originalProduct.get().getCategories().get(0).setName("UPDATED - " + originalProduct.get().getCategories().get(0).getName());
		
		Optional<Product> rtrvdProduct = productRepository.findById(3L);
		assertThat(originalProduct.get().getDescription().equals((rtrvdProduct.get().getDescription())));
		// assertThat(originalProduct.get().getCategories().get(0).getName().equals((rtrvdProduct.get().getCategories().get(0).getName())));
	}

	@Test
	public void whenSaveAll_thenReturnSavedProducts() {
		Product anotherProduct = getAnotherProduct();

		List<Category> someCategories = getSomeCategories(); // TODO: implement getting categories for real
		anotherProduct.setCategories(someCategories);
		
		Product extraProduct = getExtraProduct();
		
		List<Category> moreCategories = getMoreCategories(); // TODO: implement getting categories for real
		extraProduct.setCategories(moreCategories);

		List<Product> products = new ArrayList<Product>();
		products.add(anotherProduct);
		products.add(extraProduct);
		
		List<Product> savedProducts = productRepository.saveAll(products);
		productRepository.flush();
		
		assertThat(savedProducts.size() == 2).isTrue();
		
		assertThat(products.stream().allMatch(t -> savedProducts.contains(t))).isTrue();
		assertThat(savedProducts.stream().allMatch(t -> products.contains(t))).isTrue();
		
		long productCnt = productRepository.count();
		assertThat(productCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<Product> rtrvAnotherProduct = productRepository.findById(anotherProduct.getId());
		assertThat(rtrvAnotherProduct.orElse(null)).isNotNull();
		assertThat(rtrvAnotherProduct.get().equals(anotherProduct)).isTrue();
		
		// assertThat(Boolean.FALSE.equals(rtrvProduct.get().getCategories().isEmpty()));
		// assertThat(rtrvProduct.get().getCategories().stream().allMatch(t -> someCategories.contains(t))).isTrue();
		// assertThat(someCategories.stream().allMatch(t -> rtrvProduct.get().getCategories().contains(t))).isTrue();

		Optional<Product> rtrvExtaProduct = productRepository.findById(extraProduct.getId());
		assertThat(rtrvExtaProduct.orElse(null)).isNotNull();
		assertThat(rtrvExtaProduct.get().equals(extraProduct)).isTrue();
		
		// assertThat(Boolean.FALSE.equals(rtrvExtaProduct.get().getCategories().isEmpty()));
		// assertThat(rtrvExtaProduct.get().getCategories().stream().allMatch(t -> moreCategories.contains(t))).isTrue();
		// assertThat(moreCategories.stream().allMatch(t -> rtrvExtaProduct.get().getCategories().contains(t))).isTrue();
	}

	@Test
	public void whenSaveAndFlush_thenReturnSavedProduct() {
		Product anotherProduct = getAnotherProduct();
		
		List<Category> someCategories = getSomeCategories(); // TODO: implement getting categories for real
		anotherProduct.setCategories(someCategories);
		
		Product savedProduct = productRepository.saveAndFlush(anotherProduct);
		
		assertThat(savedProduct.equals(anotherProduct)).isTrue();
		
		long productCnt = productRepository.count();
		assertThat(productCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Product> rtrvProduct = productRepository.findById(savedProduct.getId());
		assertThat(rtrvProduct.orElse(null)).isNotNull();
		assertThat(rtrvProduct.get().equals(anotherProduct)).isTrue();
		assertThat(rtrvProduct.get().equals(savedProduct)).isTrue();

		// assertThat(Boolean.FALSE.equals(rtrvExtaProduct.get().getCategories().isEmpty()));
		// assertThat(rtrvExtaProduct.get().getCategories().stream().allMatch(t -> moreCategories.contains(t))).isTrue();
		// assertThat(moreCategories.stream().allMatch(t -> rtrvExtaProduct.get().getCategories().contains(t))).isTrue();
	}

	@Test
	public void whenFindByDto_thenReturnProducts() {
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		
		List<Product> products = productRepository.findByDto(productDto);
		productRepository.flush();
		
		assertThat(products).isNotEmpty();
		
		Optional<Product> product = products.stream().findFirst();
		
		assertThat(product.orElse(null)).isNotNull();
		assertThat(product.get().getName().equals(productDto.getName())).isTrue();
	}

	@Test
	public void whenFindPageByDto_thenReturnProducts() {
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		productDto.setStart(0);
		productDto.setLimit(2);
		
		List<Product> products = productRepository.findPageByDto(productDto);
		productRepository.flush();
		
		assertThat(products.size()).isLessThanOrEqualTo(2);
	}

	@Test
	public void whenCountSearchByDto_thenReturnCount() {
		ProductDto productDto = new ProductDto();
		productDto.setDescription("pack");
		
		Long productCount = productRepository.countSearchByDto(productDto);
		productRepository.flush();
		
		assertThat(productCount).isGreaterThan(0L);

	}
	
	@Test
	public void whenSearchByDto_thenReturnProducts() {
		ProductDto productDto = new ProductDto();
		productDto.setDescription("pack");
		
		List<Product> products = productRepository.searchByDto(productDto);
		productRepository.flush();
		
		assertThat(products).isNotEmpty();
	}

	@Test
	public void whenSearchPageByDto_thenReturnProducts() {
		ProductDto productDto = new ProductDto();
		productDto.setDescription("pack");
		productDto.setStart(0);
		productDto.setLimit(2);
		
		List<Product> products = productRepository.searchPageByDto(productDto);
		productRepository.flush();
		
		assertThat(products.size()).isLessThanOrEqualTo(2);
	}

	private Product getAnotherProduct() {
		Product product = new Product();
		
		product.setUpc("71");
		product.setSku("91");
		product.setName("Note Cards");
		product.setPrice(5.42);
		product.setDescription("5X6 Note White Lined Cards");
		
		return product;
	}
	
	private Product getExtraProduct() {
		Product product = new Product();
		
		product.setUpc("171");
		product.setSku("191");
		product.setName("Binders");
		product.setPrice(7.41);
		product.setDescription("Spiral Binder Note book");
		
		return product;

	}

	private List<Category> getSomeCategories() {
		/* TODO: retrieve some categories to apply to product */
		List<Category> someCategories = new ArrayList<Category>();
		return someCategories;
	}

	private List<Category> getMoreCategories() {
		/* TODO: retrieve some categories to apply to product */
		List<Category> moreCategories = new ArrayList<Category>();
		return moreCategories;
	}
}
