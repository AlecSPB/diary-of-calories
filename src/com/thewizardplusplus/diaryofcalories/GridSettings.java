package com.thewizardplusplus.diaryofcalories;

import android.graphics.Paint;
import android.graphics.Color;

public class GridSettings {
	public int background_color = Color.WHITE;
	public boolean draw_grid = true;
	public Paint grid_paint = new Paint();
	public int grid_step = 10;
	public boolean draw_frame = false;
	public Paint arrow_paint = new Paint();
	public Paint graph_paint = new Paint();

	public GridSettings() {
		grid_paint.setStrokeCap(Paint.Cap.ROUND);
		grid_paint.setStrokeWidth(1);
		grid_paint.setColor(Color.GRAY);

		arrow_paint.setStrokeCap(Paint.Cap.ROUND);
		arrow_paint.setStrokeJoin(Paint.Join.ROUND);
		arrow_paint.setAntiAlias(true);
		arrow_paint.setStrokeWidth(2);
		arrow_paint.setColor(Color.BLACK);

		graph_paint.setStrokeCap(Paint.Cap.ROUND);
		graph_paint.setStrokeJoin(Paint.Join.ROUND);
		graph_paint.setAntiAlias(true);
		graph_paint.setStrokeWidth(2);
		graph_paint.setColor(Color.GRAY);
	}
}
