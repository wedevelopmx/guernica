package mx.wedevelop.guernica.sqlite.model;

import java.util.Date;
import java.util.List;

/**
 * Created by root on 24/07/16.
 */

//    create table order (
//        id integer primary key autoincrement,
//        quantity integer not null,
//        unit_cost real not null,
//        created_time datetime default current_timestamp,
//        shift_id integer not null,
//        foreign key(shift_id) references shift(id)
//    )

public class Order {
    public static final String TABLE_NAME = "orders";
    public static final String[] TABLE_FIELDS = {"id", "created_date", "shift_id"};


    private int id;
    private Date createdDate;
    private Shift shift;
    private List<Product> productList;

    //Reporting
    private int quantity;
    private double cost;


    public Order() {
        this.createdDate = new Date();
    }

    public Order(int quantity, double cost, Shift shift) {
        this.quantity = quantity;
        this.cost = cost;
        this.shift = shift;
        this.createdDate = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
