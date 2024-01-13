package com.calculator.grade.studentgradecalculator.controllers;

import com.calculator.grade.studentgradecalculator.CustomDialog;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private int hBoxCount =0;

    private ObservableList<ObservableList<String>> rowEntryList;

    private static final String[] columns = {"Subject","Mark"};

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

                for (Node hBoxChild : hbox.getChildren()) {
                    if (hBoxChild instanceof TextField textField) {
                        ol.add(textField.getText());
                    }
                }

                rowEntryList.add(ol);

                tbvResult.setItems(rowEntryList);
            }
        }
    }

    private void calculatedRows(){

        double sum = 0;






        for (ObservableList<String> rows : rowEntryList) {

            for (int j = 0; j < rows.size(); j++) {
                //skip 1st column
                if( j == 0 ) {
                    //validate the subject textbox
                    continue;
                }

                sum += Double.parseDouble(rows.get(j));

            }
        }

        double average =Math.round(sum/rowEntryList.size());


        ObservableList<String> ol = FXCollections.observableArrayList();
        ol.add("Total");
        ol.add(String.valueOf(sum));


        rowEntryList.add(ol);
        tbvResult.setItems(rowEntryList);


        ObservableList<String> ol1 = FXCollections.observableArrayList();
        ol1.add("Average");
        ol1.add(String.valueOf(average));

        rowEntryList.add(ol1);
        tbvResult.setItems(rowEntryList);


        ObservableList<String> ol2 = getGrade(average);

        rowEntryList.add(ol2);
        tbvResult.setItems(rowEntryList);
    }

    private ObservableList<String> getGrade(double average) {
        char chGrade = '\0';

        if(average >=0&& average <=29){
            chGrade='1';
        }else if(average >=30 && average <=39){
            chGrade='2';
        }else if(average >=40 && average <=49){
            chGrade='3';
        }else if(average >=50 && average <=59){
            chGrade='4';
        } else if(average >=60 && average <=69){
            chGrade='5';
        }else if(average >=70 && average <=79){
            chGrade='6';
        }else if(average >=80 && average <=100){
            chGrade='7';
        }

        ObservableList<String> ol2 = FXCollections.observableArrayList();
        ol2.add("Grade");
        ol2.add(String.valueOf(chGrade));
        return ol2;
    }

    @FXML
    protected void onCalculate(ActionEvent actionEvent) {
        if(validate())return;
        addRows();
        calculatedRows();

    }

    private boolean validate() {

        //the purpose is to prevent duplication,
        Set<String> subjectSet = new HashSet<>();
        int count = 0;
        for(Node vbNodeChild :vbMain.getChildren()){
            if(vbNodeChild instanceof HBox hBox){
                for (Node hbNodeChild:hBox.getChildren()){

                    if(hbNodeChild instanceof TextField textField){

                        if(textField.getText().isEmpty()) {
                            //notify user of empty
                            CustomDialog.run("Empty input", "Input can not be empty");
                            return true;
                        }
                        //ensures that it only checks the subject textfield(s)
                        if(textField.getId().startsWith("txtSubject")) {

                            String text = textField.getText();
                            subjectSet.add(text.toLowerCase());
                            count++;

                            if(count != subjectSet.size()){
                                //a subject was not added meaning that its a duplicate
                                CustomDialog.run("Duplicate input", "You entered a duplicate subject.");
                                return true;
                            }
                        }



                    }
                }
            }
        }
        return false;
    }

    @FXML
    protected void onAddEntry(ActionEvent actionEvent) {

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