package mx.wedevelop.guernica.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.Product;
import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.sqlite.service.OrderService;
import mx.wedevelop.guernica.sqlite.service.ProductTypeService;
import mx.wedevelop.guernica.sqlite.service.ShiftService;
import mx.wedevelop.guernica.sqlite.service.UserService;
import mx.wedevelop.guernica.utils.Utils;

/**
 * Created by root on 22/07/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    ProductTypeService productTypeService;

    public DBHelper(Context context) {
        super(context, context.getString(R.string.database_name), null, 6);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(context.getString(R.string.table_user));
        db.execSQL(context.getString(R.string.table_shift));
        db.execSQL(context.getString(R.string.table_order));
        db.execSQL(context.getString(R.string.table_product));
        db.execSQL(context.getString(R.string.table_product_type));

        //Data initialization
        productTypeService = new ProductTypeService(db);

        productTypeService.save(new ProductType("Ciel", "Garrafon 20L",  10.0));
        productTypeService.save(new ProductType("Electropura", "Garrafon 20L", 10.0));
        productTypeService.save(new ProductType("Bonafont", "Garrafon 20L", 10.0));


//        UserService userService = new UserService(db);
//        ShiftService shiftService = new ShiftService(db);
//        OrderService orderService = new OrderService(db);
//
//        List<ProductType> productTypeList = productTypeService.findAll();
//
//        User user = new User("Origin", "contact@wedevelop.mx", "");
//
//        for(int i = 1; i < 28; i ++) {
//            Date start = Utils.parseDate(i + "-07-2016 08:00 AM");
//            Date end = Utils.parseDate(i + "-07-2016 08:00 PM");
//
//            Log.i("Initializing", start.toString());
//
//
//            Shift shift = new Shift();
//            shift.setStartTime(start);
//            shift.setEndTime(end);
//            shift.setUser(user);
//
//            shiftService.save(shift);
//            List<Order> orderList = generateOrder(productTypeList);
//            for(Order order : orderList) {
//                order.setShift(shift);
//                orderService.save(order);
//            }
//        }
    }

    private List<Order> generateOrder(List<ProductType> productTypeList) {
        List<Order> orderList = new ArrayList<Order>();
        int number = (int)Math.round(Math.random() * 12);
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
            int number = (int)Math.round(Math.random() * 3);
            if(Math.random() > 0.5) {
                Product product = new Product();
                product.setQuantity(number);
                product.setProductType(productType);
                productList.add(product);
            }
        }

        return productList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        db.execSQL(context.getString(R.string.drop_table_user));
        db.execSQL(context.getString(R.string.drop_table_shift));
        db.execSQL(context.getString(R.string.drop_table_order));
        db.execSQL(context.getString(R.string.drop_table_product));
        db.execSQL(context.getString(R.string.drop_table_product_type));

        onCreate(db);
    }
}
