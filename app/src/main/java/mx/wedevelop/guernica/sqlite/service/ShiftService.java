package mx.wedevelop.guernica.sqlite.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.wedevelop.guernica.sqlite.model.SalesSummary;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.sqlite.model.WorkShift;

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

    private static final String CURRENT_SHIFT = "select ws.id as ws_id, ws.name as ws_name, ws.start_time as ws_start_time, ws.weekday as ws_weekday, " +
            "    ifnull(s.id, 0) as id, " +
            "    ifnull(s.start_time, 0) as start_time, " +
            "    ifnull(s.end_time, 0) as end_time " +
            " from work_shift ws left join shift s " +
            "    on ws.id = s.work_shift_id " +
            "    and strftime('%Y-%m-%d', ?) = strftime('%Y-%m-%d', s.start_time) " +
            " where strftime('%w',?) = ws.weekday " +
            " and (strftime('%H', ws.start_time) * 60)  <= (strftime('%H', ?) * 60) " +
            " and (strftime('%H', ws.end_time) * 60)  > (strftime('%H', ?) * 60) " +
            "limit 1";

    private static final String NEAR_SHIFT = "select ws.id as ws_id, ws.name as ws_name, ws.start_time as ws_start_time, ws.weekday as ws_weekday, " +
            " ifnull(s.id, 0) as id, " +
            " ifnull(s.start_time, 0) as start_time, " +
            " ifnull(s.end_time, 0) as end_time " +
            " from work_shift ws left join shift s " +
            " on ws.id = s.work_shift_id " +
            " and strftime('%Y-%m-%d', ?) = strftime('%Y-%m-%d', s.start_time) " +
            " where strftime('%w',?) = ws.weekday " +
            " order by  abs( " +
            " (strftime('%H', ws.start_time) * 60) + strftime('%M', ws.start_time) - " +
            " ((strftime('%H', ?) * 60) - strftime('%M', ?)) " +
            " ) " +
            " limit 1";

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

    public Shift findOrCreateLatestShift(User currentUser) {
        Shift shift = null;
        Cursor cursor = db.rawQuery(CURRENT_SHIFT, new String[] {formatDate(new Date()), formatDate(new Date()), formatDate(new Date()), formatDate(new Date())});
        if(!cursor.isAfterLast()) {
            cursor.moveToFirst();
            //Process
            shift = parse(cursor);
        }
        cursor.close();

        if(shift == null) {
            cursor = db.rawQuery(NEAR_SHIFT, new String[] {formatDate(new Date()), formatDate(new Date()), formatDate(new Date()), formatDate(new Date())});
            if(!cursor.isAfterLast()) {
                cursor.moveToFirst();
                //Process
                shift = parse(cursor);
            }
            cursor.close();
        }

        //Validate if there is a shift
        if(shift.getId() == 0) {
            WorkShift ws = shift.getWorkShift();
            shift = new Shift();
            shift.setUser(currentUser);
            shift.setWorkShift(ws);
            save(shift);
        }

        return shift;
    }

    public Shift findOrCreateShift(User currentUser, Date date) {
        Shift shift = null;
        Cursor cursor = db.rawQuery(CURRENT_SHIFT, new String[] {formatDate(date), formatDate(date), formatDate(date), formatDate(date)});
        if(!cursor.isAfterLast()) {
            cursor.moveToFirst();
            //Process
            shift = parse(cursor);
        }
        cursor.close();

        //Validate if there is a shift
        if(shift.getId() == 0) {
            WorkShift ws = shift.getWorkShift();
            shift = new Shift();
            shift.setUser(currentUser);
            shift.setWorkShift(ws);
            save(shift);
        }

        return shift;
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
        WorkShift workShift = new WorkShift();

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

                case "ws_id":
                    workShift.setId(cursor.getInt(index));
                    break;
                case "ws_name":
                    workShift.setName(cursor.getString(index));
                    break;
                case "ws_weekday":
                    workShift.setWeekday(cursor.getInt(index));
                    break;
                case "ws_start_time":
                    workShift.setStartTime(parseDate(cursor.getString(index)));
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
        shift.setWorkShift(workShift);

        return shift;
    }

    private ContentValues getValues(Shift shift) {
        ContentValues values = new ContentValues();

        values.put("submitted", shift.getSubmitted());
        values.put("start_time", formatDate(shift.getStartTime()));
        values.put("end_time", shift.getEndTime() != null ? formatDate(shift.getEndTime()): "");
        values.put("user_id", shift.getUser() != null ? shift.getUser().getId() : 0);
        values.put("work_shift_id", shift.getWorkShift() != null ? shift.getWorkShift().getId() : 0);

        return values;
    }

}
