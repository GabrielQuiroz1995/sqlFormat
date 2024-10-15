module org.example.sqlformat {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens org.example.sqlformat.view to javafx.fxml;
    exports org.example.sqlformat;
}
