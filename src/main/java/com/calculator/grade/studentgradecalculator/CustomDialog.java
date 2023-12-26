package com.calculator.grade.studentgradecalculator;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CustomDialog extends DialogPane{


    public static void run(String title,String message){
        DialogPane dialogPane = new DialogPane();

        Dialog<ButtonType> dialog = new Dialog<>();

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e-> window.hide());

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);


        Label label = new Label(message);
        HBox hBoxTitle = new HBox(label);
        hBoxTitle.setAlignment(Pos.CENTER);

        HBox hBox = new HBox();
        Button btnClose = new Button("Close");
        hBox.setAlignment(Pos.CENTER);

        btnClose.setOnAction(e->{
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
        hBox.getChildren().addAll(btnClose);

        dialogPane.setHeader(hBoxTitle);
        dialogPane.setContent(hBox);

        dialogPane.setPrefHeight(USE_COMPUTED_SIZE);
        dialogPane.setPrefWidth(USE_COMPUTED_SIZE);
        dialog.getDialogPane().setContent(dialogPane);

        dialog.show();
    }
}
