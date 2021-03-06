package ru.thewizardplusplus.diaryofcalories;

import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

public class Utils {
	public static String convertNumberToLocaleFormat(double number) {
		NumberFormat number_format = NumberFormat.getInstance();
		number_format.setMinimumFractionDigits(0);
		number_format.setMaximumFractionDigits(2);

		return number_format.format(number);
	}

	public static String convertDateToLocaleFormat(String date) {
		String[] date_parts = date.split("-");
		int day = Integer.parseInt(date_parts[0]);
		int month = Integer.parseInt(date_parts[1]) - 1;
		int year = Integer.parseInt(date_parts[2]);

		GregorianCalendar calendar = new GregorianCalendar(day, month, year);
		DateFormat date_format = DateFormat.getDateInstance(DateFormat.SHORT);
		return date_format.format(calendar.getTime());
	}

	public static void showAlertDialog(
		Context context,
		String title,
		String message
	) {
		AlertDialog dialog = new AlertDialog.Builder(context)
			.setTitle(title)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage(message)
			.create();
		dialog.setButton(
			AlertDialog.BUTTON_POSITIVE,
			context.getString(android.R.string.ok),
			(Message)null
		);
		dialog.show();
	}

	public static void showNotification(
		Context context,
		int notification_id,
		int icon_id,
		String title,
		String message,
		Intent intent,
		NotificationType type,
		long hide_delay
	) {
		PendingIntent pending_intent = PendingIntent.getActivity(
			context,
			0,
			intent,
			0
		);
		Notification notification = new NotificationCompat.Builder(context)
			.setTicker(title)
			.setSmallIcon(icon_id)
			.setContentTitle(title)
			.setContentText(message)
			.setContentIntent(pending_intent)
			.build();
		if (type == NotificationType.ONGOING) {
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
		}

		final NotificationManager notifications =
			(NotificationManager)context.getSystemService(
				Context.NOTIFICATION_SERVICE
			);
		if (notification_id != 0) {
			notification_id = -Math.abs(notification_id);
		} else {
			notification_id = Utils.notification_id++;
		}
		notifications.notify(notification_id, notification);

		if (type == NotificationType.HIDDING && hide_delay > 0) {
			final int id = notification_id; 
			new Timer(true).schedule(
				new TimerTask() {
					@Override
					public void run() {
						notifications.cancel(id);
					}
				},
				hide_delay
			);
		}
	}

	private static int notification_id = 0;
}
