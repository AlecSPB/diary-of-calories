package com.thewizardplusplus.diaryofcalories;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.graphics.Color;

public class HistoryActivity extends Activity {
	public enum ViewMode {
		LIST,
		GRAPH
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data_accessor = DataAccessor.getInstance(this);
		view_mode = data_accessor.getSettings().view_mode;

		setContentView(R.layout.history);
		mean = (TextView)findViewById(R.id.mean);
		list_view_button = (ImageButton)findViewById(R.id.list_view_button);
		graph_view_button = (ImageButton)findViewById(R.id.graph_view_button);
		list_view = (ListView)findViewById(R.id.list_view);
		graph_view = (GraphView)findViewById(R.id.graph_view);
		GridSettings graph_render_settings = graph_view.getGridSettings();
		graph_render_settings.background_color = Color.rgb(0xf0, 0xf0, 0xf0);
		graph_render_settings.graph_paint.setStrokeWidth(3);
		graph_render_settings.graph_paint.setColor(Color.rgb(0, 0xc0, 0));

		updateUI();

		List<DayData> all_days_data = data_accessor.getAllDaysData();
		if (all_days_data.size() > 0) {
			List<Map<String, Object>> items = new ArrayList<Map<String,
				Object>>();
			double mean = 0.0;
			for (DayData day_data: all_days_data) {
				mean += day_data.calories;

				Map<String, Object> map = new HashMap<String, Object>();
				map.put(LIST_ITEM_TITLE_NAME, Utils.convertDateToLocaleFormat(
					day_data.date));
				map.put(LIST_ITEM_CONTENT_NAME,
					Utils.convertNumberToLocaleFormat(day_data.calories) +
					getString(R.string.kcal));
				if (day_data.calories > data_accessor.getUserSettings().
					hard_limit)
				{
					map.put(LIST_ITEM_BACKGROUND_NAME, Color.rgb(0xf0, 0, 0));
				} else if (day_data.calories > data_accessor.getUserSettings().
					soft_limit)
				{
					map.put(LIST_ITEM_BACKGROUND_NAME, Color.rgb(0xf0, 0xf0,
						0));
				} else {
					map.put(LIST_ITEM_BACKGROUND_NAME, Color.rgb(0xf0, 0xf0,
						0xf0));
				}
				items.add(map);
			}
			mean /= all_days_data.size();
			this.mean.setText(Utils.convertNumberToLocaleFormat(mean));

			SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.list_item, new String[] {LIST_ITEM_TITLE_NAME,
				LIST_ITEM_CONTENT_NAME, LIST_ITEM_BACKGROUND_NAME}, new int[] {
				R.id.list_item_title, R.id.list_item_content,
				R.id.list_item_layout});
			adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
				public boolean setViewValue(View view, Object data, String
					data_text_representation)
				{
					if (view.getId() == R.id.list_item_layout) {
						view.setBackgroundColor((Integer)data);
						return true;
					}
					return false;
				}
			});

			list_view.setAdapter(adapter);

			List<Graph> graphs = new ArrayList<Graph>();
			Graph graph1 = new Graph();
			double date = 0.0;
			Collections.reverse(all_days_data);
			for (DayData day_data : all_days_data) {
				graph1.data.put(Double.valueOf(date), Double.valueOf(
					day_data.calories));
				date += 1.0;
			}
			graph1.paint.setColor(Color.rgb(0, 0xc0, 0));
			graphs.add(graph1);

			Graph graph2 = new Graph();
			graph2.data.put(Double.valueOf(0.0), Double.valueOf(
				(double)data_accessor.getUserSettings().soft_limit));
			graph2.data.put(Double.valueOf((double)all_days_data.size() - 1.0),
				Double.valueOf((double)data_accessor.getUserSettings()
				.soft_limit));
			graph2.paint.setColor(Color.rgb(0xc0, 0xc0, 0));
			graphs.add(graph2);

			Graph graph3 = new Graph();
			graph3.data.put(Double.valueOf(0.0), Double.valueOf(
				(double)data_accessor.getUserSettings().hard_limit));
			graph3.data.put(Double.valueOf((double)all_days_data.size() - 1.0),
				Double.valueOf((double)data_accessor.getUserSettings()
				.hard_limit));
			graph3.paint.setColor(Color.rgb(0xc0, 0, 0));
			graphs.add(graph3);

			graph_view.setData(graphs);
		} else {
			TextView label10 = (TextView)findViewById(R.id.label10);
			label10.setVisibility(View.VISIBLE);
			TextView label11 = (TextView)findViewById(R.id.label11);
			label11.setVisibility(View.GONE);
			mean.setVisibility(View.GONE);
			TextView label13 = (TextView)findViewById(R.id.label13);
			label13.setVisibility(View.GONE);
			TextView label14 = (TextView)findViewById(R.id.label14);
			label14.setVisibility(View.GONE);
			list_view_button.setVisibility(View.GONE);
			graph_view_button.setVisibility(View.GONE);
			list_view.setVisibility(View.GONE);
			graph_view.setVisibility(View.GONE);
		}
	}

	public void showListView(View view) {
		if (view_mode == ViewMode.GRAPH) {
			view_mode = ViewMode.LIST;

			Settings settings = new Settings();
			settings.view_mode = view_mode;
			data_accessor.setSettings(settings);

			updateUI();
		}
	}

	public void showGraphView(View view) {
		if (view_mode == ViewMode.LIST) {
			view_mode = ViewMode.GRAPH;

			Settings settings = new Settings();
			settings.view_mode = view_mode;
			data_accessor.setSettings(settings);

			updateUI();
		}
	}

	private static final String LIST_ITEM_TITLE_NAME =      "date";
	private static final String LIST_ITEM_CONTENT_NAME =    "calories";
	private static final String LIST_ITEM_BACKGROUND_NAME = "background_color";

	private DataAccessor data_accessor;
	private ViewMode     view_mode;
	private TextView     mean;
	private ImageButton  list_view_button;
	private ImageButton  graph_view_button;
	private ListView     list_view;
	private GraphView    graph_view;

	private void updateUI() {
		if (view_mode == ViewMode.LIST) {
			list_view_button.setBackgroundResource(
				R.drawable.left_black_toggle_button_checked);
			graph_view_button.setBackgroundResource(
				R.drawable.right_black_toggle_button);
			list_view.setVisibility(View.VISIBLE);
			graph_view.setVisibility(View.GONE);
		} else if (view_mode == ViewMode.GRAPH) {
			list_view_button.setBackgroundResource(
				R.drawable.left_black_toggle_button);
			graph_view_button.setBackgroundResource(
				R.drawable.right_black_toggle_button_checked);
			list_view.setVisibility(View.GONE);
			graph_view.setVisibility(View.VISIBLE);
		}
	}
}
