package views.tm;

public class OrderDetailTM {
    private String itemcode;
    private int OrderQty;
    private double discount;

    public OrderDetailTM() {
    }

    public OrderDetailTM(String itemcode, int orderQty, double discount) {
        this.itemcode = itemcode;
        OrderQty = orderQty;
        this.discount = discount;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public int getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(int orderQty) {
        OrderQty = orderQty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OrderDetailTM{" +
                "itemcode='" + itemcode + '\'' +
                ", OrderQty=" + OrderQty +
                ", discount=" + discount +
                '}';
    }
}
