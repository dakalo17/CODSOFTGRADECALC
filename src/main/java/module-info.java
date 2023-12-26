module com.calculator.grade.studentgradecalculator {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.calculator.grade.studentgradecalculator to javafx.fxml;
    exports com.calculator.grade.studentgradecalculator;
    exports com.calculator.grade.studentgradecalculator.controllers;
    opens com.calculator.grade.studentgradecalculator.controllers to javafx.fxml;
}