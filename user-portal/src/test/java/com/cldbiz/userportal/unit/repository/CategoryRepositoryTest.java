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
import com.cldbiz.userportal.dto.CategoryDto;
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
	public void whenCount_thenReturnCount() {
		long categoryCnt = categoryRepository.count();
		categoryRepository.flush();
		
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemoveCategory() {
		List<Category> categories = categoryRepository.findAll();
		Category category = categories.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		category.getProducts().size();
		
		categoryRepository.delete(category);
		categoryRepository.flush();
		
		categories = categoryRepository.findAll();
		
		assertThat(categories.contains(category)).isFalse();

		List<Product> deletedProducts = category.getProducts().stream()
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteAll_thenRemoveAllCategorys() {
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(c -> c.getProducts().size());
		
		categoryRepository.deleteAll(categories);
		categoryRepository.flush();
		
		long categoryCnt = categoryRepository.count();

		assertThat(categoryCnt).isZero();
		
		List<Product> deletedProducts = categories.stream()
			.flatMap(c -> c.getProducts().stream())
			.filter(p -> productRepository.findById(p.getId()) != null )
			.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllCategorys() {
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(c -> c.getProducts().size());
		
		List<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
		
		categoryRepository.deleteByIds(categoryIds);
		categoryRepository.flush();
		
		long categoryCnt = categoryRepository.count();

		assertThat(categoryCnt).isZero();

		List<Product> deletedProducts = categories.stream()
				.flatMap(c -> c.getProducts().stream())
				.filter(p -> productRepository.findById(p.getId()) != null )
				.collect(Collectors.toList());

		assertThat(deletedProducts.isEmpty()).isFalse();
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
	public void whenModified_thenCategoryUpdated() {
		Optional<Category> originalCategory = categoryRepository.findById(3L);
		originalCategory.get().setName("UPDATED - " + originalCategory.get().getName());
		originalCategory.get().getProducts().forEach(p -> p.setName("UPDATED - " + p.getName()));
		
		Optional<Category> rtrvdCategory = categoryRepository.findById(3L);
		assertThat(originalCategory.get().getName().equals((rtrvdCategory.get().getName())));
		rtrvdCategory.get().getProducts().forEach(p -> assertThat(originalCategory.get().getProducts().contains(p)));
	}

	@Test
	public void whenSave_thenReturnSavedCategory() {
		Category anotherCategory = getAnotherCategory();
		Product anotherProduct = getAnotherProduct(); 
		
		anotherCategory.getProducts().add(anotherProduct);
		anotherProduct.getCategories().add(anotherCategory);
		
		Category savedCategory = categoryRepository.save(anotherCategory);
		categoryRepository.flush();
		
		assertThat(savedCategory.equals(anotherCategory)).isTrue();
		
		long categoryCnt = categoryRepository.count();
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Category> rtrvCategory = categoryRepository.findById(savedCategory.getId());
		assertThat(rtrvCategory.orElse(null)).isNotNull();
		assertThat(rtrvCategory.get().equals(anotherCategory)).isTrue();
		assertThat(rtrvCategory.get().equals(savedCategory)).isTrue();
		
		assertThat(savedCategory.getProducts().contains(anotherProduct));
	}

	@Test
	public void whenSaveAndFlush_thenReturnSavedCategory() {
		Category anotherCategory = getAnotherCategory();
		Product anotherProduct = getAnotherProduct(); 
		
		anotherCategory.getProducts().add(anotherProduct);
		anotherProduct.getCategories().add(anotherCategory);
		
		Category savedCategory = categoryRepository.saveAndFlush(anotherCategory);
		
		assertThat(savedCategory.equals(anotherCategory)).isTrue();
		
		long categoryCnt = categoryRepository.count();
		assertThat(categoryCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Category> rtrvCategory = categoryRepository.findById(savedCategory.getId());
		assertThat(rtrvCategory.orElse(null)).isNotNull();
		assertThat(rtrvCategory.get().equals(anotherCategory)).isTrue();
		assertThat(rtrvCategory.get().equals(savedCategory)).isTrue();
		assertThat(rtrvCategory.get().getProducts().contains(anotherProduct));
	}

	@Test
	public void whenSaveAll_thenReturnSavedCategoriess() {
		Category anotherCategory = getAnotherCategory();
		Product anotherProduct = getAnotherProduct(); 

		anotherCategory.getProducts().add(anotherProduct);
		anotherProduct.getCategories().add(anotherCategory);
		
		Category extraCategory = getExtraCategory();
		Product extraProduct = getExtraProduct(); 

		extraCategory.getProducts().add(extraProduct);
		extraProduct.getCategories().add(extraCategory);

		List<Category> categorys = new ArrayList<Category>();
		categorys.add(anotherCategory);
		categorys.add(extraCategory);
		
		List<Category> savedCategorys = categoryRepository.saveAll(categorys);
		categoryRepository.flush();
		
		assertThat(savedCategorys.size() == 2).isTrue();
		
		assertThat(categorys.stream().allMatch(t -> savedCategorys.contains(t))).isTrue();
		assertThat(savedCategorys.stream().allMatch(t -> categorys.contains(t))).isTrue();
		
		long categoryCnt = categoryRepository.count();
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
	public void whenExistsById_thenReturnTrue() {
		List<Category> categorys = categoryRepository.findAll();
		Category category = categorys.get(0);
		
		Boolean exists = categoryRepository.existsById(category.getId());
		categoryRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenFindById_thenReturnCategory() {
		Optional<Category> sameCategory = categoryRepository.findById(3L);
		categoryRepository.flush();
		
		assertThat(sameCategory.orElse(null)).isNotNull();
		assertThat(sameCategory.get().getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindAll_thenReturnAllCategorys() {
		List<Category> categorys = categoryRepository.findAll();
		categoryRepository.flush();
		
		assertThat(categorys.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(categorys.get(0).getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindAllById_thenReturnAllCategorys() {
		List<Category> categorys = categoryRepository.findAll();
		List<Long> categoryIds = categorys.stream().map(Category::getId).collect(Collectors.toList());
		
		categorys = categoryRepository.findAllById(categoryIds);
		categoryRepository.flush();
		
		assertThat(categorys.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(categorys.get(0).getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByDto_thenReturnCategorys() {
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
	public void whenFindPageByDto_thenReturnCategorys() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		categoryDto.setProductDto(productDto);
		
		categoryDto.setStart(0);
		categoryDto.setLimit(2);
		
		List<Category> categorys = categoryRepository.findPageByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categorys).isNotEmpty();
		assertThat(categorys.size()).isLessThanOrEqualTo(2);
		assertThat(categorys.get(0).getProducts().isEmpty()).isFalse();
	}

	@Test
	public void whenCountSearchByDto_thenReturnCount() {
		CategoryDto categoryDto = new CategoryDto();
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		categoryDto.setProductDto(productDto);
		
		Long categoryCount = categoryRepository.countSearchByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categoryCount).isGreaterThanOrEqualTo(2L);
	}

	@Test
	public void whenSearchByDto_thenReturnCategorys() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName("Writing");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		categoryDto.setProductDto(productDto);
		
		List<Category> categorys = categoryRepository.searchByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categorys).isNotEmpty();
		assertThat(categorys.get(0).getProducts().get(0).equals("Stationary"));
	}

	@Test
	public void whenSearchPageByDto_thenReturnCategorys() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setStart(0);
		categoryDto.setLimit(2);
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Stationary");
		categoryDto.setProductDto(productDto);

		List<Category> categorys = categoryRepository.searchPageByDto(categoryDto);
		categoryRepository.flush();
		
		assertThat(categorys).isNotEmpty();
		assertThat(categorys.size()).isLessThanOrEqualTo(2);
		assertThat(categorys.get(0).getProducts().isEmpty()).isFalse();

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