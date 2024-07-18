package org.example.sqlformat.viewmodel;

import org.example.sqlformat.model.SqlModel;
import javafx.beans.property.StringProperty;

public class SqlViewModel {
    private final SqlModel model = new SqlModel();

    public StringProperty inputSQLProperty() {
        return model.inputSQLProperty();
    }

    public StringProperty formattedSQLProperty() {
        return model.formattedSQLProperty();
    }

    public void minifySQL() {
        model.minifySQL();
    }

    public void formatSQL() {
        model.formatSQL();
    }

    public void productionSQL() {
        model.productionSQL();
    }

    public void clearSQL() {
        model.clearSQL();
    }
}
