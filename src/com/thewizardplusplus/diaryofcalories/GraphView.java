package com.thewizardplusplus.diaryofcalories;

import java.util.List;
import java.util.ArrayList;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;

public class GraphView extends SurfaceView implements SurfaceHolder.Callback {
	public GraphView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);

		surface_holder = getHolder();
		surface_holder.addCallback(this);
	}

	public void surfaceCreated(SurfaceHolder surface_holder) {
		graph_painter = new GraphPainter(
			this.surface_holder,
			data,
			grid_settings
		);
		graph_painter.start();
	}

	public void surfaceChanged(
		SurfaceHolder surface_holder,
		int format,
		int width,
		int height
	) {
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
			state = GraphControlState.TRANSLATING;

			pointer1.x = event.getX();
			pointer1.y = event.getY();
		} else if (
			(actions & MotionEvent.ACTION_MASK)
			== MotionEvent.ACTION_POINTER_DOWN
		) {
			state = GraphControlState.SCALING;

			pointer1.x = event.getX(0);
			pointer1.y = event.getY(0);
			pointer2.x = event.getX(1);
			pointer2.y = event.getY(1);

			middle_pointer = Vector2D.add(pointer1, pointer2).div(2.0);
			pointer_distance = Vector2D.sub(pointer1, pointer2).length();
		} else if (
			(actions & MotionEvent.ACTION_MASK)
			== MotionEvent.ACTION_MOVE
		) {
			if (state == GraphControlState.TRANSLATING) {
				Vector2D current_pointer1 = new Vector2D(
					event.getX(),
					event.getY()
				);
				Vector2D shift = Vector2D.sub(
					current_pointer1,
					pointer1
				);

				graph_translate.add(shift);
				graph_painter.setTranslate(graph_translate);

				pointer1 = current_pointer1;
			} else if (state == GraphControlState.SCALING) {
				Vector2D current_pointer1 = new Vector2D(
					event.getX(0),
					event.getY(0)
				);
				Vector2D current_pointer2 = new Vector2D(
					event.getX(1),
					event.getY(1)
				);
				Vector2D current_middle_pointer = Vector2D.add(
					current_pointer1,
					current_pointer2
				).div(2.0);
				Vector2D shift = Vector2D.sub(
					current_middle_pointer,
					middle_pointer
				);

				graph_translate.add(shift);
				graph_painter.setTranslate(graph_translate);

				double current_pointer_distance = Vector2D.sub(
					current_pointer1,
					current_pointer2
				).length();
				double distance_ratio =
					current_pointer_distance
					/ pointer_distance;
				graph_scale.x *= distance_ratio;
				graph_scale.y *= distance_ratio;
				graph_painter.setScale(graph_scale);

				pointer1 = current_pointer1;
				pointer2 = current_pointer2;
				middle_pointer = current_middle_pointer;
				pointer_distance = current_pointer_distance;
			}
		} else if (
			(actions & MotionEvent.ACTION_MASK)
			== MotionEvent.ACTION_POINTER_UP
		) {
			state = GraphControlState.TRANSLATING;

			pointer1.x = event.getX();
			pointer1.y = event.getY();
		} else if (
			(actions & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP
			|| (actions & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_CANCEL
		) {
			state = GraphControlState.STABLE;
		}

		return true;
	}

	@Override
	public boolean performClick() {
		super.performClick();
		return true;
	}

	public void setData(List<Graph> data) {
		this.data = data;
	}

	public GridSettings getGridSettings() {
		return grid_settings;
	}

	private SurfaceHolder surface_holder;
	private List<Graph> data = new ArrayList<Graph>();
	private GraphPainter graph_painter;
	private GridSettings grid_settings = new GridSettings();
	private Vector2D graph_translate = new Vector2D(
		grid_settings.grid_step,
		-grid_settings.grid_step
	);
	private Vector2D graph_scale = new Vector2D(1.0, 1.0);
	private GraphControlState state = GraphControlState.STABLE;
	private Vector2D pointer1 = new Vector2D();
	private Vector2D pointer2 = new Vector2D();
	private Vector2D middle_pointer = new Vector2D();
	private double pointer_distance = 0.0;
}
