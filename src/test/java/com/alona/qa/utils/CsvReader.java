package com.alona.qa.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CsvReader {
    private CsvReader() {
    }

    public static List<String[]> readCsvFromResources(String resourcePath) {
        InputStream inputStream = CsvReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found: " + resourcePath);
        }

        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (!line.isBlank()) {
                    rows.add(line.split(","));
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Unable to read CSV file: " + resourcePath, exception);
        }
        return rows;
    }
}
