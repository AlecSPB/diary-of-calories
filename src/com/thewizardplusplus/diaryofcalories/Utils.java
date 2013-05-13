package com.thewizardplusplus.diaryofcalories;

import java.text.NumberFormat;
import java.text.DateFormat;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

public class Utils {
	public static String convertNumberToLocaleFormat(long number) {
		NumberFormat number_format = NumberFormat.getInstance();
		number_format.setMinimumFractionDigits(0);
		number_format.setMaximumFractionDigits(2);
		return number_format.format(number);
	}

	public static String convertNumberToLocaleFormat(double number) {
		NumberFormat number_format = NumberFormat.getInstance();
		number_format.setMinimumFractionDigits(0);
		number_format.setMaximumFractionDigits(2);
		return number_format.format(number);
	}

	public static String convertDateToLocaleFormat(String date) {
		String[] date_parts = date.split("-");
		int day = Integer.parseInt(date_parts[0]);
		int month = Integer.parseInt(date_parts[1]);
		int year = Integer.parseInt(date_parts[2]);
		Date date_object = new Date(day, month, year);
		DateFormat date_format = DateFormat.getDateInstance(DateFormat.SHORT);
		return date_format.format(date_object);
	}

	public static void showAlertDialog(Context context, String title, String
		message, AlertType type)
	{
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle(title);
		if (type == AlertType.WARNING) {
			dialog.setIcon(android.R.drawable.ic_dialog_alert);
		} else {
			dialog.setIcon(android.R.drawable.ic_dialog_info);
		}
		dialog.setMessage(message);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(
			android.R.string.ok), (Message)null);
		dialog.show();
	}

	public static void showNotification(Context context, int icon_id, String
		title, String message)
	{
		PendingIntent intent = PendingIntent.getActivity(context, 0, new
			Intent(), 0);
		Notification notification = new NotificationCompat.Builder(context).
			setTicker(title).setSmallIcon(icon_id).setContentTitle(title).
			setContentText(message).setAutoCancel(true).setContentIntent(
			intent).build();

		NotificationManager notifications = (NotificationManager)context.
			getSystemService(Context.NOTIFICATION_SERVICE);
		notifications.notify(notification_id, notification);
		notification_id++;
	}

	private static int notification_id = 0;
}
