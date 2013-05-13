package com.thewizardplusplus.diaryofcalories;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import android.view.SurfaceHolder;
import android.graphics.Paint;
import android.graphics.Canvas;

public class GraphPainter extends Thread {
	public GraphPainter(SurfaceHolder surface_holder) {
		this.surface_holder = surface_holder;
		data =                new ArrayList<Graph>();
		minimal_x =           0.0;
		maximal_y =           0.0;
		minimal_y =           0.0;
		maximal_y =           0.0;
		step =                new RealVector2D();
		size =                new IntegerSize();
		number_cells =        new IntegerVector2D();
		grid_settings =       new GridSettings();
		background_paint =    new Paint();
		background_paint.setStrokeWidth(0);
		background_paint.setColor(grid_settings.background_color);
		graph_translate =     new RealVector2D(grid_settings.grid_step,
			grid_settings.grid_step);
		graph_scale =         new RealVector2D(1.0, 1.0);
		scale_base_point =    new RealVector2D();
		graph_base_point =    RealVector2D.sub(graph_translate,
			scale_base_point).mul(graph_scale);
		running =             true;
	}

	public GraphPainter(SurfaceHolder surface_holder, List<Graph> data,
		GridSettings grid_settings)
	{
		this.surface_holder = surface_holder;
		this.data =           data;
		minimal_x =           0.0;
		maximal_y =           0.0;
		minimal_y =           0.0;
		maximal_y =           0.0;
		step =                new RealVector2D();
		size =                new IntegerSize();
		number_cells =        new IntegerVector2D();
		calculateFrame();
		this.grid_settings =  grid_settings;
		background_paint =    new Paint();
		background_paint.setStrokeWidth(0);
		background_paint.setColor(this.grid_settings.background_color);
		graph_translate =     new RealVector2D(grid_settings.grid_step,
			grid_settings.grid_step);
		graph_scale =         new RealVector2D(1.0, 1.0);
		scale_base_point =    new RealVector2D();
		graph_base_point =    RealVector2D.sub(graph_translate,
			scale_base_point).mul(graph_scale);
		running =             true;
	}

	public List<Graph> getData() {
		return data;
	}

	public void setList(List<Graph> data) {
		this.data = data;
		calculateFrame();
	}

	public GridSettings getGridSettings() {
		return grid_settings;
	}

	public void setGridSettings(GridSettings grid_settings) {
		this.grid_settings = grid_settings;
	}

	public IntegerSize getSize() {
		return size;
	}

	public void setSize(IntegerSize size) {
		this.size = size;
		number_cells.x = size.width / grid_settings.grid_step;
		number_cells.y = size.height / grid_settings.grid_step;
		calculateSteps();
	}

	public RealVector2D getTranslate() {
		return graph_translate;
	}

	public void setTranslate(RealVector2D translate) {
		graph_translate = translate;
		graph_base_point = RealVector2D.sub(graph_translate,
			scale_base_point).mul(graph_scale);
	}

	public RealVector2D getScale() {
		return graph_scale;
	}

	public RealVector2D getScaleBasePoint() {
		return scale_base_point;
	}

