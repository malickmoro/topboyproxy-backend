/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.repository;

import com.theplutushome.topboy.entity.ProxyOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author plutus
 */
public interface ProxyOrderRepository extends JpaRepository<ProxyOrder, Long> {
    Optional<ProxyOrder> findByClientReference(String clientReference);
}
