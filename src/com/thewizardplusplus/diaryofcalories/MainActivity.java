package com.thewizardplusplus.diaryofcalories;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;

public class MainActivity extends Activity {
	public static final String HISTORY_BACKUP_DIRECTORY = ".diaryofcalories";
	public static final String HISTORY_BACKUP_FILE =
		"backup_of_history.xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data_accessor = DataAccessor.getInstance(this);

		setContentView(R.layout.main);
		label1 = (TextView)findViewById(R.id.label1);
		current_day_calories = (TextView)findViewById(
			R.id.current_day_calories);
		current_day_calories_unit = (TextView)findViewById(
				R.id.current_day_calories_unit);
		maximum_calories = (TextView)findViewById(R.id.maximum_calories);
		label3 = (TextView)findViewById(R.id.label3);
		label4 = (TextView)findViewById(R.id.label4);
		remaining_calories = (TextView)findViewById(R.id.remaining_calories);
		remaining_calories_unit = (TextView)findViewById(
			R.id.remaining_calories_unit);
		weight_edit = (EditText)findViewById(R.id.weight_edit);
		calories_edit = (EditText)findViewById(R.id.calories_edit);
		cancel_button = (ImageButton)findViewById(R.id.cancel_button);

		updateUI();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}

	public void addData(View view) {
		String weight = weight_edit.getText().toString();
		String calories = calories_edit.getText().toString();
		if (weight.length() != 0 && calories.length() != 0) {
			data_accessor.addData(Float.parseFloat(weight),
				Float.parseFloat(calories));
			backupHistory();
			updateUI();
			weight_edit.setText("");
			weight_edit.requestFocus();
			calories_edit.setText("");
		}
	}

	public void undoTheLast(View view) {
		data_accessor.undoTheLast();
		backupHistory();
		updateUI();
	}

	public void showHistory(View view) {
		Intent intent = new Intent(this, HistoryActivity.class);
		startActivity(intent);
	}

	public void showSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void showAbout(View view) {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	private static final long NOTIFICATION_HIDE_DELAY = 15000;

	private TextView     label1;
	private TextView     current_day_calories;
	private TextView     current_day_calories_unit;
	private TextView     maximum_calories;
	private TextView     label3;
	private TextView     label4;
	private TextView     remaining_calories;
	private TextView     remaining_calories_unit;
	private EditText     weight_edit;
	private EditText     calories_edit;
	private ImageButton  cancel_button;
	private DataAccessor data_accessor;

	private void updateUI() {
		DayData current_day_data = data_accessor.getCurrentDayData();
		double current_day_calories = current_day_data.calories;
		label1.setText(String.format(getString(R.string.label1),
			Utils.convertDateToLocaleFormat(current_day_data.date)));
		this.current_day_calories.setText(Utils.convertNumberToLocaleFormat(
			current_day_calories));
		float maximum_calories =
			data_accessor.getUserSettings().maximum_calories;
		this.maximum_calories.setText(Utils.convertNumberToLocaleFormat(
			maximum_calories));
		double difference = maximum_calories - current_day_calories;
		if (current_day_calories <= maximum_calories) {
			this.current_day_calories.setTextColor(Color.rgb(0, 0xc0, 0));
			this.current_day_calories_unit.setTextColor(Color.rgb(0, 0xc0, 0));
			label3.setVisibility(View.VISIBLE);
			label4.setVisibility(View.GONE);
			this.remaining_calories.setTextColor(Color.rgb(0, 0xc0, 0));
			this.remaining_calories_unit.setTextColor(Color.rgb(0, 0xc0, 0));
		} else {
			this.current_day_calories.setTextColor(Color.rgb(0xc0, 0, 0));
			this.current_day_calories_unit.setTextColor(Color.rgb(0xc0, 0, 0));
			label3.setVisibility(View.GONE);
			label4.setVisibility(View.VISIBLE);
			this.remaining_calories.setTextColor(Color.rgb(0xc0, 0, 0));
			this.remaining_calories_unit.setTextColor(Color.rgb(0xc0, 0, 0));
			difference = -difference;
		}
		this.remaining_calories.setText(Utils.convertNumberToLocaleFormat(
			difference));

		if (data_accessor.getNumberOfCurrentDayData() > 0) {
			cancel_button.setEnabled(true);
		} else {
			cancel_button.setEnabled(false);
		}
	}

	private void backupHistory() {
		String storage_state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(storage_state)) {
			Utils.showAlertDialog(this, getString(R.string.
				error_message_box_title), getString(R.string.
				external_storage_error_message), AlertType.WARNING);
			return;
		}

		File directory = new File(Environment.getExternalStorageDirectory(),
			HISTORY_BACKUP_DIRECTORY);
		if (!directory.exists()) {
			boolean result = directory.mkdir();
			if (!result) {
				Utils.showAlertDialog(this, getString(R.string.
					error_message_box_title), getString(R.string.
					directory_error_message), AlertType.WARNING);
				return;
			}
		}

		File backup_file = new File(directory, HISTORY_BACKUP_FILE);
		try {
			OutputStream out = new FileOutputStream(backup_file);
			byte[] bytes_data = data_accessor.getAllDataInXml().getBytes();
			out.write(bytes_data);
			out.flush();
			out.close();
		} catch (IOException exception) {
			Utils.showAlertDialog(this, getString(R.string.
				error_message_box_title), getString(R.string.
				backup_file_error_message), AlertType.WARNING);
			return;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(backup_file), "text/xml");
		Utils.showNotification(this, R.drawable.icon, getString(R.string.
			application_name), String.format(getString(R.string.
			backup_saved_notification), backup_file.getAbsolutePath()),
			NOTIFICATION_HIDE_DELAY, intent);
	}
}
