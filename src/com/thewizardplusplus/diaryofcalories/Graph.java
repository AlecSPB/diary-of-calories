package com.thewizardplusplus.diaryofcalories;

import java.util.Map;
import java.util.TreeMap;
import android.graphics.Paint;
import android.graphics.Color;

public class Graph {
	public Map<Double, Double> data = new TreeMap<
		Double,
		Double
	>();
	public Paint paint = new Paint();

	public Graph() {
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setColor(Color.GRAY);
	}
}

