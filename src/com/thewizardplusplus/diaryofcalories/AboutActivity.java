package com.thewizardplusplus.diaryofcalories;

import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.about);

		WebView web_view = (WebView)findViewById(R.id.web_view);
		web_view.getSettings().setDefaultTextEncodingName("utf-8");
		if (Locale.getDefault().toString().startsWith("ru")) {
			web_view.loadUrl("file:///android_asset/web/page-ru.html");
		} else {
			web_view.loadUrl("file:///android_asset/web/page.html");
		}
	}
}

