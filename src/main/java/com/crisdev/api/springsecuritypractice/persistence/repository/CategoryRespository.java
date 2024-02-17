package com.crisdev.api.springsecuritypractice.persistence.repository;

import com.crisdev.api.springsecuritypractice.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRespository extends JpaRepository<Category, Long> {
}
