package mx.wedevelop.guernica.sqlite.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by root on 1/11/16.
 */
public class WorkShift extends Model implements Parcelable {
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

    // Parcelling part
    public WorkShift(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.weekday = Integer.parseInt(data[2]);
        this.startTime = parseHour(data[3]);
        this.endTime = parseHour(data[4]);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.id + "",
                this.name,
                this.weekday + "",
                formatHour(this.startTime),
                formatHour(this.endTime)
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WorkShift createFromParcel(Parcel in) {
            return new WorkShift(in);
        }

        public WorkShift[] newArray(int size) {
            return new WorkShift[size];
        }
    };

}
