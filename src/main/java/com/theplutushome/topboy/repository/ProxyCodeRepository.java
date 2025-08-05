/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.repository;

import com.theplutushome.topboy.entity.enums.CodeCategory;
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

    // ✅ Count only active and unsold codes
    int countByCategoryAndSoldFalseAndArchivedFalse(CodeCategory category);

    List<ProxyCode> findAllByArchivedFalseOrderByIdAsc();

    // ✅ Find available (unsold + not archived) codes
    @Query("SELECT p FROM ProxyCode p WHERE p.category = :category AND p.sold = false AND p.archived = false ORDER BY p.id ASC")
    List<ProxyCode> findAvailableByCategoryLimited(@Param("category") CodeCategory category, Pageable pageable);
}
