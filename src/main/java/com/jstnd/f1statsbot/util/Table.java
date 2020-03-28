package com.jstnd.f1statsbot.util;

import java.util.*;

public class Table {

    private Map<String, Integer> columns;
    private List<List<String>> rows;

    public Table() {
        this.columns = new LinkedHashMap<>();
        this.rows = new ArrayList<>();
    }

    public void setHeaders(String... headers) {
        for (String s : headers) {
            columns.put(s, s.length());
        }
    }

    public void addRow(String... values) {
        List<String> row = new ArrayList<>(Arrays.asList(values));
        rows.add(row);

        int columnNum = 0;
        for (Map.Entry<String, Integer> entry : columns.entrySet()) {
            if (row.get(columnNum).length() > entry.getValue()) {
                columns.replace(entry.getKey(), row.get(columnNum).length());
            }
            columnNum++;
        }
    }

    public String render() {
        fixRowWidths();

        StringBuilder table = new StringBuilder();

        // Header line
        for (Map.Entry<String, Integer> entry : columns.entrySet()) {
            table.append(entry.getKey());

            int widthDifference = entry.getValue() - entry.getKey().length();
            if (widthDifference > 0) {
                table.append(" ".repeat(widthDifference));
            }

            table.append(" ");
        }
        table.append("\n");

        // Table separator line
        for (int width : columns.values()) {
            table.append("-".repeat(width)).append(" ");
        }
        table.append("\n");

        // Rows
        for (List<String> row : rows) {
            table.append(String.join(" ", row)).append("\n");
        }

        return table.toString();
    }

    private void fixRowWidths() {
        for (List<String> row : rows) { // for each row in the table
            int columnNum = 0;
            for (int columnWidth : columns.values()) { // for each column
                int widthDifference = columnWidth - row.get(columnNum).length();
                if (widthDifference > 0) {
                    row.set(columnNum, row.get(columnNum) + " ".repeat(widthDifference));
                }
                columnNum++;
            }
        }
    }
}
