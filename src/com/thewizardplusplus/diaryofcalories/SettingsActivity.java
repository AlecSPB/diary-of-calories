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

		DataAccessor data_accessor = DataAccessor.getInstance(this);
		Settings settings = data_accessor.getSettings();

		hard_limit_edit = (EditText)findViewById(R.id.hard_limit_edit);
		hard_limit_edit.setText(Float.toString(settings.hard_limit));

		soft_limit_edit = (EditText)findViewById(R.id.soft_limit_edit);
		soft_limit_edit.setText(Float.toString(settings.soft_limit));
	}

	public void saveSettings(View view) {
		String hard_limit = hard_limit_edit.getText().toString();
		String soft_limit = soft_limit_edit.getText().toString();
		if (hard_limit.length() != 0 || soft_limit.length() != 0) {
			DataAccessor data_accessor = DataAccessor.getInstance(this);
			Settings settings = data_accessor.getSettings();

			if (hard_limit.length() != 0) {
				settings.hard_limit = Float.parseFloat(hard_limit);
			}
			if (soft_limit.length() != 0) {
				settings.soft_limit = Float.parseFloat(soft_limit);
			}
			data_accessor.setSettings(settings);

			updateWidget();
			finish();
		}
	}

	private EditText hard_limit_edit;
	private EditText soft_limit_edit;

	private void updateWidget() {
		RemoteViews views = Widget.getUpdatedViews(this);
		ComponentName widget = new ComponentName(this, Widget.class);
		AppWidgetManager.getInstance(this).updateAppWidget(widget, views);
	}
}
