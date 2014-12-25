package com.thewizardplusplus.diaryofcalories;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class DataAccessor {
	public static synchronized DataAccessor getInstance(Context context) {
		if (instance == null) {
			instance = new DataAccessor(context);
		}

		return instance;
	}

	public int getNumberOfCurrentDayData() {
		SQLiteDatabase database = database_helper.getWritableDatabase();

		Cursor cursor = database.rawQuery(
			"SELECT COUNT(*)"
			+ "FROM day_data_list "
			+ "WHERE"
				+ "(SELECT date(date))"
				+ "= (SELECT date('now', 'localtime'))",
			null
		);

		cursor.moveToFirst();
		int number = cursor.getInt(0);
		cursor.close();

		database.close();
		return number;
	}

	public DayData getCurrentDayData() {
		SQLiteDatabase database = database_helper.getWritableDatabase();

		Cursor cursor = database.rawQuery(
			"SELECT"
				+ "(SELECT date('now', 'localtime')),"
				+ "SUM(weight * calories / 100.0)"
			+ "FROM day_data_list "
			+ "WHERE"
				+ "(SELECT date(date))"
				+ "= (SELECT date('now', 'localtime'))",
			null
		);

		cursor.moveToFirst();
		DayData day_data = new DayData();
		day_data.date = cursor.getString(0);
		day_data.calories = cursor.getDouble(1);
		cursor.close();

		database.close();
		return day_data;
	}

	public List<DayData> getAllDaysData() {
		SQLiteDatabase database = database_helper.getWritableDatabase();

		Cursor cursor = database.rawQuery(
			"SELECT"
				+ "(SELECT date(date)) AS 'date_field',"
				+ "SUM(weight * calories / 100.0)"
			+ "FROM day_data_list "
			+ "GROUP BY (SELECT date(date))"
			+ "ORDER BY date_field DESC",
			null
		);

		cursor.moveToFirst();
		List<DayData> day_data_list = new ArrayList<DayData>();
		while (!cursor.isAfterLast()) {
			DayData day_data = new DayData();
			day_data.date = cursor.getString(0);
			day_data.calories = cursor.getDouble(1);
			day_data_list.add(day_data);

			cursor.moveToNext();
		}
		cursor.close();

		database.close();
		return day_data_list;
	}

	public String getAllDataInXml() {
		SQLiteDatabase database = database_helper.getWritableDatabase();

		Cursor cursor = database.rawQuery(
			"SELECT "
				+ "_id,"
				+ "weight,"
				+ "calories,"
				+ "(SELECT date(date)) AS 'date_field'"
			+ "FROM day_data_list "
			+ "ORDER BY date_field, _id",
			null
		);

		cursor.moveToFirst();
		String xml =
			"<?xml version = \"1.0\" encoding = \"utf-8\" ?>\n"
			+ "<history>\n";

		boolean has_rows = false;
		boolean not_first_loop = false;
		String last_date = "";
		while (!cursor.isAfterLast()) {
			String current_date = cursor.getString(3);
			if (!current_date.equals(last_date)) {
				if (not_first_loop) {
					xml += "\t</day>\n";
				}
				xml += "\t<day date = \"" + current_date + "\">\n";

				last_date = current_date;
			}

			xml += "\t\t<food "
				+ "weight = \"" + cursor.getString(1) + "\" "
				+ "calories = \"" + cursor.getString(2) + "\" />\n";

			has_rows = true;
			not_first_loop = true;
			cursor.moveToNext();
		}

		if (has_rows) {
			xml += "\t</day>\n";
		}
		xml += "</history>\n";
		cursor.close();

		database.close();
		return xml;
	}

	public void setDataFromXml(InputStream backup_file) {}

	public Settings getSettings() {
		Settings settings = new Settings();
		SharedPreferences preferences =
			PreferenceManager.getDefaultSharedPreferences(context);
		settings.view_mode = HistoryViewMode.valueOf(
			preferences.getString(
				Settings.VIEW_MODE_SETTING_NAME,
				Settings.DEFAULT_VIEW_MODE.toString()
			)
		);
		settings.hard_limit = preferences.getFloat(
			Settings.HARD_LIMIT_SETTING_NAME,
			Settings.DEFAULT_HARD_LIMIT
		);
		settings.soft_limit = preferences.getFloat(
			Settings.SOFT_LIMIT_SETTING_NAME,
			Settings.DEFAULT_SOFT_LIMIT
		);

		return settings;
	}

	public void setSettings(Settings settings) {
		SharedPreferences preferences =
			PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor preferences_editor = preferences.edit();
		preferences_editor.putString(
			Settings.VIEW_MODE_SETTING_NAME,
			settings.view_mode.toString()
		);
		preferences_editor.putFloat(
			Settings.HARD_LIMIT_SETTING_NAME,
			settings.hard_limit
		);
		preferences_editor.putFloat(
			Settings.SOFT_LIMIT_SETTING_NAME,
			settings.soft_limit
		);
		preferences_editor.commit();
	}

	public void addData(float weight, float calories) {
		SQLiteDatabase database = database_helper.getWritableDatabase();
		database.execSQL(
			"INSERT INTO day_data_list"
				+ "(weight, calories, date)"
			+ "VALUES ("
				+ String.valueOf(weight) + ","
				+ String.valueOf(calories) + ","
				+ "(SELECT datetime('now', 'localtime'))"
			+ ")"
		);
		database.close();
	}

	public void undoTheLast() {
		if (getNumberOfCurrentDayData() <= 0) {
			return;
		}

		SQLiteDatabase database = database_helper.getWritableDatabase();
		database.execSQL(
			"DELETE FROM day_data_list "
			+ "WHERE"
				+ "(SELECT datetime(date))"
				+ "= ("
					+ "SELECT MAX((SELECT datetime(date)))"
					+ "FROM day_data_list"
				+ ")"
		);
		database.close();
	}

	private static DataAccessor instance;

	private Context context;
	private DatabaseHelper database_helper;

	private DataAccessor(Context context) {
		this.context = context;
		database_helper = new DatabaseHelper(context);
	}
}
