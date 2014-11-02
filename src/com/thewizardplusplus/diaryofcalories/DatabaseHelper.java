package com.thewizardplusplus.diaryofcalories;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {
	public DatabaseHelper(Context context) {
		super(context, "diary_of_calories.db", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(
			"CREATE TABLE day_data_list ("
				+ "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "weight REAL NOT NULL,"
				+ "calories REAL NOT NULL,"
				+ "date TEXT NOT NULL"
			+ ")"
		);
	}

	@Override
	public void onUpgrade(
		SQLiteDatabase database,
		int old_version,
		int new_version
	) {}

	private static final int DATABASE_VERSION = 1;
}
