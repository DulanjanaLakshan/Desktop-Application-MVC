package controller;

import com.jfoenix.controls.JFXButton;
import db.DBConnection;
import entity.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import views.tm.ItemTM;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {
     public AnchorPane mainContext;
    public TextField txtCode;
    public TextField txtName;
    public TextField txtDescription;
    public TextField txtQty;
    public TextField txtPrice;
    public TableView<ItemTM> tblItem;
    public TableColumn colCode;
    public TableColumn colName;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colPrice;
    public JFXButton btnSaveCustomer;
    public JFXButton btnSearchCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblItem.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            txtCode.setText(newValue.getCode());
            txtName.setText(newValue.getName());
            txtDescription.setText(newValue.getDescription());
            txtQty.setText(String.valueOf(newValue.getQty()));
            txtPrice.setText(String.valueOf(newValue.getPrice()));
        });
        loadAllItem();
    }

    private void loadAllItem()  {

        ArrayList<Item> items=new ArrayList<>();
        ObservableList<ItemTM> itemTMObservableList= FXCollections.observableArrayList();
        try {
            Connection connection= DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM item");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                items.add(new Item(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3),Integer.parseInt(resultSet.getString(4)),
                        Double.parseDouble(resultSet.getString(5)) ));
            }
            for (Item itm:items) {
                itemTMObservableList.add(new ItemTM(itm.getCode(),itm.getName(),itm.getDescription(),itm.getQty(),
                        itm.getPrice()));
            }
            tblItem.setItems(itemTMObservableList);


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnBack(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/HomeForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) mainContext.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void btnSearchCustomer(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * FROM item WHERE item_code=?");
            preparedStatement.setObject(1,txtCode.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                txtName.setText(resultSet.getString(2));
                txtDescription.setText(resultSet.getString(3));
                txtQty.setText(resultSet.getString(4));
                txtPrice.setText(resultSet.getString(5));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblItem.refresh();
    }
    public void btnSaveCustomer(ActionEvent actionEvent) {
        Item item = new Item(txtCode.getText(),txtName.getText(),txtDescription.getText(),
                Integer.parseInt(txtQty.getText()),Double.parseDouble(txtPrice.getText()));
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item " +
                    "VALUES(?,?,?,?,?)");
            preparedStatement.setObject(1, item.getCode());
            preparedStatement.setObject(2, item.getName());
            preparedStatement.setObject(3, item.getDescription());
            preparedStatement.setObject(4, item.getQty());
            preparedStatement.setObject(5, item.getPrice());
            int add = preparedStatement.executeUpdate();
            if (add > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!", ButtonType.OK).show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!", ButtonType.OK).show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblItem.refresh();
        clear();

    }

    public void btnUpdateCustomer(ActionEvent actionEvent) {
        Item item = new Item(txtCode.getText(),txtName.getText(),txtDescription.getText(),
                Integer.parseInt(txtQty.getText()),Double.parseDouble(txtPrice.getText()));

        try {
            Connection connection = DBConnection.getDbConnection().getConnection();;
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET " +
                    "item_name=?,description=?,qtyOnHand=?,unitprice=? WHERE item_code=?");
            preparedStatement.setObject(1, item.getName());
            preparedStatement.setObject(2, item.getDescription());
            preparedStatement.setObject(3, item.getQty());
            preparedStatement.setObject(4, item.getPrice());
            preparedStatement.setObject(5, item.getCode());
            int update = preparedStatement.executeUpdate();
            if (update > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!", ButtonType.OK).show();
                txtCode.requestFocus();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!", ButtonType.OK).show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblItem.refresh();;
        clear();
    }

    public void btnDeleteCustomer(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.
                    prepareStatement("DELETE FROM item WHERE item_code = ?");
            preparedStatement.setObject(1,txtCode.getText());
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0){
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier Delete Successfully.....!", ButtonType.OK).
                        show();
                txtCode.requestFocus();
            }else {
                new Alert(Alert.AlertType.WARNING, "Supplier Delete Unsuccessfully.....", ButtonType.OK).
                        show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblItem.refresh();
        clear();
    }

    //---------------------------request Forcuse methods--------------------------------
    public void idOnAction(ActionEvent actionEvent) {txtName.requestFocus();
    }
    public void brandOnAction(ActionEvent actionEvent) {txtDescription.requestFocus();
    }
    public void descriptionOnAction(ActionEvent actionEvent) {txtQty.requestFocus();
    }
    public void qtyOnHand(ActionEvent actionEvent) {txtPrice.requestFocus();
    }
    private void clear() {
        txtCode.clear();
        txtName.clear();
        txtDescription.clear();
        txtQty.clear();
        txtPrice.clear();
    }

}
