package com.thewizardplusplus.diaryofcalories;

public class UserSettings {
	public static final String HARD_LIMIT_SETTING_NAME = "hard_limit";
	public static final String SOFT_LIMIT_SETTING_NAME = "soft_limit";
	public static final float  DEFAULT_HARD_LIMIT = 2500.0f;
	public static final float  DEFAULT_SOFT_LIMIT = 1200.0f;

	public float hard_limit;
	public float soft_limit;

	public UserSettings() {
		hard_limit = DEFAULT_HARD_LIMIT;
		soft_limit = DEFAULT_SOFT_LIMIT;
	}
}
