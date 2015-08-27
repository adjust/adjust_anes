package com.adjust.gps;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AdjustGPSFunction implements FREFunction {
	private static final String TAG = "AdjustGPSFunction";
    private String functionName;
    
    public AdjustGPSFunction(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
    	try {
    		if (functionName == AdjustGPSContext.Initialize) {
    			return initialize(freContext, freObjects);
    		} else {
    			if (AdjustGPSFactory.isAdjustGPSInitialized()) {
			        if (functionName == AdjustGPSContext.GetAdvertisingId) {
			            return getAdvertisingId(freContext, freObjects);
			        }
			        
			        if (functionName == AdjustGPSContext.IsLimitAdTrackingEnabled) {
			        	return isLimitAdTrackingEnabled(freContext, freObjects);
			        }
    			} else {
    				Log.e(TAG, "Please, initialize Adjust GPS before calling any of it's methods!");
    			}
    		}
    	} catch (Exception e) {
        	Log.e(TAG, e.getMessage());
        }

        return null;
    }

    private FREObject initialize(FREContext freContext, FREObject[] freObjects) {
    	try {
	    	// Initialize worker and callback handler.
	    	AdjustGPSFactory.initializeAdjustGPSWorker(freContext.getActivity());
	    	AdjustGPSFactory.initializeAdjustGPSCallbacksHandler(freContext);
	    	
	    	return getAnswerForBoolean(true);
    	} catch (Exception e) {
    		Log.e(TAG, e.getMessage());
    	}
    	
    	return getAnswerForBoolean(false);
    }
    
    private FREObject getAdvertisingId(FREContext freContext, FREObject[] freObjects) {
    	try {
    		AdjustGPSFactory.getAdjustGPSWorker().getAdvertisingId();
    		
    		return getAnswerForBoolean(true);
    	} catch (Exception e) {
    		Log.e(TAG, e.getMessage());
    	}
    	
    	return getAnswerForBoolean(false);
    }
    
    private FREObject isLimitAdTrackingEnabled(FREContext freContext, FREObject[] freObjects) {
        try {
        	AdjustGPSFactory.getAdjustGPSWorker().isLimitAdTrackingEnabled();
        	
        	return getAnswerForBoolean(true);
        } catch (Exception e) {
        	Log.e(TAG, e.getMessage());
        }
        
        return getAnswerForBoolean(false);
    }
    
    private FREObject getAnswerForBoolean(boolean isPositive) {
    	try {
    		return FREObject.newObject(isPositive);
    	} catch (Exception e) {
    		Log.e(TAG, e.getMessage());
    	}
    	
    	return null;
    }
}