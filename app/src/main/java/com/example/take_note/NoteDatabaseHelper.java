package com.example.take_note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CREATED_AT = "created_at";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_CREATED_AT + " INTEGER);";

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

        // ✅ Insert default notes
        long now = System.currentTimeMillis();
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TITLE + ", " + COLUMN_CONTENT + ", " + COLUMN_CREATED_AT + ") " +
                "VALUES ('Welcome to DiaryNote!', 'This is your first note. You can write anything here.', " + now + ");");

        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TITLE + ", " + COLUMN_CONTENT + ", " + COLUMN_CREATED_AT + ") " +
                "VALUES ('Tips', 'Click the + button to add a new note. Swipe left to delete a note.', " + now + ");");

        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TITLE + ", " + COLUMN_CONTENT + ", " + COLUMN_CREATED_AT + ") " +
                "VALUES ('Privacy', 'Your notes are stored locally on your device.', " + now + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
