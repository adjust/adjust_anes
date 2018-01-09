package com.adjust.installref;

import java.util.HashMap;
import java.util.Map;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

public class InstallRefContext extends FREContext
{
	public InstallRefContext() {}
	
	@Override
	public Map<String, FREFunction> getFunctions()
	{
		return new HashMap<String, FREFunction>();
	}
	
	@Override
	public void dispose() {};
}
