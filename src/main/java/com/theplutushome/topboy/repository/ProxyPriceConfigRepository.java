/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.repository;

import com.theplutushome.topboy.entity.CodeCategory;
import com.theplutushome.topboy.entity.ProxyPriceConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author plutus
 */
public interface ProxyPriceConfigRepository extends JpaRepository<ProxyPriceConfig, Long> {
    Optional<ProxyPriceConfig> findByCategory(CodeCategory category);
}
