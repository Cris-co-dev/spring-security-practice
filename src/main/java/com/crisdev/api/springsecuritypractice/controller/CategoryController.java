package com.crisdev.api.springsecuritypractice.controller;

import com.crisdev.api.springsecuritypractice.dto.SaveCategory;
import com.crisdev.api.springsecuritypractice.dto.SaveProduct;
import com.crisdev.api.springsecuritypractice.persistence.entity.Category;
import com.crisdev.api.springsecuritypractice.persistence.entity.Product;
import com.crisdev.api.springsecuritypractice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


//casos de uso

    @GetMapping
    public ResponseEntity<Page<Category>> findAll(Pageable pageable) {

        Page<Category> categoriesPage = categoryService.findAll(pageable);

        if (categoriesPage.hasContent()) {
            return ResponseEntity.ok(categoriesPage);
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> findOneById(@PathVariable Long categoryId) {

        Optional<Category> category = categoryService.findOneById(categoryId);

        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> createOne(@RequestBody @Valid SaveCategory saveCategory) {

        Category category = categoryService.createOne(saveCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateOneById(@RequestBody @Valid SaveCategory saveCategory, @PathVariable Long categoryId) {

        Category category = categoryService.updateOneByID(categoryId,saveCategory);

        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}/disabled")
    public ResponseEntity<Category> disableOneById(@PathVariable Long categoryId) {

        Category category = categoryService.disableOneByID(categoryId);

        return ResponseEntity.ok(category);
    }



}
