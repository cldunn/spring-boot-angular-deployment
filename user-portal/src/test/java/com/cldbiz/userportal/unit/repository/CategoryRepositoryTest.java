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
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/productData.xml", "/categoryData.xml", "/categoryProductData.xml"})
public class CategoryRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Category> categorys = categoryRepository.findAll();
		Category category = categorys.get(0);
		
		Boolean exists = categoryRepository.existsById(category.getId());
		categoryRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		long categoryCnt = categoryRepository.countAll();
		categoryRepository.flush();
		
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnCategory() {
		Optional<Category> sameCategory = categoryRepository.findById(3L);
		categoryRepository.flush();
		
		assertThat(sameCategory.orElse(null)).isNotNull();
		assertThat(sameCategory.get().getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByIds_thenReturnAllCategorys() {
		List<Category> categorys = categoryRepository.findAll();
		List<Long> categoryIds = categorys.stream().map(Category::getId).collect(Collectors.toList());
		
		categorys = categoryRepository.findByIds(categoryIds);
		categoryRepository.flush();
		
		assertThat(categorys.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(categorys.get(0).getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindAll_thenReturnAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		categoryRepository.flush();
		
		assertThat(categories.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		Optional<Category> category = categories.stream()
			.filter(c -> c.getProducts().size() > 0)
			.findFirst();
		assertThat(category.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteById_thenRemoveCategory() {
		List<Category> categorys = categoryRepository.findAll();

		Category category = categorys.get(0);
		category.getProducts().size();
		
		categoryRepository.deleteById(category.getId());
		categoryRepository.flush();
		
		categorys = categoryRepository.findAll();

		assertThat(categorys.contains(category)).isFalse();
		
		List<Product> deletedProducts = category.getProducts().stream()
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.size()).isEqualTo(category.getProducts().size());
	}

	@Test
	public void whenDeleteByIds_thenRemoveCategories() {
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(c -> c.getProducts().size());
		
		List<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
		
		categoryRepository.deleteByIds(categoryIds);
		categoryRepository.flush();
		
		long categoryCnt = categoryRepository.countAll();

		assertThat(categoryCnt).isZero();

		List<Product> deletedProducts = categories.stream()
				.flatMap(c -> c.getProducts().stream())
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteByEntity_thenRemoveCategory() {
		List<Category> categories = categoryRepository.findAll();
		Category category = categories.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		category.getProducts().size();
		
		categoryRepository.deleteByEntity(category);
		categoryRepository.flush();
		
		categories = categoryRepository.findAll();
		
		assertThat(categories.contains(category)).isFalse();

		List<Product> deletedProducts = category.getProducts().stream()
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteByEntities_thenRemoveCategories() {
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(c -> c.getProducts().size());
		
		categoryRepository.deleteByEntities(categories);
		categoryRepository.flush();
		
		long categoryCnt = categoryRepository.countAll();

		assertThat(categoryCnt).isZero();
		
		List<Product> deletedProducts = categories.stream()
			.flatMap(c -> c.getProducts().stream())
			.filter(p -> productRepository.findById(p.getId()) != null )
			.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenSaveEntity_thenReturnSavedCategory() {
		Category anotherCategory = getAnotherCategory();
		Product anotherProduct = getAnotherProduct(); 
		
		anotherCategory.getProducts().add(anotherProduct);
		anotherProduct.getCategories().add(anotherCategory);
		
		Category savedCategory = categoryRepository.saveEntity(anotherCategory);
		categoryRepository.flush();
		
		assertThat(savedCategory.equals(anotherCategory)).isTrue();
		
		long categoryCnt = categoryRepository.countAll();
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Category> rtrvCategory = categoryRepository.findById(savedCategory.getId());
		assertThat(rtrvCategory.orElse(null)).isNotNull();
		assertThat(rtrvCategory.get().equals(anotherCategory)).isTrue();
		assertThat(rtrvCategory.get().equals(savedCategory)).isTrue();
		
		assertThat(savedCategory.getProducts().contains(anotherProduct));
		
		Optional<Product> savedProduct = savedCategory.getProducts().stream().filter(p -> p.getId().equals(anotherProduct.getId())).findFirst();
		assertThat(savedProduct.get().getCategories().contains(anotherCategory));
	}

	@Test
	public void whenSaveEntities_thenReturnSavedCategories() {
		Category anotherCategory = getAnotherCategory();
		Product anotherProduct = getAnotherProduct(); 

		anotherCategory.getProducts().add(anotherProduct);
		anotherProduct.getCategories().add(anotherCategory);
		
		Category extraCategory = getExtraCategory();
		Product extraProduct = getExtraProduct(); 

		extraCategory.getProducts().add(extraProduct);
		extraProduct.getCategories().add(extraCategory);

		List<Category> categories = new ArrayList<Category>();
		categories.add(anotherCategory);
		categories.add(extraCategory);
		
		List<Category> savedCategorys = categoryRepository.saveEntities(categories);
		categoryRepository.flush();
		
		assertThat(savedCategorys.size() == 2).isTrue();
		
		assertThat(categories.stream().allMatch(t -> savedCategorys.contains(t))).isTrue();
		assertThat(savedCategorys.stream().allMatch(t -> categories.contains(t))).isTrue();
		
		long categoryCnt = categoryRepository.countAll();
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<Category> rtrvAnotherCategory = categoryRepository.findById(anotherCategory.getId());
		assertThat(rtrvAnotherCategory.orElse(null)).isNotNull();
		assertThat(rtrvAnotherCategory.get().equals(anotherCategory)).isTrue();
		assertThat(rtrvAnotherCategory.get().getProducts().contains(anotherProduct));
		
		Optional<Category> rtrvExtaCategory = categoryRepository.findById(extraCategory.getId());
		assertThat(rtrvExtaCategory.orElse(null)).isNotNull();
		assertThat(rtrvExtaCategory.get().equals(extraCategory)).isTrue();
		assertThat(rtrvExtaCategory.get().getProducts().contains(extraProduct));
	}

	@Test
	public void whenModified_thenCategoryUpdated() {
		Optional<Category> originalCategory = categoryRepository.findById(3L);
		originalCategory.get().setName("UPDATED - " + originalCategory.get().getName());
		originalCategory.get().getProducts().forEach(p -> p.setName("UPDATED - " + p.getName()));
		
		Optional<Category> rtrvdCategory = categoryRepository.findById(3L);
		assertThat(originalCategory.get().getName().equals((rtrvdCategory.get().getName())));
		rtrvdCategory.get().getProducts().forEach(p -> assertThat(originalCategory.get().getProducts().contains(p)));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("pen");
		
		categoryDto.setProductDto(productDto);

		Boolean exists = categoryRepository.existsByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		CategoryDto categoryDto = new CategoryDto();
		
		ProductDto productDto = new ProductDto();
		productDto.setName("printer");
		
		categoryDto.setProductDto(productDto);
		
		long categoryCnt =  categoryRepository.countByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categoryCnt).isGreaterThanOrEqualTo(2L);
	}

	@Test
	public void whenFindByDto_thenReturnCategories() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		categoryDto.setProductDto(productDto);
		
		List<Category> categorys = categoryRepository.findByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categorys).isNotEmpty();
		
		Optional<Category> category = categorys.stream().findFirst();
		
		assertThat(category.orElse(null)).isNotNull();
		assertThat(category.get().getName().equals("Writing")).isTrue();
		assertThat(category.get().getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindPageByDto_thenReturnCategories() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		categoryDto.setProductDto(productDto);
		
		categoryDto.setStart(0);
		categoryDto.setLimit(2);
		
		List<Category> categories = categoryRepository.findPageByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categories).isNotEmpty();
		assertThat(categories.size()).isLessThanOrEqualTo(2);
		assertThat(categories.get(0).getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenSearchByDto_thenReturnCategorys() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		categoryDto.setProductDto(productDto);
		
		List<Category> categories = categoryRepository.searchByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categories).isNotEmpty();
		assertThat(categories.get(0).getProducts().stream().map(p -> p.getName()).collect(Collectors.toList()).contains("Stationary"));
	}

	@Test
	public void whenSearchPageByDto_thenReturnCategorys() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setStart(0);
		categoryDto.setLimit(2);
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		categoryDto.setProductDto(productDto);

		List<Category> categories = categoryRepository.searchPageByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categories).isNotEmpty();
		assertThat(categories.size()).isLessThanOrEqualTo(2);
		assertThat(categories.get(0).getProducts().isEmpty()).isFalse();
	}

	private Category getAnotherCategory() {
		Category category = new Category();
		
		category.setName("Binders");
		
		return category;
	}
	
	private Category getExtraCategory() {
		Category category = new Category();
		
		category.setName("Note Books");
		
		return category;
	}

	private Product getAnotherProduct() {
		Optional<Product> product = productRepository.findById(1L);
		return product.orElse(null);
	}

	private Product getExtraProduct() {
		Optional<Product> product = productRepository.findById(2L);
		return product.orElse(null);
	}

}
