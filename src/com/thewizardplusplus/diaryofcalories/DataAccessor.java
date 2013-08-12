package com.thewizardplusplus.diaryofcalories;

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
		SQLiteDatabase database = sqlite_helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " +
			CustomizedSQLiteHelper.TABLE_NAME + " WHERE (SELECT date(" +
			CustomizedSQLiteHelper.COLUMN_DATE + ")) = " + "(SELECT date(" +
			"'now', 'localtime'))", null);
		cursor.moveToFirst();
		int number = cursor.getInt(0);
		cursor.close();
		database.close();
		return number;
	}

	public DayData getCurrentDayData() {
		SQLiteDatabase database = sqlite_helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("SELECT (SELECT date('now', " +
			"'localtime')) AS 'date_field', SUM(" +
			CustomizedSQLiteHelper.COLUMN_WEIGHT + " / 100.0 * " +
			CustomizedSQLiteHelper.COLUMN_CALORIES + ") AS 'calories' FROM " +
			CustomizedSQLiteHelper.TABLE_NAME + " WHERE (SELECT date(" +
			CustomizedSQLiteHelper.COLUMN_DATE + ")) = (SELECT date('now',"
			+ " 'localtime'))", null);
		cursor.moveToFirst();
		DayData day_data = new DayData();
		day_data.date = cursor.getString(cursor.getColumnIndex("date_field"));
		day_data.calories = cursor.getDouble(cursor.getColumnIndex(
			"calories"));
		cursor.close();
		database.close();
		return day_data;
	}

	public List<DayData> getAllDaysData() {
		List<DayData> day_data_list = new ArrayList<DayData>();
		SQLiteDatabase database = sqlite_helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("SELECT (SELECT date(" +
			CustomizedSQLiteHelper.COLUMN_DATE + ")) AS 'date_field', SUM(" +
			CustomizedSQLiteHelper.COLUMN_WEIGHT + " / 100.0 * " +
			CustomizedSQLiteHelper.COLUMN_CALORIES + ") AS 'calories' FROM " +
			CustomizedSQLiteHelper.TABLE_NAME + " GROUP BY (SELECT date(" +
			CustomizedSQLiteHelper.COLUMN_DATE + ")) ORDER BY date_field DESC",
			null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DayData day_data = new DayData();
			day_data.date = cursor.getString(cursor.getColumnIndex(
				"date_field"));
			day_data.calories = cursor.getDouble(cursor.getColumnIndex(
				"calories"));
			day_data_list.add(day_data);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return day_data_list;
	}

	public String getAllDataInXml() {
		String xml =
			"<?xml version = \"1.0\" encoding = \"utf-8\" ?>\n" +
			"<!-- Backup of history from application Diary of calories. -->\n" +
			"<history>\n";

		SQLiteDatabase database = sqlite_helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("SELECT " + CustomizedSQLiteHelper.
			COLUMN_WEIGHT + ", " + CustomizedSQLiteHelper.COLUMN_CALORIES + 
			", (SELECT date(" + CustomizedSQLiteHelper.COLUMN_DATE + ")) AS " +
			"'date_field' FROM " + CustomizedSQLiteHelper.TABLE_NAME + " ORDER"
			+ " BY date_field DESC", null);
		cursor.moveToFirst();

		boolean first_loop = true;
		String old_date = "";
		while (!cursor.isAfterLast()) {
			String current_date = cursor.getString(cursor.getColumnIndex(
				"date_field"));
			if (!current_date.equals(old_date)) {
				if (!first_loop) {
					xml += "\t</day>\n";
				}
				xml += "\t<day date = \"" + current_date + "\">\n";

				old_date = current_date;
			}

			xml += "\t\t<food weight = \"" + cursor.getString(cursor.
				getColumnIndex(CustomizedSQLiteHelper.COLUMN_WEIGHT)) + "\" " +
				"calories = \"" + cursor.getString(cursor.getColumnIndex(
				CustomizedSQLiteHelper.COLUMN_CALORIES)) + "\" />\n";

			cursor.moveToNext();
			first_loop = false;
		}

		cursor.close();
		database.close();

		xml +=
			"\t</day>\n" +
			"</history>\n";
		return xml;
	}

	public UserSettings getUserSettings() {
		UserSettings settings = new UserSettings();
		SharedPreferences preferences =
			PreferenceManager.getDefaultSharedPreferences(context);
		settings.hard_limit = preferences.getFloat(
			UserSettings.HARD_LIMIT_SETTING_NAME,
			UserSettings.DEFAULT_HARD_LIMIT);
		settings.soft_limit = preferences.getFloat(
			UserSettings.SOFT_LIMIT_SETTING_NAME,
			UserSettings.DEFAULT_SOFT_LIMIT);
		return settings;
	}

	public void setUserSettings(UserSettings user_settings) {
		SharedPreferences preferences =
			PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor preferences_editor = preferences.edit();
		preferences_editor.putFloat(UserSettings.HARD_LIMIT_SETTING_NAME,
			user_settings.hard_limit);
		preferences_editor.putFloat(UserSettings.SOFT_LIMIT_SETTING_NAME,
			user_settings.soft_limit);
		preferences_editor.commit();
	}

	public Settings getSettings() {
		Settings settings = new Settings();
		SharedPreferences preferences =
			PreferenceManager.getDefaultSharedPreferences(context);
		settings.view_mode = HistoryActivity.ViewMode.valueOf(
			preferences.getString(Settings.VIEW_MODE_SETTING_NAME,
			Settings.DEFAULT_VIEW_MODE.toString()));
		return settings;
	}

	public void setSettings(Settings settings) {
		SharedPreferences preferences =
			PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor preferences_editor = preferences.edit();
		preferences_editor.putString(Settings.VIEW_MODE_SETTING_NAME,
			settings.view_mode.toString());
		preferences_editor.commit();
	}

	public void addData(float weight, float calories) {
		SQLiteDatabase database = sqlite_helper.getWritableDatabase();
		database.execSQL("INSERT INTO " + CustomizedSQLiteHelper.TABLE_NAME +
			" (" + CustomizedSQLiteHelper.COLUMN_ID + ", " +
			CustomizedSQLiteHelper.COLUMN_WEIGHT + ", " +
			CustomizedSQLiteHelper.COLUMN_CALORIES + ", " +
			CustomizedSQLiteHelper.COLUMN_DATE + ") VALUES (NULL, " +
			String.valueOf(weight) + ", " + String.valueOf(calories) + ", " +
			"(SELECT datetime('now', 'localtime')))");
		database.close();
	}

	public void undoTheLast() {
		if (getNumberOfCurrentDayData() > 0) {
			SQLiteDatabase database = sqlite_helper.getWritableDatabase();
			database.execSQL(
				"DELETE FROM " + CustomizedSQLiteHelper.TABLE_NAME + " WHERE "
				+ "(SELECT datetime(" + CustomizedSQLiteHelper.COLUMN_DATE +
				")) = (SELECT MAX(date_and_time) FROM (SELECT datetime(" +
				CustomizedSQLiteHelper.COLUMN_DATE + ") AS 'date_and_time' " +
				"FROM " + CustomizedSQLiteHelper.TABLE_NAME + "))");
			database.close();
		}
	}

	private static DataAccessor    instance;

	private Context                context;
	private CustomizedSQLiteHelper sqlite_helper;

	private DataAccessor(Context context) {
		this.context =  context;
		sqlite_helper = new CustomizedSQLiteHelper(context);
	}
}
