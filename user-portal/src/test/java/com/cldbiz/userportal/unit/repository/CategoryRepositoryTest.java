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
import org.springframework.transaction.annotation.Transactional;

import com.cldbiz.userportal.domain.Category;
import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CategoryDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.category.CategoryRepository;
import com.cldbiz.userportal.repository.product.ProductRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.CategoryData;
import com.cldbiz.userportal.unit.repository.data.ProductData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @DatabaseSetup(value= {"/productData.xml", "/categoryData.xml", "/categoryProductData.xml"})
public class CategoryRepositoryTest extends BaseRepositoryTest {

	private static final Long TOTAL_ROWS = 7L;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		List<Category> categorys = categoryRepository.findAll();
		Category category = categorys.get(0);
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke existsById here
		Boolean exists = categoryRepository.existsById(category.getId());
		categoryRepository.flush();
		
		// check for existence
		assertThat(exists);
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		log.info("whenCountAll_thenReturnCount");
		
		// invoke countAll here
		long categoryCnt = categoryRepository.countAll();
		categoryRepository.flush();
		
		// check count
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnCategory() {
		log.info("whenFindById_thenReturnCategory");
		
		// invoke findById here
		Optional<Category> sameCategory = categoryRepository.findById(3L);
		categoryRepository.flush();
		
		// check for category and related entities  
		assertThat(sameCategory.orElse(null)).isNotNull();
		assertThat(sameCategory.get().getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByIds_thenReturnAllCategories() {
		log.info("whenFindByIds_thenReturnAllCategories");
		
		// get all category ids
		List<Category> categories = categoryRepository.findAll();
		List<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke findByIds here
		categories = categoryRepository.findByIds(categoryIds);
		categoryRepository.flush();
		
		// check all categories were found
		assertThat(categories.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check that some categories have products
		List<Category> categoriesWithProducts = categories.stream().filter(c -> !c.getProducts().isEmpty()).collect(Collectors.toList());
		assertThat(categoriesWithProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenFindAll_thenReturnAllCategories() {
		log.info("whenFindAll_thenReturnAllCategories");
		
		// invoke findAll here
		List<Category> categories = categoryRepository.findAll();
		categoryRepository.flush();
		
		// check all categories returned
		assertThat(categories.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check that some categories have products
		List<Category> categoriesWithProducts = categories.stream().filter(c -> !c.getProducts().isEmpty()).collect(Collectors.toList());
		assertThat(categoriesWithProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteById_thenRemoveCategory() {
		log.info("whenDeleteById_thenRemoveCategory");
		
		Category category = categoryRepository.findById(1L).get();
		category.getProducts().size();
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke deleteById here
		categoryRepository.deleteById(category.getId());
		categoryRepository.flush();
		
		// check category deleted
		List<Category> categories = categoryRepository.findAll();
		assertThat(categories.contains(category)).isFalse();
		
		// check products still exist but not related
		category.getProducts().forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
			assertThat(product.getCategories().contains(category)).isFalse();
		});
	}

	@Test
	public void whenDeleteByIds_thenRemoveCategories() {
		log.info("whenDeleteByIds_thenRemoveCategories");
		
		// get all categories
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(c -> c.getProducts().size());
		
		// get all category ids
		List<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke deleteByIds here
		categoryRepository.deleteByIds(categoryIds);
		categoryRepository.flush();
		
		// check all categories deleted
		long categoryCnt = categoryRepository.countAll();
		assertThat(categoryCnt).isZero();

		// get products from any category
		Set<Product> relatedProducts = categories.stream()
				.flatMap(c -> c.getProducts().stream()).map(p -> p)
				.collect(Collectors.toSet());

		// check products still exist but not related
		relatedProducts.forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
			assertThat(product.getCategories().isEmpty());
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemoveCategory() {
		log.info("whenDeleteByEntity_thenRemoveCategory");
		
		// get category and its products
		Category category = categoryRepository.findById(3L).get(); 
		category.getProducts().size();
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke deleteByEntity here
		categoryRepository.deleteByEntity(category);
		categoryRepository.flush();
		
		// check category deleted
		List<Category> categories = categoryRepository.findAll();
		assertThat(categories.contains(category)).isFalse();

		// check products still exist but not related
		category.getProducts().forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
			assertThat(product.getCategories().contains(category)).isFalse();
		});
	}

	@Test
	public void whenDeleteByEntities_thenRemoveCategories() {
		log.info("whenDeleteByEntities_thenRemoveCategories");
		
		// get all categories
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(c -> c.getProducts().size());
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke deleteByEntities here
		categoryRepository.deleteByEntities(categories);
		categoryRepository.flush();
		
		// check all categories deleted
		long categoryCnt = categoryRepository.countAll();
		assertThat(categoryCnt).isZero();
		
		// get all products from any category
		Set<Product> relatedProducts = categories.stream()
				.flatMap(c -> c.getProducts().stream())
				.filter(p -> productRepository.existsById(p.getId()))
				.collect(Collectors.toSet());

		// check products still exist but not related
		relatedProducts.forEach(product -> {
			assertThat(productRepository.existsById(product.getId()));
			assertThat(product.getCategories().isEmpty());
		});
	}

	@Test
	public void whenSaveEntity_thenReturnSavedCategory() {
		log.info("whenSaveEntity_thenReturnSavedCategory");
		
		//create a new category
		Category anotherCategory = CategoryData.getAnotherCategory();
		
		// Add existing product to new category
		Product anotherProduct = ProductData.getAnotherExistingProduct(); 
		anotherCategory.getProducts().add(anotherProduct);
		anotherProduct.getCategories().add(anotherCategory);
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke saveEntity here
		Category savedCategory = categoryRepository.saveEntity(anotherCategory);
		categoryRepository.flush();
		
		// confirm persisted category returned
		assertThat(savedCategory.equals(anotherCategory));
		
		// confirmed category persisted
		long categoryCnt = categoryRepository.countAll();
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// retrieve saved category from store
		Optional<Category> rtrvCategory = categoryRepository.findById(savedCategory.getId());
		assertThat(rtrvCategory.orElse(null)).isNotNull();
		
		// check retrieved category matches saved category
		assertThat(rtrvCategory.get().equals(anotherCategory));
		assertThat(rtrvCategory.get().equals(savedCategory));
		
		// check persisted category products match saved category products
		assertThat(Boolean.FALSE.equals(rtrvCategory.get().getProducts().isEmpty()));
		assertThat(rtrvCategory.get().getProducts().stream().allMatch(p -> anotherCategory.getProducts().contains(p)));
		assertThat(anotherCategory.getProducts().stream().allMatch(p -> rtrvCategory.get().getProducts().contains(p)));
	}

	@Test
	public void whenSaveEntities_thenReturnSavedCategories() {
		log.info("whenSaveEntities_thenReturnSavedCategories");
	
		// create 1st new category
		Category anotherCategory = CategoryData.getAnotherCategory();
		
		// add product to 1st category
		Product anotherProduct = ProductData.getAnotherExistingProduct(); 
		anotherCategory.getProducts().add(anotherProduct);
		anotherProduct.getCategories().add(anotherCategory);
		
		// create 2nd new category
		Category extraCategory = CategoryData.getExtraCategory();
		
		// add product to 2nd category
		Product extraProduct = ProductData.getExtraExistingProduct(); 
		extraCategory.getProducts().add(extraProduct);
		extraProduct.getCategories().add(extraCategory);

		// create list of new categories
		List<Category> categories = new ArrayList<Category>();
		categories.add(anotherCategory);
		categories.add(extraCategory);
		
		// clear cache to test performance
		categoryRepository.flush();

		// invoke saveEntities here
		List<Category> savedCategorys = categoryRepository.saveEntities(categories);
		categoryRepository.flush();
		
		// confirm new categories returned
		assertThat(savedCategorys.size() == 2);
		
		// check returned categories match new categories
		assertThat(categories.stream().allMatch(t -> savedCategorys.contains(t)));
		assertThat(savedCategorys.stream().allMatch(t -> categories.contains(t)));
		
		// confirm new categories persisted
		long categoryCnt = categoryRepository.countAll();
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// retrieve 1st new category
		Optional<Category> rtrvAnotherCategory = categoryRepository.findById(anotherCategory.getId());
		assertThat(rtrvAnotherCategory.orElse(null)).isNotNull();
		
		// check retrieved category equals 1st category and contains its product
		assertThat(rtrvAnotherCategory.get().equals(anotherCategory));
		assertThat(rtrvAnotherCategory.get().getProducts().contains(anotherProduct));
		
		// retrieve 2nd new category
		Optional<Category> rtrvExtaCategory = categoryRepository.findById(extraCategory.getId());
		assertThat(rtrvExtaCategory.orElse(null)).isNotNull();
		
		// check retrieved category equals 2nd category and contains its product
		assertThat(rtrvExtaCategory.get().equals(extraCategory));
		assertThat(rtrvExtaCategory.get().getProducts().contains(extraProduct));
	}

	@Test
	public void whenModified_thenCategoryUpdated() {
		log.info("whenModified_thenCategoryUpdated");
		
		// retrieve category
		Optional<Category> originalCategory = categoryRepository.findById(3L);
		
		// update category and related entities
		originalCategory.get().setName("UPDATED - " + originalCategory.get().getName());
		originalCategory.get().getProducts().forEach(p -> p.setName("UPDATED - " + p.getName()));
		
		// retrieve category again 
		Optional<Category> rtrvdCategory = categoryRepository.findById(3L);
		
		// check category and related entities updated without save
		assertThat(originalCategory.get().getName().equals((rtrvdCategory.get().getName())));
		assertThat(originalCategory.get().getProducts().stream().allMatch(t -> rtrvdCategory.get().getProducts().contains(t)));
		assertThat(rtrvdCategory.get().getProducts().stream().allMatch(t -> originalCategory.get().getProducts().contains(t)));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// create category dto for search criteria
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("pen");
		categoryDto.setProductDto(productDto);

		// invoke existsByDto here
		Boolean exists = categoryRepository.existsByDto(categoryDto);
		categoryRepository.flush();
		
		// check existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// create category dto for search criteria
		CategoryDto categoryDto = new CategoryDto();
		
		ProductDto productDto = new ProductDto();
		productDto.setName("printer");
		categoryDto.setProductDto(productDto);
		
		// invoke countByDto here
		long categoryCnt =  categoryRepository.countByDto(categoryDto);
		categoryRepository.flush();
		
		// check count
		assertThat(categoryCnt).isGreaterThanOrEqualTo(2L);
	}

	@Test
	public void whenFindByDto_thenReturnCategories() {
		log.info("whenFindByDto_thenReturnCategories");
		
		// create category dto for search criteria
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		categoryDto.setProductDto(productDto);
		
		// invoke findByDto here
		List<Category> categories = categoryRepository.findByDto(categoryDto);
		categoryRepository.flush();
		
		// check categories found
		assertThat(categories).isNotEmpty();
		
		// check results match criteria
		categories.forEach(catgory -> {
			assertThat(catgory.getName().equals(categoryDto.getName()));
			assertThat(catgory.getProducts().contains(product));
		});
	}

	@Test
	public void whenFindPageByDto_thenReturnCategories() {
		log.info("whenFindPageByDto_thenReturnCategories");
		
		// create category dto for search criteria
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		categoryDto.setProductDto(productDto);
		
		// limit to first two categories
		categoryDto.setStart(0);
		categoryDto.setLimit(2);
		
		// invoke findPageByDto here
		List<Category> categories = categoryRepository.findPageByDto(categoryDto);
		categoryRepository.flush();
		
		// assert no more than 2 records found
		assertThat(categories).isNotEmpty();
		assertThat(categories.size()).isLessThanOrEqualTo(2);
		
		// check results match criteria
		categories.forEach(catgory -> {
			assertThat(catgory.getName().equals(categoryDto.getName()));
			assertThat(catgory.getProducts().contains(product));
		});
	}

	@Test
	public void whenSearchByDto_thenReturnCategories() {
		log.info("whenSearchByDto_thenReturnCategories");
		
		// create category dto for search criteria
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		categoryDto.setProductDto(productDto);
		
		// invoke searchByDto here
		List<Category> categories = categoryRepository.searchByDto(categoryDto);
		categoryRepository.flush();
		
		// check categories found
		assertThat(categories).isNotEmpty();
		
		// check results match criteria
		categories.forEach(category -> {
			assertThat(category.getName().equals(categoryDto.getName()));
			assertThat(category.getProducts().stream().map(c -> c.getName())
				.collect(Collectors.toList()).contains(productDto.getName()));
		});
	}

	@Test
	public void whenSearchPageByDto_thenReturnCategories() {
		log.info("whenSearchPageByDto_thenReturnCategories");

		// create category dto for search criteria
		CategoryDto categoryDto = new CategoryDto();
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		categoryDto.setProductDto(productDto);

		// limit to first two categories
		categoryDto.setStart(0);
		categoryDto.setLimit(2);
		
		// invoke searchPageByDto here
		List<Category> categories = categoryRepository.searchPageByDto(categoryDto);
		categoryRepository.flush();
		
		// assert no more than 2 records found
		assertThat(categories).isNotEmpty();
		assertThat(categories.size()).isLessThanOrEqualTo(2);
		
		// check results match criteria
		categories.forEach(category -> {
			assertThat(category.getProducts().stream().map(c -> c.getName())
				.collect(Collectors.toList()).contains(productDto.getName()));
		});
	}
}
