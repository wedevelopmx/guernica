package mx.wedevelop.guernica.sqlite.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.ReportItem;

/**
 * Created by root on 28/07/16.
 */
public class ReportService extends Service {
    private static final String DAILY_SUMMARY =
            "        select  strftime('%d/%m/%Y', s.start_time) as header, round(sum(pt.unit_cost * p.quantity) / count(o.id), 2) as summary, sum(pt.unit_cost * p.quantity) as cost, sum(p.quantity) as quantity" +
            "        from shift s, orders o, product p, product_type pt" +
            "        where s.id = o.shift_id" +
            "        and o.id = p.order_id" +
            "        and p.product_type_id = pt.id" +
            "        and s.submitted = 0" +
            "        and s.end_time is not null" +
            "        and s.end_time != ''" +
            "        group by strftime('%Y-%m-%d', s.start_time) " +
            "        order by strftime('%Y%m%d', s.start_time) desc";

    private static final String WEEKLY_SUMMARY =
            "        select  'FW' || strftime('%W', s.start_time) as header, round(sum(pt.unit_cost * p.quantity) / count(o.id), 2) as summary, sum(pt.unit_cost * p.quantity) as cost, sum(p.quantity) as quantity" +
            "        from shift s, orders o, product p, product_type pt" +
            "        where s.id = o.shift_id" +
            "        and o.id = p.order_id" +
            "        and p.product_type_id = pt.id" +
            "        and s.submitted = 0" +
            "        and s.end_time is not null" +
            "        and s.end_time != ''" +
            "        group by strftime('%W', s.start_time) " +
                    "order by strftime('%W', s.start_time) desc";

    private static final String MONTHLY_SUMMARY =
            "        select  strftime('%m/%Y', s.start_time) as header, round(sum(pt.unit_cost * p.quantity) / count(o.id), 2) as summary, sum(pt.unit_cost * p.quantity) as cost, sum(p.quantity) as quantity" +
            "        from shift s, orders o, product p, product_type pt" +
            "        where s.id = o.shift_id" +
            "        and o.id = p.order_id" +
            "        and p.product_type_id = pt.id" +
            "        and s.submitted = 0" +
            "        and s.end_time is not null" +
            "        and s.end_time != ''" +
            "        group by strftime('%m/%Y', s.start_time) " +
            "        order by strftime('%Y%m', s.start_time) desc";


    private SQLiteDatabase db;

    public ReportService(SQLiteDatabase db) {
        this.db = db;
    }

    public List<ReportItem> findDailySummary() {
        return execReport(DAILY_SUMMARY);
    }

    public List<ReportItem> findWeeklySummary() {
        return execReport(WEEKLY_SUMMARY);
    }

    public List<ReportItem> findMonthlySummary() {
        return execReport(MONTHLY_SUMMARY);
    }

    private List<ReportItem> execReport(String query) {
        List<ReportItem> list = new ArrayList<ReportItem>();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    private ReportItem parse(Cursor cursor) {
        ReportItem reportItem = new ReportItem();
        String [] columnNames = cursor.getColumnNames();
        for(String columnName : columnNames) {
            int index = cursor.getColumnIndex(columnName);
            switch (columnName) {
                case "id":
                    reportItem.setId(cursor.getInt(index));
                    break;
                case "header":
                    reportItem.setHeader(cursor.getString(index));
                    break;
                case "summary":
                    reportItem.setSummary(cursor.getString(index));
                    break;
                case "quantity":
                    reportItem.setQuantity(cursor.getInt(index));
                    break;
                case "cost":
                    reportItem.setCost(cursor.getDouble(index));
                    break;
            }
        }
        return reportItem;
    }


    public ContentValues getValues(ReportItem reportItem) {
        ContentValues values = new ContentValues();

        values.put("header", reportItem.getHeader());
        values.put("summary", reportItem.getSummary());
        values.put("quantity", reportItem.getQuantity());
        values.put("cost", reportItem.getCost());

        return values;
    }
}
