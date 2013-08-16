package com.thewizardplusplus.diaryofcalories;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager widget_manager,
		int[] widget_ids)
	{
		data_accessor = DataAccessor.getInstance(context);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.
			widget);
		updateUI(context, views);
		widget_manager.updateAppWidget(widget_ids, views);
	}

	private void updateUI(Context context, RemoteViews views) {
		Intent intent = new Intent(context, MainActivity.class);
		views.setOnClickPendingIntent(R.id.widget_container, PendingIntent.
			getActivity(context, 0, intent, 0));

		DayData current_day_data = data_accessor.getCurrentDayData();

		double current_day_calories = current_day_data.calories;
		views.setTextViewText(R.id.current_day_calories, Utils.
			convertNumberToLocaleFormat(current_day_calories));

		float maximum_calories = data_accessor.getUserSettings().
			soft_limit;
		views.setTextViewText(R.id.maximum_calories, Utils.
			convertNumberToLocaleFormat(maximum_calories));

		double difference = maximum_calories - current_day_calories;
		if (current_day_calories <= maximum_calories) {
			views.setTextColor(R.id.current_day_calories, Color.rgb(0, 0xc0,
				0));
			views.setTextColor(R.id.current_day_calories_unit, Color.rgb(0,
				0xc0, 0));
			views.setViewVisibility(R.id.label3, View.VISIBLE);
			views.setViewVisibility(R.id.label4, View.GONE);
			views.setTextColor(R.id.remaining_calories, Color.rgb(0, 0xc0, 0));
			views.setTextColor(R.id.remaining_calories_unit, Color.rgb(0, 0xc0,
				0));
		} else {
			views.setTextColor(R.id.current_day_calories, Color.rgb(0xc0, 0xc0,
				0));
			views.setTextColor(R.id.current_day_calories_unit, Color.rgb(0xc0,
				0xc0, 0));
			views.setViewVisibility(R.id.label3, View.GONE);
			views.setViewVisibility(R.id.label4, View.VISIBLE);
			views.setTextColor(R.id.remaining_calories, Color.rgb(0xc0, 0xc0,
				0));
			views.setTextColor(R.id.remaining_calories_unit, Color.rgb(0xc0,
				0xc0, 0));
			difference = -difference;
		}
		views.setTextViewText(R.id.remaining_calories, Utils.
			convertNumberToLocaleFormat(difference));
	}

	private DataAccessor data_accessor;
}
