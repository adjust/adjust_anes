package com.adjust.gps;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

import java.util.HashMap;
import java.util.Map;

public class AdjustGPSContext extends FREContext {
	public static String Initialize = "initialize";
    public static String GetAdvertisingId = "getAdvertisingId";
    public static String IsLimitAdTrackingEnabled = "isLimitAdTrackingEnabled";

    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> functions = new HashMap<String, FREFunction>();

        functions.put(AdjustGPSContext.Initialize, new AdjustGPSFunction(AdjustGPSContext.Initialize));
        functions.put(AdjustGPSContext.GetAdvertisingId, new AdjustGPSFunction(AdjustGPSContext.GetAdvertisingId));
        functions.put(AdjustGPSContext.IsLimitAdTrackingEnabled, new AdjustGPSFunction(AdjustGPSContext.IsLimitAdTrackingEnabled));

        return functions;
    }

    @Override
    public void dispose() {

    }
}