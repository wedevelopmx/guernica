package mx.wedevelop.guernica.sqlite.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 28/07/16.
 */
public class ReportItem implements Parcelable {

    private int id;
    private String header;
    private String summary;
    private int quantity;
    private double cost;

    public ReportItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    // Parcelling part
    public ReportItem(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.header = data[1];
        this.summary = data[2];
        this.quantity = Integer.parseInt(data[3]);
        this.cost = Double.parseDouble(data[4]);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.id + "",
                this.header,
                this.summary,
                this.quantity + "",
                this.cost + ""
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ReportItem createFromParcel(Parcel in) {
            return new ReportItem(in);
        }

        public ReportItem[] newArray(int size) {
            return new ReportItem[size];
        }
    };
}
