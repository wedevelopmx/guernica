package mx.wedevelop.guernica.sqlite.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/**
 * Created by root on 23/07/16.
 */

//    create table product_type (
//        id integer primary key autoincrement,
//        name text not null,
//        unit_cost real not null
//    )


public class ProductType implements Parcelable {
    public static final String TABLE_NAME = "product_type";
    public static final String[] TABLE_FIELDS = {"id", "name", "description", "unit_cost", "created_date"};

    private int id;
    private String name;
    private String description;
    private double unitCost;
    private Date createdDate;
    private int quantity;

    public ProductType() {
        this.createdDate = new Date();
    }

    public ProductType(String name, String description, double unitCost) {
        this.name = name;
        this.description = description;
        this.unitCost = unitCost;
        this.createdDate = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // Parcelling part
    public ProductType(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.description = data[2];
        this.unitCost = Double.parseDouble(data[3]);
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
                this.description,
                this.unitCost + ""
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ProductType createFromParcel(Parcel in) {
            return new ProductType(in);
        }

        public ProductType[] newArray(int size) {
            return new ProductType[size];
        }
    };
}
