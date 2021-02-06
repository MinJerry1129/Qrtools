package com.kessi.photopipcollagemaker.appconfig;

import android.util.Log;

public class Logger {

	public static void d(String tag, String msg) {
		if (DebugOptions.ENABLE_LOG) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DebugOptions.ENABLE_LOG) {
			Log.e(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DebugOptions.ENABLE_LOG) {
			Log.i(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (DebugOptions.ENABLE_LOG) {
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DebugOptions.ENABLE_LOG) {
			Log.w(tag, msg);
		}
	}

	public static void println(int priority, String tag, String msg) {
		if (DebugOptions.ENABLE_LOG) {
			Log.println(priority, tag, msg);
		}
	}

}
