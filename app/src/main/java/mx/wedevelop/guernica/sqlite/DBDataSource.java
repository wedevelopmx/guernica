package mx.wedevelop.guernica.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.Product;
import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.sqlite.service.OrderService;
import mx.wedevelop.guernica.sqlite.service.ProductTypeService;
import mx.wedevelop.guernica.sqlite.service.ReportService;
import mx.wedevelop.guernica.sqlite.service.ShiftService;
import mx.wedevelop.guernica.sqlite.service.UserService;
import mx.wedevelop.guernica.sqlite.service.WorkShiftService;
import mx.wedevelop.guernica.utils.Utils;
import mx.wedevelop.guernica.utils.simulation.ShiftElapseSimulation;

/**
 * Created by root on 22/07/16.
 */
public class DBDataSource {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public UserService User;
    public WorkShiftService WorkShift;
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
        WorkShift = new WorkShiftService(database);
        Shift = new ShiftService(database);
        Order = new OrderService(database);
        User = new UserService(database);
        Report = new ReportService(database);
    }

    public void close() {
        dbHelper.close();
    }
}
