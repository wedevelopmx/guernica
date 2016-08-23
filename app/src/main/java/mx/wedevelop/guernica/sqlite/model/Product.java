package mx.wedevelop.guernica.sqlite.model;

import java.util.Date;

/**
 * Created by root on 24/07/16.
 */

//    create table product (
//        id integer primary key autoincrement,
//        quantity integer not null,
//        product_type_id integer not null,
//        foreign key(product_type_id) references product_type(id)
//    )


public class Product {
    public static final String TABLE_NAME = "product";
    public static final String[] TABLE_FIELDS = {"id", "quantity", "created_date"};

    private int id;
    private int quantity;
    private Date createdDate;
    private Order order;
    private ProductType productType;

    public Product() {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
