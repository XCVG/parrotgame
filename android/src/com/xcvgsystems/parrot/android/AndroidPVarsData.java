package com.xcvgsystems.parrot.android;

import com.xcvgsystems.hypergiant.PVars.PlatformID;
import com.xcvgsystems.hypergiant.PVarsInterface;

/**
 * An implementation of PVarsInterface for Android.
 * 
 * @author Chris
 *
 */
public class AndroidPVarsData implements PVarsInterface
{

	@Override
	public PlatformID getPlatform()
	{
		return PlatformID.ANDROID;
	}

}
