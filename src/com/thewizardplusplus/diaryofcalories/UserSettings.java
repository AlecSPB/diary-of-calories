package com.thewizardplusplus.diaryofcalories;

public class UserSettings {
	public static final String MAXIMUM_CALORIES_SETTING_NAME =
		"maximum_calories";
	public static final float  DEFAULT_MAXIMUM_CALORIES =      2500.0f;

	public float maximum_calories;

	public UserSettings() {
		maximum_calories = DEFAULT_MAXIMUM_CALORIES;
	}
}
