package org.example.sqlformat.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SqlModel {
    private final StringProperty inputSQL = new SimpleStringProperty();
    private final StringProperty formattedSQL = new SimpleStringProperty();

    public StringProperty inputSQLProperty() {
        return inputSQL;
    }

    public StringProperty formattedSQLProperty() {
        return formattedSQL;
    }

    public void minifySQL() {
        String sql = inputSQL.get();
        if (sql != null && !sql.isEmpty()) {
            formattedSQL.set(sql.replaceAll("\\s+", " ").trim());
        } else {
            formattedSQL.set("");
        }
    }

    public void formatSQL() {
        String sql = inputSQL.get();
        if (sql != null && !sql.isEmpty()) {

            sql = sql.replaceAll(";", ";\n")
                    .replaceAll("(?i)\\bBEGIN\\b", "\nBEGIN\n")
                    .replaceAll("(?i)\\bTHEN\\b", "THEN\n\n")
                    .replaceAll("(?i)\\s+IN\\b", "\nIN");

            String[] lines = sql.split("\n");
            StringBuilder formattedSQLBuilder = new StringBuilder();
            String indent = "    ";

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                formattedSQLBuilder.append(indent).append(line).append("\n");

                if (line.endsWith(";") && i + 1 < lines.length) {
                    String nextLine = lines[i + 1].trim().toUpperCase();
                    if (!nextLine.startsWith("DECLARE") && !nextLine.startsWith("CALL") && !nextLine.startsWith("SET")) {
                        formattedSQLBuilder.append("\n");
                    }
                }
            }
            formattedSQL.set(formattedSQLBuilder.toString().trim());
        } else {
            formattedSQL.set("");
        }
    }

    public void productionSQL() {
        String sql = inputSQL.get();
        if (sql != null && !sql.isEmpty()) {

            // Eliminar comentarios de tipo -- comentario
            sql = sql.replaceAll("(?m)--.*$", "");

            sql = sql.replaceAll("(?i)DEFINER.*?PROCEDURE", "PROCEDURE")
                    .replaceAll("\\s+", " ").trim();
            formattedSQL.set("DELIMITER $$ " + sql + "; $$");
        } else {
            formattedSQL.set("");
        }
    }
    public void clearSQL() {
        inputSQL.set("");
        formattedSQL.set("");
    }
}
