package mx.wedevelop.guernica.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.wedevelop.guernica.LoginActivity;


@RunWith(AndroidJUnit4.class)
public class TestDB extends ActivityInstrumentationTestCase2<LoginActivity> {
    public static final String LOG_TAG = TestDB.class.getSimpleName();

    private GuernicaDBHelper guernicaDBHelper;

    private Context mContext;

    public TestDB() {
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        mContext = new RenamingDelegatingContext(this.getActivity(), "test_");
    }

    @After
    public void tearDown() {

    }

    @Test
    public void schemaCreation() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(GuernicaDBHelper.Tables.USER);
        tableNameHashSet.add(GuernicaDBHelper.Tables.SHIFT);
        tableNameHashSet.add(GuernicaDBHelper.Tables.ORDER);
        tableNameHashSet.add(GuernicaDBHelper.Tables.PRODUCT);
        tableNameHashSet.add(GuernicaDBHelper.Tables.PRODUCT_TYPE);

        guernicaDBHelper = new GuernicaDBHelper(mContext);
        SQLiteDatabase db = guernicaDBHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables", tableNameHashSet.isEmpty());


        userTableCreation(db);
        shiftTableCreation(db);
        orderTableCreation(db);
        productTableCreation(db);
        productTypeTableCreation(db);

