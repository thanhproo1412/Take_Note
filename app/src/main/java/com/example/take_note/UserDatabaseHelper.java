package com.example.take_note;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TakeNoteDB";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_DOB + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_ADDRESS + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Thêm tài khoản mặc định admin123
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin123");
        values.put(COLUMN_EMAIL, "admin@example.com");
        values.put(COLUMN_PASSWORD, "admin123");
        values.put(COLUMN_NAME, "Admin");
        values.put(COLUMN_GENDER, "Male");
        values.put(COLUMN_DOB, "01/01/1990");
        values.put(COLUMN_PHONE, "0123456789");
        values.put(COLUMN_ADDRESS, "Hanoi");
        db.insert(TABLE_USERS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        // Có thể thêm default cho các cột mới
        values.put(COLUMN_NAME, username); // hoặc để trống
        values.put(COLUMN_GENDER, "");
        values.put(COLUMN_DOB, "");
        values.put(COLUMN_PHONE, "");
        values.put(COLUMN_ADDRESS, "");
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // ✅ Get user info by username
// Thêm vào cuối class UserDatabaseHelper
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});

        User user = null;

        if (cursor.moveToFirst()) {
            user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));

            // Các thông tin còn lại nếu bạn có thêm cột (name, gender, dob, phone, address)
            // Ví dụ:
            int nameIndex = cursor.getColumnIndex("name");
            if (nameIndex != -1) user.setName(cursor.getString(nameIndex));

            int genderIndex = cursor.getColumnIndex("gender");
            if (genderIndex != -1) user.setGender(cursor.getString(genderIndex));

            int dobIndex = cursor.getColumnIndex("dob");
            if (dobIndex != -1) user.setDob(cursor.getString(dobIndex));

            int phoneIndex = cursor.getColumnIndex("phone");
            if (phoneIndex != -1) user.setPhone(cursor.getString(phoneIndex));

            int addressIndex = cursor.getColumnIndex("address");
            if (addressIndex != -1) user.setAddress(cursor.getString(addressIndex));
        }

        cursor.close();
        db.close();

        return user;
    }
}
