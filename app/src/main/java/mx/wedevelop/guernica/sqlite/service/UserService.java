package mx.wedevelop.guernica.sqlite.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mx.wedevelop.guernica.sqlite.model.User;

/**
 * Created by root on 25/07/16.
 */
public class UserService extends Service {

    private SQLiteDatabase db;

    public UserService(SQLiteDatabase db) {
        this.db = db;
    }

    public User find(int id) {
        User user = null;
        Cursor cursor = db.query(User.TABLE_NAME, User.TABLE_FIELDS, "id = " + id, null, null, null, null, null);
        if(!cursor.isAfterLast()) {
            cursor.moveToFirst();
            user = parse(cursor);
        }
        cursor.close();
        return user;
    }

    public User findByEmail(String email) {
        User user = null;
        Cursor cursor = db.query(User.TABLE_NAME, User.TABLE_FIELDS, "email = '" + email + "'", null, null, null, null, null);
        if(!cursor.isAfterLast()) {
            cursor.moveToFirst();
            user = parse(cursor);
        }
        cursor.close();
        return user;
    }

    public void save(User user) {
        long insertId = db.insert(User.TABLE_NAME, null, getValues(user));
        user.setId((int)insertId);
    }

    public User findOrCreate(User user) {
        User dbUser = findByEmail(user.getEmail());
        if(dbUser == null) {
            save(user);
            dbUser = user;
        }
        return dbUser;
    }

    private ContentValues getValues(User user) {
        ContentValues values = new ContentValues();

        values.put("display_name", user.getDisplayName());
        values.put("email", user.getEmail());
        values.put("avatar", user.getAvatar());
        values.put("created_date", formatDate(user.getCreatedDate()));

        return values;
    }

    private User parse(Cursor cursor) {
        User user = new User();
        String [] columnNames = cursor.getColumnNames();
        for(String columnName : columnNames) {
            int index = cursor.getColumnIndex(columnName);
            switch (columnName) {
                case "id":
                    user.setId(cursor.getInt(index));
                    break;
                case "display_name":
                    user.setDisplayName(cursor.getString(index));
                    break;
                case "email":
                    user.setEmail(cursor.getString(index));
                    break;
                case "avatar":
                    user.setAvatar(cursor.getString(index));
                    break;
                case "created_date":
                    user.setCreatedDate(parseDate(cursor.getString(index)));
                    break;
            }

        }
        return user;
    }

}
