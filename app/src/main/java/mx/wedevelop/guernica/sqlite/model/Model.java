package mx.wedevelop.guernica.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/07/16.
 */
public interface Model {

    public void setId(int id);
    public ContentValues getValues();
}
