package com.thewizardplusplus.diaryofcalories;

import android.graphics.Paint;
import android.graphics.Color;

public class GridSettings {
	public int     background_color;
	public boolean draw_grid;
	public Paint   grid_paint;
	public int     grid_step;
	public boolean draw_frame;
	public Paint   arrow_paint;
	public Paint   graph_paint;

	public GridSettings() {
		background_color =    Color.WHITE;

		draw_grid =           true;

		grid_paint =          new Paint();
		grid_paint.setStrokeCap(Paint.Cap.ROUND);
		grid_paint.setStrokeWidth(1);
		grid_paint.setColor(Color.GRAY);
		grid_step =           10;

		draw_frame =          false;

		arrow_paint =         new Paint();
		arrow_paint.setStrokeCap(Paint.Cap.ROUND);
		arrow_paint.setStrokeJoin(Paint.Join.ROUND);
		arrow_paint.setAntiAlias(true);
		arrow_paint.setStrokeWidth(2);
		arrow_paint.setColor(Color.BLACK);

		graph_paint =         new Paint();
		graph_paint.setStrokeCap(Paint.Cap.ROUND);
		graph_paint.setStrokeJoin(Paint.Join.ROUND);
		graph_paint.setAntiAlias(true);
		graph_paint.setStrokeWidth(2);
		graph_paint.setColor(Color.GRAY);
	}

	public GridSettings(int background_color, boolean draw_grid, Paint
		grid_paint, int grid_step, boolean draw_frame, Paint arrow_paint, Paint
		graph_paint)
	{
		this.background_color = background_color;
		this.draw_grid =        draw_grid;
		this.grid_paint =       grid_paint;
		this.grid_step =        grid_step;
		this.draw_frame =       draw_frame;
		this.arrow_paint =      arrow_paint;
		this.graph_paint =      graph_paint;
	}
}
