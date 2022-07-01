package entity;

public class Order {
    private String orderid;
    private String custid;
    private String date;
    private String price;

    public Order(String text, String txtcustIdText, String lbdateText, double v) {
    }

    public Order(String orderid, String custid, String date, String price) {
        this.orderid = orderid;
        this.custid = custid;
        this.date = date;
        this.price = price;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderid='" + orderid + '\'' +
                ", custid='" + custid + '\'' +
                ", date='" + date + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
