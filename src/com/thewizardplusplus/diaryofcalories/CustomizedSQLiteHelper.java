package com.thewizardplusplus.diaryofcalories;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class CustomizedSQLiteHelper extends SQLiteOpenHelper {
	public static final String   DATABASE_NAME =         "diary_of_calories." +
		"db";
	public static final int      DATABASE_VERSION =      1;
	public static final String   TABLE_NAME =            "day_data_list";
	public static final String   COLUMN_ID =             "_id";
	public static final String   COLUMN_WEIGHT =         "weight";
	public static final String   COLUMN_CALORIES =       "calories";
	public static final String   COLUMN_DATE =           "date";
	public static final String[] ALL_COLUMNS =
		{COLUMN_ID, COLUMN_WEIGHT, COLUMN_CALORIES, COLUMN_DATE};
	public static final String   DATABASE_CREATE_QUERY =
		"CREATE TABLE " + TABLE_NAME + "(" +
		COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		COLUMN_WEIGHT + " REAL NOT NULL, " +
		COLUMN_CALORIES + " REAL NOT NULL, " +
		COLUMN_DATE + " TEXT NOT NULL);";
	public static final String   DATABASE_DELETE_QUERY = "DROP TABLE IF EXISTS"
		+ " " + TABLE_NAME + ";";

	public CustomizedSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int old_version,
		int new_version)
	{
		database.execSQL(DATABASE_DELETE_QUERY);
		onCreate(database);
	}
}
