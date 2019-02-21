package com.cldbiz.userportal.hibernate;

import java.io.Reader;
import java.util.Arrays;

import org.hibernate.tool.hbm2ddl.ImportSqlCommandExtractor;

public class CustomHibernateSqlExtractor implements ImportSqlCommandExtractor {
    private static final String STATEMENT_DELIMITER = "/;";

    @Override
    public String[] extractCommands(Reader reader) {
        try {
            int charVal;
            String str = "";
            while ((charVal = reader.read()) != -1) {
                str += (char) charVal;
            }
            reader.close();

            String[] split = str.split(STATEMENT_DELIMITER);
            String[] statements = Arrays.stream(split)
                                        .map(String::trim)
                                        .filter(s -> s.length() > 0)
                                        .map(s -> s += ";")
                                        .toArray(String[]::new);
            return statements;

        } catch (Exception e) {
            throw new RuntimeException("Error during import script parsing.", e);
        }
    }
}