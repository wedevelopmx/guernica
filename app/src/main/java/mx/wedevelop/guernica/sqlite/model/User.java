package mx.wedevelop.guernica.sqlite.model;

import java.util.Date;

/**
 * Created by root on 24/07/16.
 */

//    create table user (
//        id integer primary key autoincrement,
//        display_name text not null,
//        email text not null,
//        avatar text not null
//    )

public class User {
    public static final String TABLE_NAME = "user";
    public static final String[] TABLE_FIELDS = {"id", "display_name", "email", "avatar"};

    private int id;
    private String displayName;
    private String email;
    private String avatar;
    private Date createdDate;

    public User() {
        this.createdDate = new Date();
    }

    public User(String displayName, String email, String avatar) {
        this.displayName = displayName;
        this.email = email;
        this.avatar = avatar;
        this.createdDate = new Date();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
