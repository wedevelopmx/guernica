package mx.wedevelop.guernica.sqlite.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.sqlite.model.Model;
import mx.wedevelop.guernica.sqlite.model.SalesSummary;
import mx.wedevelop.guernica.sqlite.model.Shift;

/**
 * Created by root on 22/07/16.
 */
public class ShiftService extends Service {

    private static final String UNSUBMITTED_SHIFT_REPORT = "select s.id, s.submitted, s.start_time, s.end_time, sum(p.quantity) as quantity, sum(p.quantity * pt.unit_cost) as cost" +
            "    from shift s, orders o, product p, product_type pt" +
            "    where s.id = o.shift_id" +
            "    and o.id = p.order_id and p.product_type_id = pt.id" +
            "    and s.submitted = 0" +
            "    and s.end_time != ''" +
            "    group by s.id" +
            "    order by s.id desc";

    private SQLiteDatabase db;

    public ShiftService(SQLiteDatabase db) {
        this.db = db;
    }

    public void update(Shift shift) {
        db.update(Shift.TABLE_NAME, getValues(shift), "id = " + shift.getId(), null);
    }

    public void save(Shift shift) {
        long insertId = db.insert(Shift.TABLE_NAME, null, getValues(shift));
        shift.setId((int)insertId);
    }

    public Shift getLatestShift() {
        Shift shif = null;
        Cursor cursor = db.query(Shift.TABLE_NAME, Shift.TABLE_FIELDS, null, null, null, null, "id desc", "1");
        if(!cursor.isAfterLast()) {
            cursor.moveToFirst();
            shif = parse(cursor);
        }
        cursor.close();
        return shif;
    }

    public List<Shift> findAllUnsubmitted() {
        List<Shift> list = new ArrayList<Shift>();

        Cursor cursor = db.rawQuery(UNSUBMITTED_SHIFT_REPORT, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    public List<Shift> findAll() {
        List<Shift> list = new ArrayList<Shift>();

        Cursor cursor = db.query(Shift.TABLE_NAME, Shift.TABLE_FIELDS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    public Shift get(int id) {
        Cursor cursor = db.query(Shift.TABLE_NAME, Shift.TABLE_FIELDS, "id = " + id, null, null, null, null);
        cursor.moveToFirst();
        Shift shift = parse(cursor);
        cursor.close();
        return shift;
    }

    private Shift parse(Cursor cursor) {
        Shift shift = new Shift();
        SalesSummary salesSummary = new SalesSummary();

        String [] columnNames = cursor.getColumnNames();
        for(String columnName : columnNames) {
            int index = cursor.getColumnIndex(columnName);
            switch (columnName) {
                case "id":
                    shift.setId(cursor.getInt(index));
                    break;
                case "submitted":
                    shift.setSubmitted(cursor.getInt(index));
                    break;
                case "start_time":
                    shift.setStartTime(parseDate(cursor.getString(index)));
                    break;
                case "end_time":
                    shift.setEndTime(parseDate(cursor.getString(index)));
                    break;

                //Summary Object for reporting
                case "quantity":
                    salesSummary.setQuantity(cursor.getInt(index));
                    break;
                case "cost":
                    salesSummary.setCost(cursor.getDouble(index));
                    break;
            }
        }

        shift.setSalesSummary(salesSummary);

        return shift;
    }

    private ContentValues getValues(Shift shift) {
        ContentValues values = new ContentValues();

        values.put("submitted", shift.getSubmitted());
        values.put("start_time", formatDate(shift.getStartTime()));
        values.put("end_time", shift.getEndTime() != null ? formatDate(shift.getEndTime()): "");
        values.put("user_id", shift.getUser() != null ? shift.getUser().getId() : 0);

        return values;
    }

}
