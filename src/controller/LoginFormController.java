package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane LoginContext;
    public JFXTextField txtusername;
    public JFXPasswordField txtpass;

    public void usernameOnAction(ActionEvent actionEvent) {
        txtpass.requestFocus();
    }

    public void passwordOnAction(ActionEvent actionEvent) throws IOException {
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/HomeForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) LoginContext.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
