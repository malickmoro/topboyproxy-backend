package com.theplutushome.topboy.repository;

import com.theplutushome.topboy.entity.SaleLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author plutus
 */
public interface SaleLogRepository extends JpaRepository<SaleLog, Long> {
    List<SaleLog> findByPhoneNumber(String phoneNumber);
}
