package org.example.sqlformat.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

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
            sql = sql.replaceAll("(?m)--.*$", "")
                    .replaceAll("(?i)DEFINER.*?PROCEDURE", "PROCEDURE")
                    .replaceAll("(?i)PROCEDURE\\s+(?:`[^`]+`\\.)?`([^`]+)`", "PROCEDURE $1")
                    .replaceAll("\\s+", " ")
                    .trim();

            formattedSQL.set("DELIMITER $$ " + sql + "; $$");
        } else {
            formattedSQL.set("");
        }
    }

    public void clearSQL() {
        inputSQL.set("");
        formattedSQL.set("");
    }

    public void formatJson() {
        String jsonInput = inputSQL.get();
        if (jsonInput != null && !jsonInput.isEmpty()) {
            try {
                // Usar ObjectMapper para deserializar el JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonInput);

                // Iterar sobre todas las claves del JSON para encontrar un array
                Iterator<String> fieldNames = rootNode.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    JsonNode jsonArrayNode = rootNode.get(fieldName);

                    // Verificar si la clave contiene un array
                    if (jsonArrayNode.isArray()) {
                        // Convertir el array en una cadena JSON escapada
                        String jsonArrayString = objectMapper.writeValueAsString(jsonArrayNode);
                        String escapedJsonArray = jsonArrayString.replace("\"", "\\\"");

                        // Remover los corchetes externos si existen
                        if (escapedJsonArray.startsWith("[") && escapedJsonArray.endsWith("]")) {
                            escapedJsonArray = escapedJsonArray.substring(1, escapedJsonArray.length() - 1);
                        }

                        // Formatear el resultado con el mismo nombre de la clave
                        String result = "\"" + fieldName + "\":\"[" + escapedJsonArray + "]\"";

                        formattedSQL.set(result);
                        return; // Salir del método una vez que se procesa la primera clave que contiene un array
                    }
                }
                formattedSQL.set("No se encontró un array en el JSON.");
            } catch (JsonProcessingException e) {
                formattedSQL.set("Error procesando el JSON: " + e.getMessage());
            }
        } else {
            formattedSQL.set("El input JSON está vacío.");
        }
    }

    public void generateDropProcedure() {
        String sql = inputSQL.get();
        if (sql != null && !sql.isEmpty()) {
            StringBuilder resultadoFinal = new StringBuilder();
            String[] bloques = sql.split("DELIMITER \\$\\$");

            for (String bloque : bloques) {
                if (bloque.contains("CREATE PROCEDURE")) {
                    // Buscar el nombre del procedimiento
                    String nombreProcedimiento = extraerNombreProcedimiento(bloque);
                    if (!nombreProcedimiento.isEmpty()) {
                        resultadoFinal.append("DROP PROCEDURE IF EXISTS ").append(nombreProcedimiento).append(";\n");
                    }
                }
            }

            formattedSQL.set(resultadoFinal.toString().trim());
        } else {
            formattedSQL.set("El input SQL está vacío.");
        }
    }

    private String extraerNombreProcedimiento(String bloque) {
        String nombreProcedimiento = "";
        String[] lineas = bloque.split("\n");

        for (String linea : lineas) {
            if (linea.contains("CREATE PROCEDURE")) {
                // Extraer el nombre del procedimiento entre backticks (`) o después de CREATE PROCEDURE
                int startIndex = linea.indexOf("`") + 1;
                int endIndex = linea.indexOf("`", startIndex);
                if (startIndex > 0 && endIndex > 0) {
                    nombreProcedimiento = linea.substring(startIndex, endIndex);
                } else {
                    // Si no hay backticks, buscar el nombre después de CREATE PROCEDURE
                    startIndex = linea.indexOf("CREATE PROCEDURE") + "CREATE PROCEDURE".length();
                    nombreProcedimiento = linea.substring(startIndex).trim().split("\\s+")[0];
                }
                break;
            }
        }
        return nombreProcedimiento;
    }
}
