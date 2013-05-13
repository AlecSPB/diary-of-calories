package com.thewizardplusplus.diaryofcalories;

public class Settings {
	public static final String                   VIEW_MODE_SETTING_NAME =
		"view_mode";
	public static final HistoryActivity.ViewMode DEFAULT_VIEW_MODE =
		HistoryActivity.ViewMode.LIST;

	public HistoryActivity.ViewMode view_mode;

	public Settings() {
		view_mode = DEFAULT_VIEW_MODE;
	}
}

