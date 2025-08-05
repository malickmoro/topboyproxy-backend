package com.theplutushome.topboy.service;

import com.theplutushome.topboy.dto.ParsedCodesResult;
import com.theplutushome.topboy.dto.UploadResult;
import com.theplutushome.topboy.entity.enums.CodeCategory;
import com.theplutushome.topboy.entity.ProxyCode;
import com.theplutushome.topboy.repository.ProxyCodeRepository;
import com.theplutushome.topboy.util.CsvReaderUtil;
import com.theplutushome.topboy.util.ExcelReaderUtil;
import com.theplutushome.topboy.util.TxtReaderUtil;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author plutus
 */
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final ProxyCodeRepository proxyCodeRepository;

    public UploadResult processFile(MultipartFile file, CodeCategory category) throws Exception {
        ParsedCodesResult parsed;

        String fileName = file.getOriginalFilename().toLowerCase();

        if (fileName.endsWith(".csv")) {
            parsed = CsvReaderUtil.extractCodes(file.getInputStream());
        } else if (fileName.endsWith(".xlsx")) {
            parsed = ExcelReaderUtil.extractCodes(file.getInputStream());
        } else if (fileName.endsWith(".txt")) {
            parsed = TxtReaderUtil.extractCodes(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Unsupported file format.");
        }

        Set<String> uniqueCodes = new LinkedHashSet<>(parsed.getCodes());
        int uploadedCount = 0;
        List<String> duplicates = new ArrayList<>();

        for (String code : uniqueCodes) {
            if (proxyCodeRepository.existsByCode(code)) {
                duplicates.add(code);
                continue;
            }

            ProxyCode entity = new ProxyCode();
            entity.setCode(code);
            entity.setCategory(category);
            entity.setSold(false);

            proxyCodeRepository.save(entity);
            uploadedCount++;
        }

        return new UploadResult(uploadedCount, duplicates, parsed.getBlankLines());
    }
}
