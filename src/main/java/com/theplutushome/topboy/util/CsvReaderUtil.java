/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.util;

import com.opencsv.CSVReader;
import com.theplutushome.topboy.dto.ParsedCodesResult;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author plutus
 */
public class CsvReaderUtil {

    public static ParsedCodesResult extractCodes(InputStream input) throws Exception {
        List<String> codes = new ArrayList<>();
        int blankLines = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(input))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 0 || nextLine[0].trim().isEmpty()) {
                    blankLines++;
                    continue;
                }
                codes.add(nextLine[0].trim());
            }
        }

        return new ParsedCodesResult(codes, blankLines);
    }
}
