package com.calculator.grade.studentgradecalculator.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class MainController {

    @FXML
    private HBox hbButtons;
    @FXML
    private VBox vbMain;
    @FXML
    private HBox hbInit;

    private UUID uuid;


    private void start(){
        var vbMainChildren = vbMain.getChildren();

        //add controls to the hBoxes
        
    }

    
    

    public void onCalculate(ActionEvent actionEvent) {
    }

    public void onAddEntry(ActionEvent actionEvent) {

        uuid = UUID.randomUUID();
        HBox hBox = new HBox();


        hBox.setId(uuid.toString());

        hBox.prefWidth(Region.USE_COMPUTED_SIZE);
        hBox.prefHeight(Region.USE_COMPUTED_SIZE);

        TextField txtSubjectName = new TextField();
        TextField txtMarks = new TextField();

        txtSubjectName.prefWidth(Region.USE_COMPUTED_SIZE);
        txtMarks.prefWidth(Region.USE_COMPUTED_SIZE);

        txtMarks.setPromptText("Marks");
        txtMarks.addEventFilter(KeyEvent.KEY_TYPED,keyEvent -> {


            String character = keyEvent.getCharacter();
            if(!character.matches("[0-9]"))
              keyEvent.consume();
            else if (keyEvent.getSource() instanceof TextField textField && !textField.getText().isEmpty()) {
                String val = textField.getText().concat(character);
                double mark = Double.parseDouble(val);
                if (!(mark >= 0 && mark <= 100))
                    keyEvent.consume();
            }


        });
//        txtMarks.addEventFilter(KeyEvent.KEY_TYPED,keyEvent -> {
//            if(!keyEvent.getText().matches("([0-9]|[0-100])"))
//                keyEvent.consume();
//        });


        txtSubjectName.setPromptText("Subject Name");

        hBox.setPadding(new Insets(10));


        hBox.getChildren().addAll(txtSubjectName,txtMarks);

        vbMain.getChildren().add(hBox);

        vbMain.getChildren().removeIf(new Predicate<Node>() {
            @Override
            public boolean test(Node node) {
                if (node instanceof HBox hbNode) {
                    return Objects.equals(hbNode.getId(), hbButtons.getId());
                }
                return false;
            }
        });
        vbMain.getChildren().add(hbButtons);
    }
}