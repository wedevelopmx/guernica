package mx.wedevelop.guernica.sqlite.model;

/**
 * Created by root on 26/07/16.
 */
public class SalesSummary {
    private int quantity;
    private double cost;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
