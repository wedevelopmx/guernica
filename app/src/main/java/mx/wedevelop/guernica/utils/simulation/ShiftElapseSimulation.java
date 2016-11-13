package mx.wedevelop.guernica.utils.simulation;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 13/11/16.
 */
public class ShiftElapseSimulation extends DayElapseSimulation {
    public ShiftElapseSimulation(Date simEndDate, int months) {
        super(simEndDate, months);
    }

    public Date today() {
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        return super.today();
    }

    public Date afternoonShift() {
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        return calendar.getTime();
    }
}