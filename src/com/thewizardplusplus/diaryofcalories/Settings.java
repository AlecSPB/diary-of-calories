package com.thewizardplusplus.diaryofcalories;

public class Settings {
	public static final String VIEW_MODE_SETTING_NAME = "view_mode";
	public static final String HARD_LIMIT_SETTING_NAME = "hard_limit";
	public static final String SOFT_LIMIT_SETTING_NAME = "soft_limit";
	public static final HistoryViewMode DEFAULT_VIEW_MODE = HistoryViewMode.LIST;
	public static final float DEFAULT_HARD_LIMIT = 2500.0f;
	public static final float DEFAULT_SOFT_LIMIT = 1200.0f;

	public HistoryViewMode view_mode = DEFAULT_VIEW_MODE;
	public float hard_limit = DEFAULT_HARD_LIMIT;
	public float soft_limit = DEFAULT_SOFT_LIMIT;
}

