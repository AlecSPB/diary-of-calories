package com.thewizardplusplus.diaryofcalories;

import java.util.Map;
import java.util.TreeMap;
import android.graphics.Paint;
import android.graphics.Color;

public class Graph {
	public Map<Double, Double> data;
	public Paint               paint;

	public Graph() {
		data = new TreeMap<Double, Double>();
		paint = new Paint();
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setColor(Color.GRAY);
	}

	public Graph(Map<Double, Double> data) {
		this.data = data;
		paint = new Paint();
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setColor(Color.GRAY);
	}

	public Graph(Paint paint) {
		data = new TreeMap<Double, Double>();
		this.paint = paint;
	}

	public Graph(Map<Double, Double> data, Paint paint) {
		this.data =  data;
		this.paint = paint;
	}
}
