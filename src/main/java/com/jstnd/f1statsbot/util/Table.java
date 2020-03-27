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
        for (String s: headers) {
            columns.put(s, s.length());
        }
    }

    public void addRow(String... values) {
        List<String> row = new ArrayList<>(Arrays.asList(values));
        rows.add(row);

        int i = 0;
        for (Map.Entry<String, Integer> entry : columns.entrySet()) {
            if (row.get(i).length() > entry.getValue()) {
                columns.replace(entry.getKey(), row.get(i).length());
            }
            i++;
        }
    }

    public String render() {
        fixRowWidths();

        StringBuilder table = new StringBuilder();

        // Header line
        for (Map.Entry<String, Integer> entry : columns.entrySet()) {
            table.append(entry.getKey());
            if (entry.getKey().length() < entry.getValue()) {
                int n = entry.getValue() - entry.getKey().length();
                for (int i = 0; i < n; ++i) {
                    table.append(" ");
                }
            }
            table.append(" ");
        }
        table.append("\n");

        // Table separator line
        for (int i : columns.values()) {
            for (int j = 0; j < i; ++j) {
                table.append("-");
            }
            table.append(" ");
        }
        table.append("\n");

        // Rows
        for (List<String> row : rows) {
            for (String s: row) {
                table.append(s).append(" ");
            }
            table.append("\n");
        }

        return table.toString();
    }

    private void fixRowWidths() {
        for (List<String> row : rows) { // for each row in the table
            int i = 0;
            for (Map.Entry<String, Integer> entry : columns.entrySet()) { // for each header
                if (row.get(i).length() < entry.getValue()) { // if length of a value in a given row is lower than the column width
                    int widthDifference = entry.getValue() - row.get(i).length();
                    String value = row.get(i);
                    for (int j = 0; j < widthDifference; ++j) {
                        value = value.concat(" ");
                    }
                    row.set(i, value);
                }
                i++;
            }

        }
    }
}
