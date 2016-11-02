package mx.wedevelop.guernica.sqlite.model;


import java.util.Date;

/**
 * Created by root on 1/11/16.
 */
public class WorkShift extends Model {
    public static final String TABLE_NAME = "work_shift";
    public static final String[] TABLE_FIELDS = {"id", "name", "weekday", "start_time", "end_time"};


    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int weekday;
    private Date startTime;
    private Date endTime;

    public WorkShift() {

    }

    public WorkShift(String name, int weekday, String shiftStart, String shiftEnd) {
        this.name = name;
        this.weekday = weekday;
        this.startTime = parseHour(shiftStart);
        this.endTime = parseHour(shiftEnd);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStartTimeString() {
        return formatHour(startTime);
    }

    public String getEndTimeString() {
        return formatHour(endTime);
    }

}