        db.close();
    }

    public void userTableCreation(SQLiteDatabase db) {
        Cursor c = loadTableInfo(db, GuernicaDBHelper.Tables.USER);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(GuernicaContract.UserEntity._ID);
        locationColumnHashSet.add(GuernicaContract.UserEntity.COLUMN_DISPLAY_NAME);
        locationColumnHashSet.add(GuernicaContract.UserEntity.COLUMN_EMAIL);
        locationColumnHashSet.add(GuernicaContract.UserEntity.COLUMN_AVATAR);
        locationColumnHashSet.add(GuernicaContract.UserEntity.COLUMN_CREATED_DATE);

        validateTable(GuernicaDBHelper.Tables.USER, locationColumnHashSet, c);

    }

    public void shiftTableCreation(SQLiteDatabase db) {
        Cursor c = loadTableInfo(db, GuernicaDBHelper.Tables.SHIFT);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(GuernicaContract.ShiftEntity._ID);
        locationColumnHashSet.add(GuernicaContract.ShiftEntity.COLUMN_SHIFT_END);
        locationColumnHashSet.add(GuernicaContract.ShiftEntity.COLUMN_USER_ID);
        locationColumnHashSet.add(GuernicaContract.ShiftEntity.COLUMN_SUBMITTED);
        locationColumnHashSet.add(GuernicaContract.UserEntity.COLUMN_CREATED_DATE);

        validateTable(GuernicaDBHelper.Tables.SHIFT, locationColumnHashSet, c);

    }

    public void orderTableCreation(SQLiteDatabase db) {
        Cursor c = loadTableInfo(db, GuernicaDBHelper.Tables.ORDER);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(GuernicaContract.OrderEntity._ID);
        locationColumnHashSet.add(GuernicaContract.OrderEntity.COLUMN_SHIFT_ID);
        locationColumnHashSet.add(GuernicaContract.OrderEntity.COLUMN_CREATED_DATE);

        validateTable(GuernicaDBHelper.Tables.ORDER, locationColumnHashSet, c);

    }

    public void productTableCreation(SQLiteDatabase db) {
        Cursor c = loadTableInfo(db, GuernicaDBHelper.Tables.PRODUCT);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(GuernicaContract.ProductEntity._ID);
        locationColumnHashSet.add(GuernicaContract.ProductEntity.COLUMN_QUANTITY);
        locationColumnHashSet.add(GuernicaContract.ProductEntity.COLUMN_ORDER_ID);
        locationColumnHashSet.add(GuernicaContract.ProductEntity.COLUMN_PRODUCT_TYPE_ID);
        locationColumnHashSet.add(GuernicaContract.ProductEntity.COLUMN_CREATED_DATE);

        validateTable(GuernicaDBHelper.Tables.PRODUCT, locationColumnHashSet, c);

    }

    public void productTypeTableCreation(SQLiteDatabase db) {
        Cursor c = loadTableInfo(db, GuernicaDBHelper.Tables.PRODUCT_TYPE);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(GuernicaContract.ProductTypeEntity._ID);
        locationColumnHashSet.add(GuernicaContract.ProductTypeEntity.COLUMN_NAME);
        locationColumnHashSet.add(GuernicaContract.ProductTypeEntity.COLUMN_DESCRIPTION);
        locationColumnHashSet.add(GuernicaContract.ProductTypeEntity.COLUMN_UNIT_COST);
        locationColumnHashSet.add(GuernicaContract.ProductTypeEntity.COLUMN_CREATED_DATE);

        validateTable(GuernicaDBHelper.Tables.PRODUCT_TYPE, locationColumnHashSet, c);

    }

    public Cursor loadTableInfo(SQLiteDatabase db, String tableName) {
        // now, do our tables contain the correct columns?
        Cursor c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information [" + tableName + "]." , c.moveToFirst());

        return c;
    }

    public void validateTable(String tableName, HashSet<String> locationColumnHashSet, Cursor c) {
        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns [" + tableName + "]", locationColumnHashSet.isEmpty());
    }

    @Test
    public void testInsert() {
        guernicaDBHelper = new GuernicaDBHelper(mContext);
        SQLiteDatabase db = guernicaDBHelper.getWritableDatabase();

        //Generating the product type catalog
        List<ContentValues> productTypeCatalog = TestUtils.mockProductTypeCatalog();
        List<Long> productTypeCatalogId = testTable(db, GuernicaDBHelper.Tables.PRODUCT_TYPE, productTypeCatalog);

        //Inserting a user
        long userId = testTable(db, GuernicaDBHelper.Tables.USER, TestUtils.mockUser());
        //Inserting a new Shift
        long shiftId = testTable(db, GuernicaDBHelper.Tables.SHIFT, TestUtils.mockShift(userId));
        //Inserting a new Order
        long orderId = testTable(db, GuernicaDBHelper.Tables.ORDER, TestUtils.mockOrder(shiftId));
        //Iserting order products
        List<ContentValues> productList = TestUtils.mockOrderContent(orderId, productTypeCatalogId);
        testTable(db, GuernicaDBHelper.Tables.PRODUCT, productList);

        db.close();
    }

    public List<Long> testTable(SQLiteDatabase db, String tableName, List<ContentValues> recordList) {
        List<Long> productTypeCatalogId = new ArrayList<Long>();
        HashMap<String, ContentValues> productHash = new HashMap<String, ContentValues>();

        for(ContentValues values : recordList) {
            long productTypeId = db.insert(tableName, null, values);
            assertTrue(productTypeId != -1);

            productHash.put(productTypeId + "", values);

            productTypeCatalogId.add(productTypeId);
        }

        Cursor c = db.query(tableName, null, null, null, null, null, null);

        assertTrue("Error: This means that we were unable to query the database for table information [" + tableName + "]." , c.moveToFirst());

        while(c.moveToNext()) {
            int index = c.getColumnIndex(GuernicaContract.ProductTypeEntity._ID);
            String id = c.getString(index);
            ContentValues value = productHash.get(id);

            assertTrue("Could not find a table record into our test data for " + tableName, value != null);

            validateCurrentRecord("testInsertReadDb " + tableName + " failed to validate", c, value);

            productHash.remove(id);
        }

        assertFalse( "Error: Could not find all reccords into " + tableName, productHash.isEmpty());

        c.close();

        return productTypeCatalogId;
    }

    public long testTable(SQLiteDatabase db, String tableName, ContentValues values) {
        long result = db.insert(tableName, null, values);

        assertTrue(result != -1);

        Cursor c = db.query(tableName, null, null, null, null, null, null);

        assertTrue("Error: This means that we were unable to query the database for table information [" + tableName + "]." , c.moveToFirst());

        validateCurrentRecord("testInsertReadDb " + tableName + " failed to validate", c, values);

        assertFalse( "Error: More than one record returned from " + tableName + " query", c.moveToNext() );

        c.close();

        return result;
    }

    void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();

            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);

            String expectedValue = entry.getValue().toString();
            String cursorValue = "";
            switch (valueCursor.getType(idx)) {
                case Cursor.FIELD_TYPE_FLOAT:
                    cursorValue = valueCursor.getFloat(idx) + "";
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    cursorValue = valueCursor.getInt(idx) + "";
                    break;
                default:
                    cursorValue = valueCursor.getString(idx);
            }

            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, cursorValue);
        }
    }
}
