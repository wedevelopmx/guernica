package mx.wedevelop.guernica.provider;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mx.wedevelop.guernica.provider.GuernicaContract.*;

/**
 * Created by root on 25/08/16.
 */
public class TestUtils {

    static int randomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }

    static ContentValues mockUser() {
        ContentValues values = new ContentValues();
        values.put(UserEntity.COLUMN_DISPLAY_NAME,"Guernica");
        values.put(UserEntity.COLUMN_EMAIL,"contact@aguasguernica.com");
        values.put(UserEntity.COLUMN_AVATAR,"http://wedevelop.mx/assets/guenica.png");
        return values;
    }

    static ContentValues mockShift(long userId) {
        ContentValues values = new ContentValues();
        values.put(ShiftEntity.COLUMN_USER_ID, userId);
        return values;
    }

    static ContentValues mockOrder(long shiftId) {
        ContentValues values = new ContentValues();
        values.put(OrderEntity.COLUMN_SHIFT_ID, shiftId);
        return values;
    }

    private static ContentValues mockProduct(long orderId, long productType, int quantity) {
        ContentValues values = new ContentValues();
        values.put(ProductEntity.COLUMN_ORDER_ID, orderId);
        values.put(ProductEntity.COLUMN_PRODUCT_TYPE_ID, productType);
        values.put(ProductEntity.COLUMN_QUANTITY, quantity);
        return values;
    }

    private static ContentValues mockProductType(String name, String desc, double unitCost) {
        ContentValues values = new ContentValues();
        values.put(ProductTypeEntity.COLUMN_NAME, name);
        values.put(ProductTypeEntity.COLUMN_DESCRIPTION, desc);
        values.put(ProductTypeEntity.COLUMN_UNIT_COST, unitCost);
        return values;
    }

    static List<ContentValues> mockProductTypeCatalog() {
        List<ContentValues> list = new ArrayList<ContentValues>();
        list.add(mockProductType("Ciel", "Ciel 20L", 10.0));
        list.add(mockProductType("Electropura", "Electropura 20L", 10.0));
        list.add(mockProductType("Bonoafont", "Bonafont 20L", 10.0));
        return list;
    }

    static List<ContentValues> mockOrderContent(long orderId, List<Long> productTypeCatalog) {
        List<ContentValues> list = new ArrayList<ContentValues>();

        int i;
        for(i = randomNumber(1, 4); i > 0 ; i --) {
            list.add(
                    mockProduct(
                            orderId,
                            productTypeCatalog.get(randomNumber(0, productTypeCatalog.size())),
                            randomNumber(1,3)
                    )
            );
        }

        return list;
    }


}
