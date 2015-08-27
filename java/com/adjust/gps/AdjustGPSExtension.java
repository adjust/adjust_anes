package com.adjust.gps;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

public class AdjustGPSExtension implements FREExtension {
    private static FREContext context;

    @Override
    public void initialize() { }

    @Override
    public FREContext createContext(String s) {
        if (context == null) {
            context = new AdjustGPSContext();
        }

        return context;
    }

    @Override
    public void dispose() { }
}