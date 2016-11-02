package mx.wedevelop.guernica.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 22/07/16.
 */
public abstract class Model {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    SimpleDateFormat hf = new SimpleDateFormat("HH:mm");

    public abstract void setId(int id);
   // public abstract ContentValues getValues();

    protected Date parseDate(String dateField) {
        java.util.Date date = null;
        try {
            if (dateField != null && !dateField.isEmpty())
                date = df.parse(dateField);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return date;
        }
    }

    protected Date parseHour(String dateField) {
        java.util.Date date = null;
        try {
            if (dateField != null && !dateField.isEmpty())
                date = hf.parse(dateField);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return date;
        }
    }

    protected String formatDate(Date dateField) {
        return df.format(dateField);
    }

    protected String formatHour(Date dateField) {
        return hf.format(dateField);
    }
}
