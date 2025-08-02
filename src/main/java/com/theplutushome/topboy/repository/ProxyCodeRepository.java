/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.repository;

import com.theplutushome.topboy.entity.CodeCategory;
import com.theplutushome.topboy.entity.ProxyCode;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author plutus
 */
public interface ProxyCodeRepository extends JpaRepository<ProxyCode, Long> {

    boolean existsByCode(String code);

    int countByCategoryAndSoldFalse(CodeCategory category); // ✅ Add this line

    @Query("SELECT p FROM ProxyCode p WHERE p.category = :category AND p.sold = false ORDER BY p.id ASC")
    List<ProxyCode> findAvailableByCategoryLimited(@Param("category") CodeCategory category, Pageable pageable);
}
