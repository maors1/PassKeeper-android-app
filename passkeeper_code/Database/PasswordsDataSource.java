package appdev.sapir.maor.passkeeper.Database;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
public class PasswordsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.PASS_ID, MySQLiteHelper.COLUMN_PASS_NAME, MySQLiteHelper.COLUMN_USER_NAME, MySQLiteHelper.COLUMN_PASS_VALUE, MySQLiteHelper.COLUMN_URL};

    public PasswordsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public PassItem createPassItem(String passName, String userName, String passValue, String passUrl) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PASS_NAME, passName);
        values.put(MySQLiteHelper.COLUMN_USER_NAME, userName);
        values.put(MySQLiteHelper.COLUMN_PASS_VALUE, passValue);
        values.put(MySQLiteHelper.COLUMN_URL, passUrl);
        long insertId = database.insert(MySQLiteHelper.TABLE_PASSWORDS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PASSWORDS,
                allColumns, MySQLiteHelper.PASS_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        PassItem newPassItem = cursorToPassItem(cursor);
        cursor.close();
        return newPassItem;
    }

    public void deletePassItem(PassItem passItem) {
        long id = passItem.getId();
//        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PASSWORDS, MySQLiteHelper.PASS_ID
                + " = " + id, null);
    }

    public boolean updatePassItem(PassItem upItem) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PASS_NAME, upItem.getPassName());
        values.put(MySQLiteHelper.COLUMN_USER_NAME, upItem.getUserName());
        values.put(MySQLiteHelper.COLUMN_PASS_VALUE, upItem.getPassValue());
        values.put(MySQLiteHelper.COLUMN_URL, upItem.getPassUrl());
        String where = "_id = ?";
        String[] whereArgs = {Long.toString(upItem.getId())};
        return database.update(MySQLiteHelper.TABLE_PASSWORDS, values, where, whereArgs) > 0;
    }


    public List<PassItem> getAllPassItems() {
        List<PassItem> passItems = new ArrayList<PassItem>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PASSWORDS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PassItem passItem = cursorToPassItem(cursor);
            passItems.add(passItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return passItems;
    }

    private PassItem cursorToPassItem(Cursor cursor) {
        PassItem passItem = new PassItem();
        passItem.setId(cursor.getLong(0));
        passItem.setPassName(cursor.getString(1));
        passItem.setUserName(cursor.getString(2));
        passItem.setPassValue(cursor.getString(3));
        passItem.setPassUrl(cursor.getString(4));
        return passItem;
    }

    public ArrayList<String> getPasswords() {
        open();
        Cursor cursor = database.query(true, MySQLiteHelper.TABLE_PASSWORDS,
                new String[] {MySQLiteHelper.COLUMN_PASS_VALUE}, null, null, null, null, null, null);
        ArrayList<String> names = new ArrayList<String>();
        cursor.moveToFirst();
        if (cursor != null){
            while(!cursor.isAfterLast()) {
                names.add(cursor.getString(0));
                cursor.moveToNext();
            }
            }
            cursor.close();
        return names;
    }

}