	public void setScale(RealVector2D scale, RealVector2D base_point) {
		graph_scale = scale;
		scale_base_point = base_point;
		graph_base_point = RealVector2D.sub(graph_translate,
			scale_base_point).mul(graph_scale);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		while (running) {
			if (!size.isNull()) {
				Canvas canvas = null;
				try {
					canvas = surface_holder.lockCanvas();
					synchronized(surface_holder) {
						draw(canvas);
					}
				} catch(Exception exception) {
				} finally {
					if (canvas != null) {
						surface_holder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
	}

	private SurfaceHolder   surface_holder;
	private List<Graph>     data;
	private double          minimal_x;
	private double          maximal_x;
	private double          minimal_y;
	private double          maximal_y;
	private RealVector2D    step;
	private IntegerSize     size;
	private IntegerVector2D number_cells;
	private GridSettings    grid_settings;
	private Paint           background_paint;
	private RealVector2D    graph_translate;
	private RealVector2D    graph_scale;
	private RealVector2D    scale_base_point;
	private RealVector2D    graph_base_point;
	private boolean         running;

	private void calculateFrame() {
		boolean first_entry = true;
		for (Graph graph : data) {
			for (Map.Entry<Double, Double> entry : graph.data.entrySet()) {
				double x = entry.getKey().doubleValue();
				double y = entry.getValue().doubleValue();
				if (first_entry) {
					minimal_x = x;
					maximal_x = x;
					minimal_y = y;
					maximal_y = y;
					first_entry = false;
				} else {
					if (x < minimal_x) {
						minimal_x = x;
					} else if (x > maximal_x) {
						maximal_x = x;
					}
					if (y < minimal_y) {
						minimal_y = y;
					} else if (y > maximal_y) {
						maximal_y = y;
					}
				}
			}
		}
		calculateSteps();
	}

	private void calculateSteps() {
		double delta_x = (maximal_x - minimal_x);
		if (delta_x != 0.0) {
			step.x = delta_x / (number_cells.x - 4);
		} else {
			step.x = 1.0;
		}

		double delta_y = (maximal_y - minimal_y);
		if (delta_y != 0.0) {
			step.y = delta_y / (number_cells.y - 4);
		} else {
			step.y = 1.0;
		}
	}

	private void draw(Canvas canvas) {
		drawBackground(canvas);
		drawGrid(canvas);
		drawArrows(canvas);
		for (Graph graph : data) {
			drawGraph(canvas, graph);
		}
	}

	private void drawBackground(Canvas canvas) {
		canvas.drawPaint(background_paint);
	}

	private void drawGrid(Canvas canvas) {
		if (grid_settings.draw_grid) {
			for (int x = grid_settings.grid_step; x <= size.width -
				grid_settings.grid_step; x += grid_settings.grid_step)
			{
				canvas.drawLine(x, 0, x, size.height, grid_settings.
					grid_paint);
			}
			for (int y = (int)size.height - grid_settings.grid_step; y >=
				grid_settings.grid_step; y -= grid_settings.grid_step)
			{
				canvas.drawLine(0, y, size.width, y, grid_settings.grid_paint);
			}
		}
		if (grid_settings.draw_frame) {
			canvas.drawLine(0, 0, size.width - 1, 0, grid_settings.grid_paint);
			canvas.drawLine(size.width - 1, 0, size.width - 1, size.height - 1,
				grid_settings.grid_paint);
			canvas.drawLine(size.width - 1, size.height - 1, 0, size.height -
				1, grid_settings.grid_paint);
			canvas.drawLine(0, size.height - 1, 0, 0, grid_settings.
				grid_paint);
		}
	}

	private void drawArrows(Canvas canvas) {
		canvas.drawLine(Math.round(0.5 * grid_settings.grid_step), size.height
			- grid_settings.grid_step, grid_settings.grid_step * (number_cells.
			x - 1), size.height - grid_settings.grid_step, grid_settings.
			arrow_paint);
		canvas.drawLine(grid_settings.grid_step * (number_cells.x - 1),
			size.height - grid_settings.grid_step, grid_settings.grid_step *
			(number_cells.x - 2), size.height - Math.round(1.5 *
			grid_settings.grid_step), grid_settings.arrow_paint);
		canvas.drawLine(grid_settings.grid_step * (number_cells.x - 1),
			size.height - grid_settings.grid_step, grid_settings.grid_step *
			(number_cells.x - 2), size.height - Math.round(0.5 *
			grid_settings.grid_step), grid_settings.arrow_paint);
		canvas.drawLine(grid_settings.grid_step, size.height - Math.round(0.5 *
			grid_settings.grid_step), grid_settings.grid_step, size.height -
			grid_settings.grid_step * (number_cells.y - 1),
			grid_settings.arrow_paint);
		canvas.drawLine(grid_settings.grid_step, size.height -
			grid_settings.grid_step * (number_cells.y - 1), Math.round(0.5 *
			grid_settings.grid_step), size.height - grid_settings.grid_step *
			(number_cells.y - 2), grid_settings.arrow_paint);
		canvas.drawLine(grid_settings.grid_step, size.height -
			grid_settings.grid_step * (number_cells.y - 1), Math.round(1.5 *
			grid_settings.grid_step), size.height - grid_settings.grid_step *
			(number_cells.y - 2), grid_settings.arrow_paint);
	}

	public IntegerVector2D transformPoint(RealVector2D point) {
		IntegerVector2D result = new IntegerVector2D();
		result.x = Math.round((point.x - minimal_x) / step.x *
			grid_settings.grid_step * graph_scale.x + graph_base_point.x);
		result.y = size.height - Math.round((point.y - minimal_y) / step.y *
			grid_settings.grid_step * graph_scale.y + graph_base_point.y);
		return result;
	}

	public void drawGraph(Canvas canvas, Graph graph) {
		IntegerVector2D start = new IntegerVector2D();
		boolean first_entry = true;
		for (Map.Entry<Double, Double> entry : graph.data.entrySet()) {
			RealVector2D original_point = new RealVector2D();
			original_point.x = entry.getKey().doubleValue();
			original_point.y = entry.getValue().doubleValue();
			IntegerVector2D transformed_point = transformPoint(original_point);
			if (first_entry) {
				first_entry = false;
			} else {
				canvas.drawLine(start.x, start.y, transformed_point.x,
						transformed_point.y, graph.paint);
			}
			start = transformed_point;
		}
	}
}
