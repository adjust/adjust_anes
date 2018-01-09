package com.adjust.gps;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

import java.util.HashMap;
import java.util.Map;

public class AdjustGPSContext extends FREContext {
    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
        return functions;
    }

    @Override
    public void dispose() {

    }
}
