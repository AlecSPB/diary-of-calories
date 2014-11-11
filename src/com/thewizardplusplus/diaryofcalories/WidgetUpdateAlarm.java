package com.thewizardplusplus.diaryofcalories;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.content.ComponentName;
import android.appwidget.AppWidgetManager;

public class WidgetUpdateAlarm extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		RemoteViews views = Widget.getUpdatedViews(context);
		ComponentName widget = new ComponentName(context, Widget.class);
		AppWidgetManager.getInstance(context).updateAppWidget(widget, views);
	}
}

