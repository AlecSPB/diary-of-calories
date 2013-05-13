package com.thewizardplusplus.diaryofcalories;

import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		WebView web_view = (WebView)findViewById(R.id.web_view);
		web_view.getSettings().setDefaultTextEncodingName("utf-8");
		if (Locale.getDefault().toString().startsWith("ru")) {
			web_view.loadUrl("file:///android_asset/about_page/page-ru.html");
		} else {
			web_view.loadUrl("file:///android_asset/about_page/page.html");
		}
	}
}
