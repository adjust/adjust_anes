package com.adjust.installref;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

public class InstallRefExtension implements FREExtension {
    private static FREContext context;

    @Override
    public void initialize() { }

    @Override
    public FREContext createContext(String s) {
        if (context == null) {
            context = new InstallRefContext();
        }

        return context;
    }

    @Override
    public void dispose() { }
}
