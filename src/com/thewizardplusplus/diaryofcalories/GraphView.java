package com.thewizardplusplus.diaryofcalories;

import java.util.List;
import java.util.ArrayList;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;

public class GraphView extends SurfaceView implements SurfaceHolder.Callback {
	public enum ControlState {
		QUIET,
		TRANSLATING,
		SCALING
	}

	public GraphView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);

		surface_holder =   getHolder();
		surface_holder.addCallback(this);
		data =             new ArrayList<Graph>();
		grid_settings =    new GridSettings();
		graph_translate =  new Vector2D(grid_settings.grid_step,
			grid_settings.grid_step);
		graph_scale =      new Vector2D(1.0, 1.0);
		state =            ControlState.QUIET;
		pointer1 =         new Vector2D();
		//pointer2 =         new RealVector2D();
		//middle_pointer =   new RealVector2D();
		//pointer_distance = 0.0;
	}

	public void surfaceCreated(SurfaceHolder surface_holder) {
		graph_painter = new GraphPainter(this.surface_holder, data,
			grid_settings);
		graph_painter.start();
	}

	public void surfaceChanged(SurfaceHolder surface_holder, int format, int
		width, int height)
	{
		graph_painter.setSize(new Vector2D(width, height));
	}

	public void surfaceDestroyed(SurfaceHolder surface_holder) {
		graph_painter.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				graph_painter.join();
				retry = false;
			} catch (InterruptedException exception) {}
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		int actions = event.getAction();
		if ((actions & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			state = ControlState.TRANSLATING;
			pointer1.x = event.getX();
			pointer1.y = event.getY();
		/*} else if ((actions & MotionEvent.ACTION_MASK) ==
			MotionEvent.ACTION_POINTER_1_DOWN)
		{
			state = ControlState.SCALING;
			pointer1.x = event.getX(0);
			pointer1.y = event.getY(0);
			pointer2.x = event.getX(1);
			pointer2.y = event.getY(1);
			middle_pointer = RealVector2D.add(pointer1, pointer2).div(2.0);
			pointer_distance = RealVector2D.sub(pointer1, pointer2).length();*/
		} else if ((actions & MotionEvent.ACTION_MASK) ==
			MotionEvent.ACTION_MOVE)
		{
			if (state == ControlState.TRANSLATING) {
				Vector2D current_pointer1 = new Vector2D();
				current_pointer1.x = event.getX();
				current_pointer1.y = event.getY();
				Vector2D shift = Vector2D.sub(current_pointer1,
					pointer1);
				//shift.y = -shift.y;
				shift.y = 0;
				graph_translate.add(shift);
				graph_painter.setTranslate(graph_translate);
				pointer1 = current_pointer1;
			}/* else if (state == ControlState.SCALING) {
				RealVector2D current_pointer1 = new RealVector2D();
				current_pointer1.x = event.getX(0);
				current_pointer1.y = event.getY(0);
				RealVector2D current_pointer2 = new RealVector2D();
				current_pointer2.x = event.getX(1);
				current_pointer2.y = event.getY(1);

				RealVector2D current_middle_pointer = RealVector2D.add(
					current_pointer1, current_pointer2).div(2.0);
				RealVector2D shift = RealVector2D.sub(
					current_middle_pointer, middle_pointer);
				shift.y = -shift.y;
				graph_translate.add(shift);
				graph_painter.setTranslate(graph_translate);

				double current_pointer_distance = RealVector2D.sub(
					current_pointer1, current_pointer2).length();
				double distance_ratio = current_pointer_distance /
					pointer_distance;
				graph_scale.x *= distance_ratio;
				graph_scale.y *= distance_ratio;
				graph_painter.setScale(graph_scale, middle_pointer);

				pointer1 = current_pointer1;
				pointer2 = current_pointer2;
				middle_pointer = current_middle_pointer;
				pointer_distance = current_pointer_distance;
			}
		} else if ((actions & MotionEvent.ACTION_MASK) ==
			MotionEvent.ACTION_POINTER_1_UP)
		{
			state = ControlState.TRANSLATING;
			pointer1.x = event.getX();
			pointer1.y = event.getY();*/
		} else if ((actions & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP
			|| (actions & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_CANCEL)
		{
			state = ControlState.QUIET;
		}
		return true;
	}

	public List<Graph> getData() {
		return data;
	}

	public void setData(List<Graph> data) {
		this.data = data;
	}

	public GridSettings getGridSettings() {
		return grid_settings;
	}

	public void setGridSettings(GridSettings grid_settings) {
		this.grid_settings = grid_settings;
	}

	public Vector2D getTranslate() {
		return graph_translate;
	}

	public void setTranslate(Vector2D translate) {
		graph_translate = translate;
	}

	public Vector2D getScale() {
		return graph_scale;
	}

	public Vector2D getScaleBasePoint() {
		return scale_base_point;
	}

	public void setScale(Vector2D scale, Vector2D base_point) {
		graph_scale = scale;
		scale_base_point = base_point;
	}

	private SurfaceHolder surface_holder;
	private List<Graph>   data;
	private GraphPainter  graph_painter;
	private GridSettings  grid_settings;
	private Vector2D  graph_translate;
	private Vector2D  graph_scale;
	private Vector2D  scale_base_point;
	private ControlState  state;
	private Vector2D  pointer1;
	//private RealVector2D  pointer2;
	//private RealVector2D  middle_pointer;
	//private double        pointer_distance;
}
