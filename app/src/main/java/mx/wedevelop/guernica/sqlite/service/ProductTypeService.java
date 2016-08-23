package mx.wedevelop.guernica.sqlite.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.sqlite.model.Product;
import mx.wedevelop.guernica.sqlite.model.ProductType;

/**
 * Created by root on 24/07/16.
 */
public class ProductTypeService extends Service {
    private static final String SHIFT_SUMMARY =
            "        select  pt.id, pt.name, pt.description, (sum(p.quantity) * pt.unit_cost) as unit_cost, sum(p.quantity) as quantity" +
            "        from orders o, product p, product_type pt" +
            "        where o.id = p.order_id" +
            "        and p.product_type_id = pt.id" +
            "        and o.shift_id = ?" +
            "        group by pt.id";

    private SQLiteDatabase db;

    public ProductTypeService(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(ProductType model) {
        long insertId = db.insert(ProductType.TABLE_NAME, null, getValues(model));
        model.setId((int)insertId);
        return insertId;
    }

    public List<ProductType> findAll() {
        List<ProductType> list = new ArrayList<ProductType>();

        Cursor cursor = db.query(ProductType.TABLE_NAME, ProductType.TABLE_FIELDS, null, null, null, null, "created_date desc", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    public List<ProductType> findSummary(int shiftId) {
        String[] queryArgs = {shiftId + ""};
        List<ProductType> list = new ArrayList<ProductType>();

        Cursor cursor = db.rawQuery(SHIFT_SUMMARY, queryArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    public boolean remove(int id) {
        return db.delete(ProductType.TABLE_NAME, "id = " + id, null) > 0 ? true : false;
    }

    private ProductType parse(Cursor cursor) {
        ProductType productType = new ProductType();
        String [] columnNames = cursor.getColumnNames();
        for(String columnName : columnNames) {
            int index = cursor.getColumnIndex(columnName);
            switch (columnName) {
                case "id":
                    productType.setId(cursor.getInt(index));
                    break;
                case "name":
                    productType.setName(cursor.getString(index));
                    break;
                case "description":
                    productType.setDescription(cursor.getString(index));
                    break;
                case "unit_cost":
                    productType.setUnitCost(cursor.getDouble(index));
                    break;
                case "created_date":
                    productType.setCreatedDate(parseDate(cursor.getString(index)));
                    break;
                case "quantity":
                    productType.setQuantity(cursor.getInt(index));
                    break;
            }

        }
        return productType;
    }


    public ContentValues getValues(ProductType productType) {
        ContentValues values = new ContentValues();

        values.put("name", productType.getName());
        values.put("description", productType.getDescription());
        values.put("unit_cost", productType.getUnitCost());
        values.put("created_date", formatDate(productType.getCreatedDate()));

        return values;
    }
}

