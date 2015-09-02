package com.adjust.gps;

import com.adobe.fre.FREContext;

public class AdjustGPSCallbacksHandler implements IAdjustGPSCallbacks {
	private static final String KEY_ADVERTISING_ID 			= "KEY_ADVERTISING_ID";
	private static final String KEY_IS_AD_TRACKING_ENABLED 	= "KEY_IS_AD_TRACKING_ENABLED";
	
	private FREContext freContext;
	
	public AdjustGPSCallbacksHandler(FREContext freContext) {
		this.freContext = freContext;
	}
	
	@Override
	public void cbGetAdvertisingId(String advertisingId) {
		String response = KEY_ADVERTISING_ID + "=" + advertisingId;
		
		this.freContext.dispatchStatusEventAsync("adjust_gps_data", response);
	}

	@Override
	public void cbIsLimitAdTrackingEnabled(boolean isLimitAdTrackingEnabled) {
		String response = KEY_IS_AD_TRACKING_ENABLED + "=" + isItTrueOrFalse(!isLimitAdTrackingEnabled);
		
		this.freContext.dispatchStatusEventAsync("adjust_gps_data", response);
	}
	
	private String isItTrueOrFalse(boolean variable) {
		return variable == true ? "true" : "fasle";
	}
}
