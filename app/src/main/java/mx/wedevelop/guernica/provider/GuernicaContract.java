package mx.wedevelop.guernica.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by root on 23/08/16.
 */
public class GuernicaContract {

    public static final String CONTENT_AUTHORITY = "mx.wedevelop.guernica";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    private interface SyncColumns {
        public String COLUMN_SUBMITTED = "submitted";
    }

    private interface DateColumns {
        public String COLUMN_CREATED_DATE = "created_date";
    }

    private interface UserColumns {
        public String COLUMN_DISPLAY_NAME = "display_name";
        public String COLUMN_EMAIL = "email";
        public String COLUMN_AVATAR = "avatar";
    }

    private interface ShiftColumns {
        public String COLUMN_SHIFT_END = "shift_end";
        public String COLUMN_USER_ID = "user_id";
    }

    private interface OrderColumns {
        public static final String COLUMN_SHIFT_ID = "shift_id";
    }

    private interface ProductColumns {
        public String COLUMN_QUANTITY = "quantity";
        public String COLUMN_ORDER_ID = "order_id";
        public String COLUMN_PRODUCT_TYPE_ID = "product_type_id";
    }

    private interface ProductTypeColumns {
        public String COLUMN_NAME = "name";
        public String COLUMN_DESCRIPTION = "description";
        public String COLUMN_UNIT_COST = "unit_cost";
    }

    public static final class UserEntity implements UserColumns, DateColumns, BaseColumns {

    }

    public static final class ShiftEntity implements ShiftColumns, SyncColumns, DateColumns, BaseColumns {

    }

    public static final class OrderEntity implements OrderColumns, DateColumns, BaseColumns {

    }

    public static final class ProductEntity implements ProductColumns, DateColumns, BaseColumns {

    }

    public static final class ProductTypeEntity implements ProductTypeColumns, DateColumns, BaseColumns {

    }
}
