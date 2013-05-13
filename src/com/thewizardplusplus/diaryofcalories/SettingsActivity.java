package com.thewizardplusplus.diaryofcalories;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data_accessor = DataAccessor.getInstance(this);

		setContentView(R.layout.settings);

		maximum_calories_edit = (EditText)findViewById(
			R.id.maximum_calories_edit);
		float maximum_calories =
			data_accessor.getUserSettings().maximum_calories;
		maximum_calories_edit.setText(Float.toString(maximum_calories));
	}

	public void saveSettings(View view) {
		String maximum_calories = maximum_calories_edit.getText().toString();
		if (maximum_calories.length() != 0) {
			UserSettings setting = new UserSettings();
			setting.maximum_calories = Float.parseFloat(maximum_calories);
			data_accessor.setUserSettings(setting);

			finish();
		}
	}

	private EditText     maximum_calories_edit;
	private DataAccessor data_accessor;
}
