package controller;


import com.jfoenix.controls.JFXButton;
import db.DBConnection;
import entity.Customer;
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
import views.tm.CustomerTM;
import views.tm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {
    public AnchorPane mainContext;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtCustomerNo;
    public TextField txtCustomerEmail;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colNumber;
    public TableColumn colEmail;
    public JFXButton btnSaveCustomer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblCustomer.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("number"));
        tblCustomer.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("email"));

        loadAllCustomers();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            txtCustomerId.setText(newValue.getId());
            txtCustomerName.setText(newValue.getName());
            txtCustomerAddress.setText(newValue.getAddress());
            txtCustomerNo.setText(String.valueOf(newValue.getNumber()));
            txtCustomerEmail.setText(String.valueOf(newValue.getEmail()));

        });
    }

    private void loadAllCustomers() {
        ArrayList<Customer> customers=new ArrayList<>();
        ObservableList<CustomerTM> costomerTMObservableList= FXCollections.observableArrayList();
        try {
            Connection connection= DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM customer");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                customers.add(new Customer(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),resultSet.getString(5) ));
            }
            for (Customer cus:customers) {
                costomerTMObservableList.add(new CustomerTM(cus.getId(),cus.getName(),cus.getAddress(),
                        cus.getNumber(),cus.getEmail()));
            }
            tblCustomer.setItems(costomerTMObservableList);


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSaveCustomer(ActionEvent actionEvent) {
        Customer customer=new Customer(txtCustomerId.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),
                txtCustomerNo.getText(),txtCustomerEmail.getText());
        try {
            Connection connection=DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer "+"VALUES (?,?,?,?,?)");
            preparedStatement.setObject(1,customer.getId());
            preparedStatement.setObject(2,customer.getName());
            preparedStatement.setObject(3,customer.getAddress());
            preparedStatement.setObject(4,customer.getNumber());
            preparedStatement.setObject(5,customer.getEmail());
            int add = preparedStatement.executeUpdate();
            if (add > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!", ButtonType.OK).show();

            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!", ButtonType.OK).show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblCustomer.refresh();
        clear();
    }

    public void serchCustomer(ActionEvent actionEvent) {
        try {
            Connection connection=DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * FROM customer WHERE cust_id=?");
            preparedStatement.setObject(1,txtCustomerId.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                txtCustomerName.setText(resultSet.getString(2));
                txtCustomerAddress.setText(resultSet.getString(3));
                txtCustomerNo.setText(resultSet.getString(4));
                txtCustomerEmail.setText(resultSet.getString(5));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateCustomer(ActionEvent actionEvent) {
        Customer customer=new Customer(txtCustomerId.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),
                txtCustomerNo.getText(),txtCustomerEmail.getText());
        try {
            Connection connection=DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer SET " +
                    "cust_name=?,cust_address=?,customer_tp=?,email=? WHERE cust_id =?");
            preparedStatement.setObject(1, customer.getName());
            preparedStatement.setObject(2, customer.getAddress());
            preparedStatement.setObject(3, customer.getNumber());
            preparedStatement.setObject(4, customer.getEmail());
            preparedStatement.setObject(5, customer.getId());
            int update = preparedStatement.executeUpdate();
            if (update > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!", ButtonType.OK).show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!", ButtonType.OK).show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblCustomer.refresh();
        clear();
    }

    public void btnDeleteCustomer(ActionEvent actionEvent){
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.
                    prepareStatement("DELETE FROM customer WHERE cust_id = ?");
            preparedStatement.setObject(1,txtCustomerId.getText());
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0){
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier Delete Successfully.....!", ButtonType.OK).
                        show();
                txtCustomerId.requestFocus();
            }else {
                new Alert(Alert.AlertType.WARNING, "Supplier Delete Unsuccessfully.....", ButtonType.OK).
                        show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblCustomer.refresh();
        clear();
    }
//------------------------------reqrest forcus------------------------------------------------------------------------------
    public void idOnAction(ActionEvent actionEvent) {txtCustomerName.requestFocus();
    }
    public void customernameOnAction(ActionEvent actionEvent) {txtCustomerAddress.requestFocus();
    }
    public void addressOnAction(ActionEvent actionEvent) {txtCustomerNo.requestFocus();
    }
    public void contactOnAction(ActionEvent actionEvent) {txtCustomerEmail.requestFocus();
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/HomeForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) mainContext.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    private void clear() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerNo.clear();
        txtCustomerEmail.clear();
    }




}
