package com.thewizardplusplus.diaryofcalories;

public class IntegerSize {
	public long width;
	public long height;

	public IntegerSize() {
		width =  0;
		height = 0;
	}

	public IntegerSize(long width, long height) {
		this.width =  width;
		this.height = height;
	}

	public boolean isNull() {
		return width == 0 && height == 0;
	}
}
