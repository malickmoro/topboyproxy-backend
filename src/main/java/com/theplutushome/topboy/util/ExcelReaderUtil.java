/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.util;

import com.theplutushome.topboy.dto.ParsedCodesResult;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author plutus
 */
public class ExcelReaderUtil {

    public static ParsedCodesResult extractCodes(InputStream input) throws Exception {
        List<String> codes = new ArrayList<>();
        int blankLines = 0;

        try (Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell == null || cell.toString().trim().isEmpty()) {
                    blankLines++;
                    continue;
                }
                codes.add(cell.toString().trim());
            }
        }

        return new ParsedCodesResult(codes, blankLines);
    }
}