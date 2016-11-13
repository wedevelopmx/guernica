package mx.wedevelop.guernica.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.Product;
import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.sqlite.model.WorkShift;
import mx.wedevelop.guernica.sqlite.service.OrderService;
import mx.wedevelop.guernica.sqlite.service.ProductTypeService;
import mx.wedevelop.guernica.sqlite.service.ShiftService;
import mx.wedevelop.guernica.sqlite.service.UserService;
import mx.wedevelop.guernica.sqlite.service.WorkShiftService;
import mx.wedevelop.guernica.utils.Utils;

/**
 * Created by root on 22/07/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    ProductTypeService productTypeService;
    WorkShiftService workShiftService;

    public DBHelper(Context context) {
        super(context, context.getString(R.string.database_name), null, 7);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(context.getString(R.string.table_user));
        db.execSQL(context.getString(R.string.table_work_shift));
        db.execSQL(context.getString(R.string.table_shift));
        db.execSQL(context.getString(R.string.table_order));
        db.execSQL(context.getString(R.string.table_product));
        db.execSQL(context.getString(R.string.table_product_type));

        //Data initialization
        productTypeService = new ProductTypeService(db);

        productTypeService.save(new ProductType("Ciel", "Garrafon 20L",  10.0));
        productTypeService.save(new ProductType("Electropura", "Garrafon 20L", 10.0));
        productTypeService.save(new ProductType("Bonafont", "Garrafon 20L", 10.0));

        //WorkShifts
        workShiftService = new WorkShiftService(db);

        workShiftService.save(new WorkShift(context.getString(R.string.sunday), 0, "9:00", "15:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.monday), 1, "9:00", "15:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.monday), 1, "15:00", "20:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.tuesday), 2, "9:00", "15:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.tuesday), 2, "15:00", "20:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.wednesday), 3, "9:00", "15:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.wednesday), 3, "15:00", "20:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.thursday), 4, "9:00", "15:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.thursday), 4, "15:00", "20:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.friday), 5, "9:00", "15:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.friday), 5, "15:00", "20:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.saturday), 6, "9:00", "15:00"));
        workShiftService.save(new WorkShift(context.getString(R.string.saturday), 6, "15:00", "20:00"));


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
