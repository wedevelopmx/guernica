package mx.wedevelop.guernica.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import mx.wedevelop.guernica.sqlite.service.OrderService;
import mx.wedevelop.guernica.sqlite.service.ProductTypeService;
import mx.wedevelop.guernica.sqlite.service.ReportService;
import mx.wedevelop.guernica.sqlite.service.ShiftService;
import mx.wedevelop.guernica.sqlite.service.UserService;

/**
 * Created by root on 22/07/16.
 */
public class DBDataSource {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public UserService User;
    public ShiftService Shift;
    public OrderService Order;
    public ProductTypeService ProductType;
    public ReportService Report;

    public DBDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

        ProductType = new ProductTypeService(database);
        Shift = new ShiftService(database);
        Order = new OrderService(database);
        User = new UserService(database);
        Report = new ReportService(database);
    }

    public void close() {
        dbHelper.close();
    }
}
