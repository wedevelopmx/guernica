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

    public void createSample(Date now) {
        UserService userService = new UserService(database);
        ShiftService shiftService = new ShiftService(database);
        OrderService orderService = new OrderService(database);
        ProductTypeService productTypeService = new ProductTypeService(database);


        List<ProductType> productTypeList = productTypeService.findAll();
        User user = new User("Origin", "contact@wedevelop.mx", "");
        ShiftElapseSimulation sim = new ShiftElapseSimulation(new Date(), 1);

        while (sim.tomorrow()) {

            Shift shift = shiftService.findOrCreateShift(user, sim.today());
            shift.setStartTime(sim.today());

            shiftService.update(shift);

            List<Order> orderList = generateOrder(productTypeList);
            for(Order order : orderList) {
                order.setShift(shift);
                orderService.save(order);
            }


            shift = shiftService.findOrCreateShift(user, sim.afternoonShift());
            shift.setStartTime(sim.afternoonShift());

            shiftService.update(shift);

            orderList = generateOrder(productTypeList);
            for(Order order : orderList) {
                order.setShift(shift);
                orderService.save(order);
            }
        }
    }

    private List<Order> generateOrder(List<ProductType> productTypeList) {
        List<Order> orderList = new ArrayList<Order>();
        int number = (int)Math.round((Math.random() + 1) * 12);
        while(number-- > 0) {
            Order order = new Order();
            order.setProductList(generateOrderProducts(productTypeList));
            orderList.add(order);
        }
        return orderList;
    }

    private List<Product> generateOrderProducts(List<ProductType> productTypeList) {
        List<Product> productList = new ArrayList<Product>();

        for(ProductType productType: productTypeList) {
            int number = (int)Math.round((Math.random() + 1) * 3);
            if(Math.random() > 0.5) {
                Product product = new Product();
                product.setQuantity(number);
                product.setProductType(productType);
                productList.add(product);
            }
        }

        return productList;
    }


}
