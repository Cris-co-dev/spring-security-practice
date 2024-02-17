package com.crisdev.api.springsecuritypractice.service.impl;

import com.crisdev.api.springsecuritypractice.dto.SaveCategory;
import com.crisdev.api.springsecuritypractice.dto.SaveProduct;
import com.crisdev.api.springsecuritypractice.exception.ObjectNotFoundException;
import com.crisdev.api.springsecuritypractice.persistence.entity.Category;
import com.crisdev.api.springsecuritypractice.persistence.entity.Product;
import com.crisdev.api.springsecuritypractice.persistence.repository.CategoryRespository;
import com.crisdev.api.springsecuritypractice.persistence.repository.ProductRepository;
import com.crisdev.api.springsecuritypractice.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.support.incrementer.SybaseAnywhereMaxValueIncrementer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRespository categoryRespository;

    public CategoryServiceImpl(CategoryRespository categoryRespository) {
        this.categoryRespository = categoryRespository;
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRespository.findAll(pageable);
    }

    @Override
    public Optional<Category> findOneById(Long categoryId) {
        return categoryRespository.findById(categoryId);
    }

    @Override
    public Category createOne(SaveCategory saveCategory) {

        Category category = new Category();

        category.setName(saveCategory.getName());
        category.setStatus(Category.CategoryStatus.ENABLED);

        return categoryRespository.save(category);
    }


    @Override
    public Category updateOneByID(Long categoryId, SaveCategory saveCategory) {
        Category categoryFromDB = categoryRespository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + categoryId));

        categoryFromDB.setName(saveCategory.getName());
        categoryFromDB.setStatus(Category.CategoryStatus.ENABLED);

        return categoryRespository.save(categoryFromDB);
    }

    @Override
    public Category disableOneByID(Long categoryId) {
        Category categoryFromDB = categoryRespository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + categoryId));
        categoryFromDB.setStatus(Category.CategoryStatus.DISABLED);
        return categoryRespository.save(categoryFromDB);
    }
}
