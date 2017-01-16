package org.dhis2.ehealthmobile;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.BuildConfig;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public abstract class BaseRoboelectricTest {

	private static final Gson gson = new Gson();

	protected Context getContext(){
		return RuntimeEnvironment.application;
	}

	protected <T> T loadJson(String filename, Class<T> clazz){
		return gson.fromJson(loadJsonString(filename), clazz);
	}

	protected String loadJsonString(String filename){

		if(!filename.endsWith(".json"))
			filename = filename + ".json";

		return readRawTextFile(String.format("json/%s", filename));
	}

	protected String readRawTextFile(String filename){

		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);//getContext().getResources().openRawResource(id);
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				builder.append(line).append('\n');
			}

			return builder.toString();
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}

	protected TextView textViewWithText(View view, String text) {
		if (view instanceof TextView) {
			String t = ((TextView) view).getText().toString();
			return t.equals(text) ? (TextView) view : null;
		} else if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			for (int i = 0; i < group.getChildCount(); i++) {
				View child = group.getChildAt(i);
				TextView childTextView = textViewWithText(child, text);
				if (childTextView != null)
					return childTextView;
			}
		}
		return null;
	}

}
