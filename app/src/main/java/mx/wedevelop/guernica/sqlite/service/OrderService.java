package mx.wedevelop.guernica.sqlite.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.Product;
import mx.wedevelop.guernica.sqlite.model.ProductType;

/**
 * Created by root on 24/07/16.
 */
public class OrderService extends Service {
    private static final String TABLE_ORDER_PRODUCT = "order_product";

    private static final String SUMMARY_ORDER =
            " select o.id, o.created_date, sum(p.quantity) as quantity, sum(p.quantity * pt.unit_cost) as cost " +
            " from shift s, orders o, product p, product_type pt " +
            " where s.id = o.shift_id and o.id = p.order_id and p.product_type_id = pt.id " +
            " and s.id = ? " +
            " group by o.id";

    private SQLiteDatabase db;

    public OrderService(SQLiteDatabase db) {
        this.db = db;
    }

    public void save(Order order) {
        long insertId = db.insert(Order.TABLE_NAME, null, getValues(order));
        order.setId((int)insertId);

        List<Product> productList = order.getProductList();
        for(Product product : productList) {
            product.setOrder(order);
            save(product);
        }
    }

    public void save(Product product) {
        long insertId = db.insert(Product.TABLE_NAME, null, getValues(product));
        product.setId((int)insertId);
    }

    public List<Order> findAll(int shiftId) {
        String[] args = {shiftId + ""};
        List<Order> list = new ArrayList<Order>();

        Cursor cursor = db.rawQuery(SUMMARY_ORDER, args);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    public boolean remove(int orderId) {
        return db.delete(Order.TABLE_NAME, "id = " + orderId, null) > 0 ? true : false;
    }

    private Order parse(Cursor cursor) {
        Order order = new Order();
        String [] columnNames = cursor.getColumnNames();
        for(String columnName : columnNames) {
            int index = cursor.getColumnIndex(columnName);
            switch (columnName) {
                case "id":
                    order.setId(cursor.getInt(index));
                    break;
                case "created_date":
                    order.setCreatedDate(parseDate(cursor.getString(index)));
                    break;
                //Reporting
                case "quantity":
                    order.setQuantity(cursor.getInt(index));
                    break;
                case "cost":
                    order.setCost(cursor.getDouble(index));
                    break;
            }

        }
        return order;
    }


    public ContentValues getValues(Order order) {
        ContentValues values = new ContentValues();

        values.put("created_date", formatDate(order.getCreatedDate()));
        values.put("shift_id", order.getShift().getId());

        return values;
    }

    public ContentValues getValues(Product product) {
        ContentValues values = new ContentValues();

        values.put("quantity", product.getQuantity());
        values.put("created_date", formatDate(product.getCreatedDate()));
        values.put("product_type_id", product.getProductType().getId());
        values.put("order_id", product.getOrder().getId());

        return values;
    }

}
