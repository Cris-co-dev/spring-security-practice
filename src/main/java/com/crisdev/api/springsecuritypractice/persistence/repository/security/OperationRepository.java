package com.crisdev.api.springsecuritypractice.persistence.repository.security;

import com.crisdev.api.springsecuritypractice.persistence.entity.security.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("select o from Operation o where o.permitAll = true")
    List<Operation> findByPublicAccess();
}
