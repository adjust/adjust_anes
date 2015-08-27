package com.adjust.gps;

import android.util.Log;
import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

public class AdjustGPSAds {
	private static final String TAG = "AdjustGPSAds";
	
	public static String GetAdvertisingId(Context context) {
		try {
			Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
			String id = info.getId();
			
			return id;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		return null;
	}
	
	public static boolean IsLimitAdTrackingEnabled(Context context) {
		try {
			Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
			boolean isLimitAdTrackingEnabled = info.isLimitAdTrackingEnabled();
			
			return isLimitAdTrackingEnabled;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		return false;
	}
}
