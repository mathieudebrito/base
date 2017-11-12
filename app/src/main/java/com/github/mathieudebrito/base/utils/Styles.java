package com.github.mathieudebrito.base.utils;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public class Styles {

	public static Drawable getDrawable(TypedArray arr, int res) {
        if (arr.hasValue(res)) {
			return arr.getDrawable(res);
		} else {
			return null;
		}
	}

	public static String getString(TypedArray arr, int res) {
		if (arr.hasValue(res)) {
			return arr.getString(res);
		} else {
			return null;
		}
	}
	public static boolean getBoolean(TypedArray arr, int res) {
		if (arr.hasValue(res)) {
			return arr.getBoolean(res, false);
		} else {
			return false;
		}
	}
}