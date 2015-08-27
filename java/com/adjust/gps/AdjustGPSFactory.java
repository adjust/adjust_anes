package com.adjust.gps;

import com.adobe.fre.FREContext;

import android.content.Context;

public class AdjustGPSFactory {
	private static AdjustGPSWorker adjustGPSWorker = null;
	private static AdjustGPSCallbacksHandler adjustGPSCallbacksHandler = null;
	
	public static void initializeAdjustGPSWorker(Context context) {
		if (adjustGPSWorker == null) {
			adjustGPSWorker = new AdjustGPSWorker(context);
		}
	}
	
	public static void initializeAdjustGPSCallbacksHandler(FREContext freContext) {
		if (adjustGPSCallbacksHandler == null) {
			adjustGPSCallbacksHandler = new AdjustGPSCallbacksHandler(freContext);
		}
	}
	
	public static AdjustGPSWorker getAdjustGPSWorker() {
		return adjustGPSWorker;
	}
	
	public static AdjustGPSCallbacksHandler getAdjustGPSCallbacksHandler() {
		return adjustGPSCallbacksHandler;
	}
	
	public static boolean isAdjustGPSInitialized() {
		return (adjustGPSWorker != null) && (adjustGPSCallbacksHandler != null);
	}
}
