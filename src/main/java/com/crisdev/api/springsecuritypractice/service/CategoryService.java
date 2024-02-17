package com.crisdev.api.springsecuritypractice.service;

import com.crisdev.api.springsecuritypractice.dto.SaveCategory;
import com.crisdev.api.springsecuritypractice.dto.SaveProduct;
import com.crisdev.api.springsecuritypractice.persistence.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {


    Page<Category> findAll(Pageable pageable);

    Optional<Category> findOneById(Long categoryId);

    Category createOne(SaveCategory saveCategory);

    Category updateOneByID(Long categoryId, SaveCategory saveCategory);

    Category disableOneByID(Long categoryId);
}
