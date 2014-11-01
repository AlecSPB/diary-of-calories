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
	public static RemoteViews getUpdatedViews(Context context) {
		RemoteViews views = new RemoteViews(
			context.getPackageName(),
			R.layout.widget
		);

		Intent intent = new Intent(context, MainActivity.class);
		views.setOnClickPendingIntent(
			R.id.widget_container,
			PendingIntent.getActivity(context, 0, intent, 0)
		);

		DataAccessor data_accessor = DataAccessor.getInstance(context);
		DayData current_day_data = data_accessor.getCurrentDayData();

		double current_day_calories = current_day_data.calories;
		views.setTextViewText(
			R.id.current_day_calories,
			Utils.convertNumberToLocaleFormat(current_day_calories)
		);

		float soft_limit = data_accessor
			.getSettings()
			.soft_limit;
		float hard_limit = data_accessor
			.getSettings()
			.hard_limit;
		double maximum_calories = 0.0;
		double difference = 0.0;
		if (current_day_calories <= hard_limit) {
			views.setViewVisibility(R.id.label3, View.VISIBLE);
			views.setViewVisibility(R.id.label4, View.GONE);

			if (current_day_calories <= soft_limit) {
				maximum_calories = soft_limit;

				views.setTextColor(R.id.current_day_calories, Color.rgb(0, 0xc0, 0));
				views.setTextColor(R.id.current_day_calories_unit, Color.rgb(0, 0xc0, 0));
				views.setTextColor(R.id.remaining_calories, Color.rgb(0, 0xc0, 0));
				views.setTextColor(R.id.remaining_calories_unit, Color.rgb(0, 0xc0, 0));
			} else {
				maximum_calories = hard_limit;

				views.setTextColor(R.id.current_day_calories, Color.rgb(0xc0, 0xc0, 0));
				views.setTextColor(R.id.current_day_calories_unit, Color.rgb(0xc0, 0xc0, 0));
				views.setTextColor(R.id.remaining_calories, Color.rgb(0xc0, 0xc0, 0));
				views.setTextColor(R.id.remaining_calories_unit, Color.rgb(0xc0, 0xc0, 0));
			}

			difference = maximum_calories - current_day_calories;
		} else {
			maximum_calories = hard_limit;
			difference = current_day_calories - maximum_calories;

			views.setViewVisibility(R.id.label3, View.GONE);
			views.setViewVisibility(R.id.label4, View.VISIBLE);

			views.setTextColor(R.id.current_day_calories, Color.rgb(0xc0, 0, 0));
			views.setTextColor(R.id.current_day_calories_unit, Color.rgb(0xc0, 0, 0));
			views.setTextColor(R.id.remaining_calories, Color.rgb(0xc0, 0, 0));
			views.setTextColor(R.id.remaining_calories_unit, Color.rgb(0xc0, 0, 0));
		}

		views.setTextViewText(
			R.id.maximum_calories,
			Utils.convertNumberToLocaleFormat(maximum_calories)
		);
		views.setTextViewText(
			R.id.remaining_calories,
			Utils.convertNumberToLocaleFormat(difference)
		);

		return views;
	}

	@Override
	public void onUpdate(
		Context context,
		AppWidgetManager widget_manager,
		int[] widget_ids
	) {
		RemoteViews views = getUpdatedViews(context);
		widget_manager.updateAppWidget(widget_ids, views);
	}
}

