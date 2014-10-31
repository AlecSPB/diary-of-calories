package com.thewizardplusplus.diaryofcalories;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

public class SettingsActivity extends Activity {
	@Override
	public void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.settings);

		data_accessor = DataAccessor.getInstance(this);

		hard_limit_edit = (EditText)findViewById(R.id.hard_limit_edit);
		hard_limit_edit.setText(
			Float.toString(data_accessor.getSettings().hard_limit)
		);

		soft_limit_edit = (EditText)findViewById(R.id.soft_limit_edit);
		soft_limit_edit.setText(
			Float.toString(data_accessor.getSettings().soft_limit)
		);
	}

	public void saveSettings(View view) {
		String hard_limit = hard_limit_edit.getText().toString();
		String soft_limit = soft_limit_edit.getText().toString();
		if (hard_limit.length() != 0 || soft_limit.length() != 0) {
			Settings setting = data_accessor.getSettings();
			if (hard_limit.length() != 0) {
				setting.hard_limit = Float.parseFloat(hard_limit);
			}
			if (soft_limit.length() != 0) {
				setting.soft_limit = Float.parseFloat(soft_limit);
			}
			data_accessor.setSettings(setting);

			updateWidgetUI();
			finish();
		}
	}

	private DataAccessor data_accessor;
	private EditText hard_limit_edit;
	private EditText soft_limit_edit;

	private void updateWidgetUI() {
		RemoteViews views = new RemoteViews(
			this.getPackageName(),
			R.layout.widget
		);

		Intent intent = new Intent(this, MainActivity.class);
		views.setOnClickPendingIntent(
			R.id.widget_container,
			PendingIntent.getActivity(this, 0, intent, 0)
		);

		DataAccessor data_accessor = DataAccessor.getInstance(this);
		DayData current_day_data = data_accessor.getCurrentDayData();

		double current_day_calories = current_day_data.calories;
		views.setTextViewText(
			R.id.current_day_calories,
			Utils.convertNumberToLocaleFormat(current_day_calories)
		);

		float maximum_calories = data_accessor
			.getSettings()
			.soft_limit;
		views.setTextViewText(
			R.id.maximum_calories,
			Utils.convertNumberToLocaleFormat(maximum_calories)
		);

		double difference = maximum_calories - current_day_calories;
		if (current_day_calories <= maximum_calories) {
			views.setTextColor(R.id.current_day_calories, Color.rgb(0, 0xc0, 0));
			views.setTextColor(R.id.current_day_calories_unit, Color.rgb(0, 0xc0, 0));
			views.setViewVisibility(R.id.label3, View.VISIBLE);
			views.setViewVisibility(R.id.label4, View.GONE);
			views.setTextColor(R.id.remaining_calories, Color.rgb(0, 0xc0, 0));
			views.setTextColor(R.id.remaining_calories_unit, Color.rgb(0, 0xc0, 0));
		} else {
			views.setTextColor(R.id.current_day_calories, Color.rgb(0xc0, 0, 0));
			views.setTextColor(R.id.current_day_calories_unit, Color.rgb(0xc0, 0, 0));
			views.setViewVisibility(R.id.label3, View.GONE);
			views.setViewVisibility(R.id.label4, View.VISIBLE);
			views.setTextColor(R.id.remaining_calories, Color.rgb(0xc0, 0, 0));
			views.setTextColor(R.id.remaining_calories_unit, Color.rgb(0xc0, 0, 0));

			difference = -difference;
		}
		views.setTextViewText(
			R.id.remaining_calories,
			Utils.convertNumberToLocaleFormat(difference)
		);

		ComponentName widget = new ComponentName(this, Widget.class);
		AppWidgetManager.getInstance(this).updateAppWidget(widget, views);
	}
}

