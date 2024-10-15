package org.example.sqlformat.view;

import org.example.sqlformat.util.TooltipConfigurer;
import org.example.sqlformat.viewmodel.SqlViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SqlViewController {
    private final SqlViewModel viewModel = new SqlViewModel();

    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea outputArea;
    @FXML
    private CheckBox darkModeCheckBox;
    @FXML
    private VBox layout;
    @FXML
    private Button copyButton;
    @FXML
    private Button productionButton;
    @FXML
    private Button parrafoButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button btnFormatJson;

    @FXML
    public void initialize() {
        inputArea.textProperty().bindBidirectional(viewModel.inputSQLProperty());
        outputArea.textProperty().bind(viewModel.formattedSQLProperty());

        btnFormatJson.setOnAction(event -> {
            viewModel.formatJson(); // Llama al método en el ViewModel
        });

        // Seleccionar el modo oscuro por defecto
        darkModeCheckBox.setSelected(true);
        setDarkMode(); // Aplicar el estilo de modo oscuro

        copyButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");
        clearButton.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white;");

        TooltipConfigurer.configure(productionButton, "Se agregan los DELIMITER $$, se borra DEFINER y se eliminan los comentarios");
        TooltipConfigurer.configure(parrafoButton, "Recuerda agregar 'IN' a los campos de entrada del SP");
        TooltipConfigurer.configure(copyButton, "Copiar el SQL formateado al portapapeles");
        TooltipConfigurer.configure(clearButton, "Limpiar los campos de entrada y salida");
        TooltipConfigurer.configure(btnFormatJson, "Transforma un objeto JSON a un texto plano");
    }

    @FXML
    private void handleMinifySQL() {
        viewModel.minifySQL();
    }

    @FXML
    private void handleFormatSQL() {
        viewModel.formatSQL();
    }

    @FXML
    private void handleProductionSQL() {
        viewModel.productionSQL();
    }

    @FXML
    private void handleCopy() {
        String formattedSQL = outputArea.getText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(formattedSQL);
        clipboard.setContent(content);

        String originalStyle = copyButton.getStyle();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(copyButton.styleProperty(), "-fx-background-color: #B0B0B0; -fx-text-fill: white;")),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(copyButton.styleProperty(), originalStyle))
        );
        timeline.play();
    }

    @FXML
    private void handleClear() {
        viewModel.clearSQL();
    }

    @FXML
    private void handleToggleDarkMode() {
        if (darkModeCheckBox.isSelected()) {
            setDarkMode();
        } else {
            setLightMode();
        }
    }

    public void handleGenerateDropProcedure() {
        viewModel.generateDropProcedure();
    }


    private void setDarkMode() {
        layout.setStyle("-fx-background-color: darkslategray;");
        inputArea.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-fill: white; -fx-prompt-text-fill: white;");
        outputArea.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-fill: white; -fx-prompt-text-fill: white;");
        darkModeCheckBox.setStyle("-fx-text-fill: white;");
    }

    private void setLightMode() {
        layout.setStyle("-fx-background-color: lightgray;");
        inputArea.setStyle("-fx-control-inner-background: white; -fx-text-fill: black; -fx-prompt-text-fill: gray;");
        outputArea.setStyle("-fx-control-inner-background: white; -fx-text-fill: black; -fx-prompt-text-fill: gray;");
        darkModeCheckBox.setStyle("-fx-text-fill: black;");
    }
}
