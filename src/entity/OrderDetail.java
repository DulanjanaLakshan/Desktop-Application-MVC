package entity;

public class OrderDetail {
    private String orderId;
    private String itemcode;
    private int orderQty;
    private double discount;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String itemcode, int orderQty, double discount) {
        this.orderId = orderId;
        this.itemcode = itemcode;
        this.orderQty = orderQty;
        this.discount = discount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId='" + orderId + '\'' +
                ", itemcode='" + itemcode + '\'' +
                ", orderQty=" + orderQty +
                ", discount=" + discount +
                '}';
    }
}
