package org.dhis2.ehealthMobile.utils;

import android.content.Context;

public class ResourcesUtils {

	private ResourcesUtils(){}

	public static String getStringFromStringId(Context context, String resIdString){

		int resId = context.getResources().getIdentifier(resIdString, "string", context.getPackageName());
		if(resId == 0) return null;

		return context.getResources().getString(resId);
	}

}
