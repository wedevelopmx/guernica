package mx.wedevelop.guernica.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.lang.ref.Reference;
import java.security.BasicPermission;

import mx.wedevelop.guernica.sqlite.model.ProductType;

import static mx.wedevelop.guernica.provider.GuernicaContract.*;

/**
 * Created by root on 23/08/16.
 */
public class GuernicaDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "guernica.db";

    private static final int CUR_DATABASE_VERSION = 1;

    private final Context mContext;

    interface Tables {
        String USER = "users";
        String SHIFT = "shifts";
        String ORDER = "orders";
        String PRODUCT = "products";
        String PRODUCT_TYPE = "product_types";
    }

    interface References {
        String USER_ID = "REFERENCES " + Tables.USER + " (" + BaseColumns._ID + ")";
        String SHIFT_ID = "REFERENCES " + Tables.SHIFT + " (" + BaseColumns._ID + ")";
        String ORDER_ID = "REFERENCES " + Tables.ORDER + " (" + BaseColumns._ID + ")";
        String PRODUCT_TYPE_ID = "REFERENCES " + Tables.PRODUCT_TYPE + " (" + BaseColumns._ID + ")";
    }

    public GuernicaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //USER
        db.execSQL("CREATE TABLE " + Tables.USER + " (" +
                UserEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntity.COLUMN_DISPLAY_NAME + " TEXT NOT NULL, " +
                UserEntity.COLUMN_EMAIL + " TEXT NOT NULL, " +
                UserEntity.COLUMN_AVATAR + " TEXT NOT NULL, " +
                UserEntity.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
            ")"
        );

        //SHIFT
        db.execSQL("CREATE TABLE " + Tables.SHIFT + " (" +
                ShiftEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShiftEntity.COLUMN_SUBMITTED + " INTEGER DEFAULT 0, " +
                ShiftEntity.COLUMN_SHIFT_END + " DATETIME, " +
                ShiftEntity.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                ShiftEntity.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + ShiftEntity.COLUMN_USER_ID + ") " +  References.USER_ID +
            ")"
        );

        //ORDER
        db.execSQL("CREATE TABLE " + Tables.ORDER + " (" +
                OrderEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OrderEntity.COLUMN_SHIFT_ID + " INTEGER NOT NULL, " +
                OrderEntity.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + OrderEntity.COLUMN_SHIFT_ID + ") " +  References.SHIFT_ID +
            ")"
        );

        //PRODUCT
        db.execSQL("CREATE TABLE " + Tables.PRODUCT + " (" +
                ProductEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductEntity.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                ProductEntity.COLUMN_PRODUCT_TYPE_ID + " INTEGER NOT NULL, " +
                ProductEntity.COLUMN_ORDER_ID + " INTEGER NOT NULL, " +
                ProductEntity.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + ProductEntity.COLUMN_ORDER_ID + ") " +  References.ORDER_ID + ", " +
                "FOREIGN KEY (" + ProductEntity.COLUMN_PRODUCT_TYPE_ID + ") " +  References.PRODUCT_TYPE_ID +
            ")"
        );

        //PRODUCT_TYPE
        db.execSQL("CREATE TABLE " + Tables.PRODUCT_TYPE + " (" +
                ProductTypeEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductTypeEntity.COLUMN_NAME + " TEXT NOT NULL, " +
                ProductTypeEntity.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ProductTypeEntity.COLUMN_UNIT_COST + " REAL NOT NULL, " +
                ProductTypeEntity.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
