package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeFormController {
    public AnchorPane MainContext;

    public void btnCustomer(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/CustomerForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) MainContext.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void btnItem(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/ItemForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) MainContext.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void userButton(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/DashBoardForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) MainContext.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
