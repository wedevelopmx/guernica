package mx.wedevelop.guernica.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 26/07/16.
 */
public class Utils {
    private static final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static final SimpleDateFormat dfHour = new SimpleDateFormat("HH:mm a");
    private static final SimpleDateFormat dfDay = new SimpleDateFormat("EEE, MMM d, yyyy");

    public static Date parseDate(String dateField) {
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

    public static String formatDateHour(Date dateField) {
        return df.format(dateField);
    }

    public static String formatHour(Date dateField) {
        return dfHour.format(dateField);
    }

    public static String formatDate(Date dateField) {
        return dfDay.format(dateField);
    }
}
