package com.cldbiz.userportal.unit.repository.data;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cldbiz.userportal.domain.Category;
import com.cldbiz.userportal.repository.category.CategoryRepository;
import com.cldbiz.userportal.repository.product.ProductRepository;

@Component
public class CategoryData {
	private static CategoryRepository categoryRepository;

	@Autowired
	public CategoryData(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public static Category getAnotherCategory() {
		Category category = new Category();
		
		category.setName("Binders");
		
		return category;
	}
	
	public static Category getExtraCategory() {
		Category category = new Category();
		
		category.setName("Note Books");
		
		return category;
	}

	public static Set<Category> getSomeExistingCategories() {
		Set<Category> someCategories = new HashSet<Category>();
		Category category = categoryRepository.findById(1L).get();
		
		someCategories.add(category);
		
		return someCategories;
	}

	public static Set<Category> getMoreExistingCategories() {
		Set<Category> moreCategories = new HashSet<Category>();
		Category category = categoryRepository.findById(3L).get();
		
		moreCategories.add(category);
		
		return moreCategories;
	}
}
