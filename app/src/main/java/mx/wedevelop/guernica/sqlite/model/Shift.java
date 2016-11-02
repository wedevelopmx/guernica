package mx.wedevelop.guernica.sqlite.model;

import android.content.ContentValues;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by root on 22/07/16.
 */

//    create table shift (
//        id integer primary key autoincrement,
//        submited integer default 0,
//        start_time datetime default current_timestamp,
//        end_time datetime,
//        user_id integer not null,
//        foreign key(user_id) references user(id)
//    )

public class Shift {

    public static final String TABLE_NAME = "shift";
    public static final String[] TABLE_FIELDS = {"id", "submitted", "start_time", "end_time", "user_id"};

    private int id;
    private int submitted;
    private Date startTime;
    private Date endTime;
    private User user;

    private SalesSummary salesSummary;
    private WorkShift workShift;

    public Shift() {
        this.submitted = 0;
        this.startTime = new Date();
    }

    public Shift(int id, int submited, Date startTime, Date endTime) {
        this.id = id;
        this.submitted = submited;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public int getSubmitted() {
        return submitted;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setSubmitted(int submited) {
        this.submitted = submited;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SalesSummary getSalesSummary() {
        return salesSummary;
    }

    public void setSalesSummary(SalesSummary salesSummary) {
        this.salesSummary = salesSummary;
    }

    public WorkShift getWorkShift() {
        return workShift;
    }

    public void setWorkShift(WorkShift workShift) {
        this.workShift = workShift;
    }

}
