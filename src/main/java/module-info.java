module org.example.sqlformat {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.sqlformat.view to javafx.fxml;
    exports org.example.sqlformat;
}
