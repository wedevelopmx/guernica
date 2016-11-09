package mx.wedevelop.guernica.sqlite.model.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.sqlite.model.WorkShift;

/**
 * Created by root on 4/11/16.
 */
public class WorkDay implements Parcelable {

    private String dayName;
    private int weekDay;
    private List<WorkShift> workShiftList;

    public WorkDay(int weekDay, String dayName) {
        this.weekDay = weekDay;
        this.dayName = dayName;
        this.workShiftList = new ArrayList<WorkShift>();
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<WorkShift> getWorkShiftList() {
        return workShiftList;
    }

    public void setWorkShiftList(List<WorkShift> workShiftList) {
        this.workShiftList = workShiftList;
    }

    // Parcelling part
    public WorkDay(Parcel in){
        String[] data = new String[2];
        in.readStringArray(data);
        this.dayName = data[0];
        this.weekDay = Integer.parseInt(data[1]);
        this.workShiftList = new ArrayList<WorkShift>();
        in.readList(this.workShiftList, WorkShift.class.getClassLoader());
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.dayName,
                this.weekDay + ""
        });
        dest.writeList(workShiftList);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WorkDay createFromParcel(Parcel in) {
            return new WorkDay(in);
        }

        public WorkDay[] newArray(int size) {
            return new WorkDay[size];
        }
    };
}
