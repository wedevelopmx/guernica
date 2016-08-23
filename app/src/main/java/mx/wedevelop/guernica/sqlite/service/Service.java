package mx.wedevelop.guernica.sqlite.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 22/07/16.
 */
public abstract class Service {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    public abstract long save(Model shift);
//
//    public abstract List<? extends Model> findAll();
//
//    public abstract Model get(int id);

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

    protected String formatDate(Date dateField) {
        return df.format(dateField);
    }

}
