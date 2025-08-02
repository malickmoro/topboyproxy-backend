/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.util;

import com.theplutushome.topboy.dto.ParsedCodesResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author plutus
 */
public class TxtReaderUtil {
    public static ParsedCodesResult extractCodes(InputStream input) throws IOException {
        List<String> codes = new ArrayList<>();
        int blankLines = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    blankLines++;
                    continue;
                }
                codes.add(line.trim());
            }
        }

        return new ParsedCodesResult(codes, blankLines);
    }
}
