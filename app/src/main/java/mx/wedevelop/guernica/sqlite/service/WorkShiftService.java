package mx.wedevelop.guernica.sqlite.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.wedevelop.guernica.sqlite.model.WorkShift;
import mx.wedevelop.guernica.sqlite.model.helper.WorkDay;

/**
 * Created by root on 1/11/16.
 */
public class WorkShiftService extends Service {

    private SQLiteDatabase db;

    public WorkShiftService(SQLiteDatabase db) {
        this.db = db;
    }

    public void update(WorkShift ws) {
        db.update(WorkShift.TABLE_NAME, getValues(ws), "id = " + ws.getId(), null);
    }

    public  void save(WorkShift ws) {
        long insertId = db.insert(WorkShift.TABLE_NAME, null, getValues(ws));
        ws.setId((int)insertId);
    }

    public List<WorkShift> findAll() {
        List<WorkShift> list = new ArrayList<WorkShift>();

        Cursor cursor = db.query(WorkShift.TABLE_NAME, WorkShift.TABLE_FIELDS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    public List<WorkDay> findAllWorkDays() {
        List<WorkDay> workDayList = new ArrayList<WorkDay>();
        Map<Integer, WorkDay> map = new HashMap<Integer, WorkDay>();

        Cursor cursor = db.query(WorkShift.TABLE_NAME, WorkShift.TABLE_FIELDS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WorkShift ws = parse(cursor);
            WorkDay wd = null;
            if(map.containsKey(ws.getWeekday())) {
                wd = map.get(ws.getWeekday());
            } else {
                wd = new WorkDay(ws.getWeekday(), ws.getName());
                workDayList.add(wd);
            }
            wd.getWorkShiftList().add(ws);
            map.put(ws.getWeekday(), wd);
            cursor.moveToNext();
        }

        cursor.close();
        return workDayList;
    }

    public void update(List<WorkDay> workDayList) {
        for(WorkDay wd : workDayList) {
            List<WorkShift> workShiftList = wd.getWorkShiftList();
            for(WorkShift ws : workShiftList)
                update(ws);
        }
    }

    public WorkShift get(int id) {
        Cursor cursor = db.query(WorkShift.TABLE_NAME, WorkShift.TABLE_FIELDS, "id = " + id, null, null, null, null);
        cursor.moveToFirst();
        WorkShift ws = parse(cursor);
        cursor.close();
        return ws;
    }

    public WorkShift getCurrentWorkShift() {
        Cursor cursor = db.query(WorkShift.TABLE_NAME, WorkShift.TABLE_FIELDS, "weekday = " + today(), null, null, null, null);
        cursor.moveToFirst();
        WorkShift ws = parse(cursor);
        cursor.close();
        return ws;
    }

    private String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        return sdf.format(d).toLowerCase();
    }

    private WorkShift parse(Cursor cursor) {
        WorkShift ws = new WorkShift();

        String [] columnNames = cursor.getColumnNames();
        for(String columnName : columnNames) {
            int index = cursor.getColumnIndex(columnName);
            switch (columnName) {
                case "id":
                    ws.setId(cursor.getInt(index));
                    break;
                case "name":
                    ws.setName(cursor.getString(index));
                case "weekday":
                    ws.setWeekday(cursor.getInt(index));
                    break;
                case "start_time":
                    ws.setStartTime(parseDate(cursor.getString(index)));
                    break;
                case "end_time":
                    ws.setEndTime(parseDate(cursor.getString(index)));
                    break;
            }
        }

        return ws;
    }

    private ContentValues getValues(WorkShift ws) {
        ContentValues values = new ContentValues();

        values.put("name", ws.getName());
        values.put("weekday",ws.getWeekday());
        values.put("start_time",formatDate(ws.getStartTime()));
        values.put("end_time",formatDate(ws.getEndTime()));

        return values;
    }
}
