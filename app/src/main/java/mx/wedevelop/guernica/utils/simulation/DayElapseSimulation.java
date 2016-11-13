package mx.wedevelop.guernica.utils.simulation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 13/11/16.
 */
public class DayElapseSimulation {
    private Calendar endDate;
    protected Calendar calendar;

    public DayElapseSimulation(Date simEndDate, int months) {
        endDate = Calendar.getInstance();
        endDate.setTime(simEndDate);
        calendar = Calendar.getInstance();
        calendar.setTime(simEndDate);
        calendar.add(Calendar.MONTH, -months);
    }

    public Date today() {
        return calendar.getTime();
    }

    public boolean tomorrow() {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return !(calendar.get(Calendar.YEAR) == endDate.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == endDate.get(Calendar.DAY_OF_YEAR));
    }
}

