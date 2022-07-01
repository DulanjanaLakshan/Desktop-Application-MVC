package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import entity.Order;
import entity.OrderDetail;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.tm.OrderDetailTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class DashBoardFormController implements Initializable {
    public TableView<OrderDetailTM> tbOrderDetail;
    public Label lbdate;
    public Label lbtime;
    public Label lbOrderId;
    public JFXTextField txtTotal;
    public JFXTextField txtbalance;
    public JFXTextField txtcash;
    public JFXTextField txtaddQty;
    public JFXTextField txtItemIdUnitPrice;
    public JFXTextField txtItemqtyonHand;
    public JFXTextField txtItemdiscription;
    public JFXTextField txtItemBrand;
    public JFXTextField txtItemId;
    public JFXTextField txtcustemail;
    public JFXTextField txtcusttp;
    public JFXTextField txtcustAddress;
    public JFXTextField txtcustname;
    public JFXTextField txtcustId;
    public AnchorPane root;
    public TableColumn tbId;
    public TableColumn tbOrderQty;
    public TableColumn tbDiscount;
    ObservableList<OrderDetailTM> observableList = FXCollections.observableArrayList();
    int cartSelectedRowForRemove=-1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbId.setCellValueFactory(new PropertyValueFactory<>("itemcode"));
        tbOrderQty.setCellValueFactory(new PropertyValueFactory<>("OrderQty"));
        tbDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        generateOrderId();
        loadDateAndTime();

        tbOrderDetail.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cartSelectedRowForRemove = (int) newValue;
        });;

    }

    public void backButton(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/HomeForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void removeOnAction(ActionEvent actionEvent) {
        if (cartSelectedRowForRemove == -1) {
            new Alert(Alert.AlertType.WARNING, "please select a row").show();
        } else {
            observableList.remove(cartSelectedRowForRemove);
            tbOrderDetail.refresh();
        }
    }
    private int isExists(OrderDetailTM tm) {
        for (int i = 0; i < observableList.size(); i++) {
            if (tm.getItemcode().equals(observableList.get(i).getItemcode())) {
                return i;
            }
        }
        return -1;
    }

    public void addToTableAction(ActionEvent actionEvent) {
            String itemid = txtItemId.getText();
            double unitprice = Double.parseDouble(txtItemIdUnitPrice.getText());
            int qtyonhand = Integer.parseInt(txtItemqtyonHand.getText());
            int addqty = Integer.parseInt(txtaddQty.getText());
            double total = addqty * unitprice;

            if (qtyonhand < addqty) {
                new Alert(Alert.AlertType.WARNING, "Invalid amount").show();
                return;
            }
            OrderDetailTM tm = new OrderDetailTM(itemid, addqty, total);
            int rownumber = isExists(tm);
            if (isExists(tm) == -1) {
                observableList.add(tm);
            } else {
                OrderDetailTM temptm = observableList.get(rownumber);
                OrderDetailTM newtm = new OrderDetailTM(temptm.getItemcode(), temptm.getOrderQty(), temptm.getDiscount());
                if (qtyonhand < temptm.getOrderQty()) {
                    new Alert(Alert.AlertType.WARNING, "Invalid amount").show();
                    return;
                }
                observableList.remove(rownumber);
                observableList.add(newtm);
            }
            tbOrderDetail.setItems(observableList);
            calCulateCost();
            txtcash.requestFocus();
    }

    public void AddCustomerForm(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../views/CustomerForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void addQtyOnAction(ActionEvent actionEvent) {
    }

    public void itemSearchOnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * FROM item WHERE item_code=?");
            preparedStatement.setObject(1,txtItemId.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                txtItemBrand.setText(resultSet.getString(2));
                txtItemdiscription.setText(resultSet.getString(3));
                txtItemqtyonHand.setText(resultSet.getString(4));
                txtItemIdUnitPrice.setText(resultSet.getString(5));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtaddQty.requestFocus();
    }

    public void customerSearchOnAction(ActionEvent actionEvent) {
        try {
            Connection connection= DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * FROM customer WHERE cust_id=?");
            preparedStatement.setObject(1,txtcustId.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                txtcustname.setText(resultSet.getString(2));
                txtcustAddress.setText(resultSet.getString(3));
                txtcusttp.setText(resultSet.getString(4));
                txtcustemail.setText(resultSet.getString(5));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtItemId.requestFocus();
    }
    void calCulateCost() {
        double ttl = 0;
        for (OrderDetailTM tm : observableList) {
            ttl += tm.getDiscount();
        }
        txtTotal.setText(String.valueOf(ttl));
    }


    public void calculateBalsance(ActionEvent actionEvent) {
        if (!txtcash.getText().isEmpty()) {
            double orderTotal = Double.parseDouble(txtTotal.getText());
            double custCash = Double.parseDouble(txtcash.getText());
            double cashBalance = custCash - orderTotal;

            cashBalance = cashBalance * 100;
            cashBalance = (int) cashBalance;
            cashBalance = cashBalance / 100;

            txtbalance.setText(Double.toString(cashBalance));
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Please Enter Amount to calculate Balance").show();
        }
    }
    public boolean cashPayment() {
        double total = Double.parseDouble(txtTotal.getText());
        double cash = Double.parseDouble(txtcash.getText());
        return cash > total;
    }
    private void generateOrderId() {

        String lastOrderId = null;

        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().
                    prepareStatement("SELECT orderid FROM orders ORDER BY orderid DESC LIMIT 1");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lastOrderId = resultSet.getString(1);
            }

            if (lastOrderId != null) {
                lastOrderId = lastOrderId.split("[A-Z]")[1];
                if (Integer.parseInt(lastOrderId) < 9) {
                    lastOrderId = "O00" + (Integer.parseInt(lastOrderId) + 1);
                    lbOrderId.setText(lastOrderId);
                } else if (Integer.parseInt(lastOrderId) < 100) {
                    lastOrderId = "O0" + (Integer.parseInt(lastOrderId) + 1);
                    lbOrderId.setText(lastOrderId);
                }
            } else {
                lbOrderId.setText("O001");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lbdate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lbtime.setText(currentTime.getHour() + " : " + currentTime.getMinute() + " : " + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }
    public void clearAllTextFields() {
        txtcustId.clear();
        txtcustname.clear();
        txtcustAddress.clear();
        txtcusttp.clear();
        txtcustemail.clear();
        txtItemId.clear();
        txtItemBrand.clear();
        txtItemdiscription.clear();
        txtItemqtyonHand.clear();
        txtItemIdUnitPrice.clear();
        txtaddQty.clear();
        txtTotal.clear();
        txtcash.clear();
        txtbalance.clear();
        for (int i = 0; i < tbOrderDetail.getItems().size(); i++) {
            tbOrderDetail.getItems().clear();
        }
    }
    //--------------------------------Place Order method and Transaction part------------------------------------------
    public static boolean addOrder(Order order) throws SQLException, ClassNotFoundException {

            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement psmt = connection.prepareStatement("INSERT INTO orders VALUES (?,?,?,?)");
            psmt.setObject(1, order.getOrderid());
            psmt.setObject(2, order.getCustid());
            psmt.setObject(3, order.getDate());
            psmt.setObject(4, order.getPrice());
            return psmt.executeUpdate() > 0;
    }

    public static boolean addOrderDetails(OrderDetail orderDetail) {

        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO order_detail VALUES (?,?,?,?)");
            pstm.setObject(1, orderDetail.getOrderId());
            pstm.setObject(2, orderDetail.getItemcode());
            pstm.setObject(3, orderDetail.getOrderQty());
            pstm.setObject(4, orderDetail.getDiscount());
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addOrderDetails(ArrayList<OrderDetail> arrayList) throws SQLException, ClassNotFoundException {
        for (OrderDetail orderdetail : arrayList) {
            boolean addOrderDetails = addOrderDetails(orderdetail);
            if (!addOrderDetails) {
                return false;
            }
        }
        return true;
    }

    public static boolean updateItemQty(ArrayList<OrderDetail> arrayList) throws SQLException, ClassNotFoundException {
        for (OrderDetail orderDetail : arrayList) {
            boolean updateQty = updateItemQty(orderDetail);
            if (!updateQty) {
                return false;
            }
        }
        return true;
    }

    public static boolean updateItemQty(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("UPDATE item SET qtyOnHand=qtyOnHand-? WHERE item_code=?");
        statement.setObject(1, orderDetail.getOrderQty());
        statement.setObject(2, orderDetail.getItemcode());
        return statement.executeUpdate() > 0;
    }
    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Order orders = new Order(lbOrderId.getText(), txtcustId.getText(), lbdate.getText(),
                Double.parseDouble(txtTotal.getText()));
        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
        for (OrderDetailTM cartTm : observableList) {
            orderDetailArrayList.add(new OrderDetail(lbOrderId.getText(), cartTm.getItemcode(),
                    cartTm.getOrderQty(), cartTm.getDiscount()));
        }
        Connection connection = DBConnection.getDbConnection().getConnection();
        try {
            connection.setAutoCommit(false);//transaction part
            if (cashPayment()) {
                boolean addOrder = addOrder(orders);
                if (addOrder) {
                    boolean addOrderDetails = addOrderDetails(orderDetailArrayList);
                    if (addOrderDetails) {
                        boolean updateItemQty = updateItemQty(orderDetailArrayList);
                        if (updateItemQty) {
                            connection.commit();
                            new Alert(Alert.AlertType.CONFIRMATION, "Order placed successfully", ButtonType.OK)
                                    .show();
                        }
                    }
                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.CONFIRMATION, "Order placed Unsuccessfully", ButtonType.OK).show();
                }
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Your payment is not full", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
        generateOrderId();
        clearAllTextFields();

    }


}
