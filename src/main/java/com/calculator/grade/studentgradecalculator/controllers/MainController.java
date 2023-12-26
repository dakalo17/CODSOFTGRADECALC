package com.calculator.grade.studentgradecalculator.controllers;


import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;


public class MainController implements Initializable {

    @FXML
    private TableView<ObservableList<String>> tbvResult;
    @FXML
    private HBox hbButtons;
    @FXML
    private VBox vbMain;

    private UUID uuid;

    private int hBoxCount =0;

    private ObservableList<ObservableList<String>> rowEntryList;

    private static final String[] columns = {"Subject","Mark"};
    private static final String[] rows = {"Total","Average","Grade"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::setColumns);
        rowEntryList = FXCollections.observableArrayList();

    }
    private void setColumns(){


        for (int i = 0; i < columns.length; i++) {
            String colName = columns[i];

            TableColumn<ObservableList<String>, String> colAve = new TableColumn<>(colName);

            final int colIndex = i;
            colAve.setCellValueFactory(val -> {
                var list = val.getValue();

                double sum = 0;

                for (String s : list) {
                    //sum += Double.parseDouble(s);
                }
                //double average = sum / list.size();
              return new SimpleStringProperty(list.get(colIndex));

            });
            tbvResult.getColumns().add(colAve);

        }
        tbvResult.setItems(rowEntryList);

    }

    private void addRows(){

        //clear before calculation
        rowEntryList.clear();
        tbvResult.setPlaceholder(null);
        for (Node vbChild : vbMain.getChildren()) {
            if (vbChild instanceof HBox hbox && !hbox.getId().equals(hbButtons.getId())) {
                ObservableList<String> ol = FXCollections.observableArrayList();

                //for (Node child : vbMain.getChildren()) {
                //   if (child instanceof HBox hbox && !hbox.getId().equals(hbButtons.getId())) {
                for (Node hBoxChild : hbox.getChildren()) {
                    if (hBoxChild instanceof TextField textField) {
                        ol.add(textField.getText());
                    }
                    //}
                }

                rowEntryList.add(ol);
                // tbvResult.setEditable(true);
                tbvResult.setItems(rowEntryList);
            }
        }
    }
    @FXML
    protected void onCalculate(ActionEvent actionEvent) {
        addRows();

//        var children = vbMain.getChildren();
//        for (int i = 0; i < children.size(); i++) {
//
//            if(children.get(i) instanceof HBox hBox && !hBox.getId().equals(hbButtons.getId())) {
//
//                var hBoxChildren = hBox.getChildren();
//                for (int j = 0; j < hBoxChildren.size(); j++) {
//                    if(hBoxChildren.get(i) instanceof TextField textField){
//                        if(textField.getId().contains("txtSubject")) {
////                            //add column
////                            TableColumn<ObservableList<String>, String> col = new TableColumn<>(textField.getText());
////                            col.setCellValueFactory(val -> {
////                                var rowlist = val.getValue();
////
////                                return new SimpleStringProperty();
////                            });
////                            tbvResult.getColumns().add(col);
//
//                        }else if(textField.getId().contains("txtMarks")){
//                            //add row
//                        }
//
//                        //add row
//                    }
//                }
//
//            }
//        }


    }

    @FXML
    protected void onAddEntry(ActionEvent actionEvent) {

        uuid = UUID.randomUUID();
        HBox hBox = new HBox();


        hBox.setId(String.valueOf(hBoxCount));

        hBox.prefWidth(Region.USE_COMPUTED_SIZE);
        hBox.prefHeight(Region.USE_COMPUTED_SIZE);

         Button btnRemove = new Button("Remove");
        TextField txtSubjectName = new TextField();
        TextField txtMarks = new TextField();

        btnRemove.setId(String.valueOf(hBoxCount));
        txtSubjectName.setId("txtSubject"+hBoxCount);
        txtMarks.setId("txtMarks"+(hBoxCount++));

        txtSubjectName.prefWidth(Region.USE_COMPUTED_SIZE);
        txtMarks.prefWidth(Region.USE_COMPUTED_SIZE);

        txtMarks.setPromptText("Marks [0 - 100]");
        txtMarks.addEventFilter(KeyEvent.KEY_TYPED,keyEvent -> {

            String character = keyEvent.getCharacter();
            if (character.matches("[0-9]")) {
                if (keyEvent.getSource() instanceof TextField textField &&
                        !textField.getText().isEmpty()) {
                    String val = textField.getText().concat(character);
                    double mark = Double.parseDouble(val);
                    if (!(mark >= 0 && mark <= 100))
                        keyEvent.consume();
                }
            } else {
                keyEvent.consume();
            }


        });
        btnRemove.setOnAction(e->
                vbMain.getChildren().removeIf(node ->
                        removeEntryControl(node,btnRemove.getId())));

        txtSubjectName.setPromptText("Subject Name");

        hBox.setPadding(new Insets(10));


        hBox.getChildren().addAll(txtSubjectName,txtMarks,btnRemove);

        vbMain.getChildren().add(hBox);

        vbMain.getChildren().removeIf(node -> removeEntryControl(node,hbButtons.getId()));
        vbMain.getChildren().add(hbButtons);
    }

    private boolean removeEntryControl(Node node,String Id) {
        if (node instanceof HBox hbNode) {
            return Objects.equals(hbNode.getId(),Id);
        }
        return false;
    }


}