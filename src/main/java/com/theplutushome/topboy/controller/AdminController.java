/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.controller;

import com.theplutushome.topboy.dto.CreateAdminUserRequest;
import com.theplutushome.topboy.dto.LoginRequest;
import com.theplutushome.topboy.dto.LoginResponse;
import com.theplutushome.topboy.dto.ProxyCodeDTO;
import com.theplutushome.topboy.dto.UploadResult;
import com.theplutushome.topboy.entity.AdminUser;
import com.theplutushome.topboy.entity.CodeCategory;
import com.theplutushome.topboy.entity.ProxyPriceConfig;
import com.theplutushome.topboy.repository.AdminUserRepository;
import com.theplutushome.topboy.repository.ProxyCodeRepository;
import com.theplutushome.topboy.repository.ProxyPriceConfigRepository;
import com.theplutushome.topboy.service.FileUploadService;
import com.theplutushome.topboy.util.JwtUtil;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author plutus
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final FileUploadService fileUploadService;
    private final ProxyPriceConfigRepository proxyPriceConfigRepository;
    private final ProxyCodeRepository proxyCodeRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Login attempt: {}", request.getUsername());
        AdminUser user = adminUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        log.info("The found user's role is: {}", user.getRole());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        log.info("The found user has a correct password");

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PutMapping("/price")
    public ResponseEntity<?> updatePrice(
            @RequestParam CodeCategory category,
            @RequestParam int newPrice
    ) {
        ProxyPriceConfig config = proxyPriceConfigRepository
                .findByCategory(category)
                .orElseGet(() -> {
                    ProxyPriceConfig newConfig = new ProxyPriceConfig();
                    newConfig.setCategory(category);
                    return newConfig;
                });

        config.setPrice(newPrice);
        proxyPriceConfigRepository.save(config);

        return ResponseEntity.ok("Price for " + category + " updated to " + newPrice + " GHS");
    }

    @GetMapping("/prices")
    public ResponseEntity<List<ProxyPriceConfig>> getAllPrices() {
        return ResponseEntity.ok(proxyPriceConfigRepository.findAll());
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResult> uploadCodes(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") CodeCategory category
    ) {
        try {
            UploadResult result = fileUploadService.processFile(file, category);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new UploadResult(0, List.of("Error: " + e.getMessage()), 0));
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createAdminUser(@RequestBody @Valid CreateAdminUserRequest request) {
        if (adminUserRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        AdminUser newUser = new AdminUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("ROLE_ADMIN"); // or any role you're using

        adminUserRepository.save(newUser);

        return ResponseEntity.ok("Admin user created successfully");
    }

    @GetMapping("/codes")
    public ResponseEntity<List<ProxyCodeDTO>> getAllCodes() {
        List<ProxyCodeDTO> result = proxyCodeRepository.findAll().stream()
                .map(code -> new ProxyCodeDTO(
                code.getId(),
                code.getCode(),
                code.getCategory(),
                code.getCreatedAt(),
                code.isSold(),
                code.getSoldAt()
        ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CodeCategory>> getAvailableCategories() {
        return ResponseEntity.ok(Arrays.asList(CodeCategory.values()));
    }

}
