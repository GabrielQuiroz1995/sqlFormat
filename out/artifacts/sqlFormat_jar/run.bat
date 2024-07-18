@echo off
start /B javaw --module-path "./javafx/lib" --add-modules javafx.controls,javafx.fxml -jar sqlFormat.jar
exit


