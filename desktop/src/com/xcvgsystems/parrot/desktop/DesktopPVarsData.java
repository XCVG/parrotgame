package com.xcvgsystems.parrot.desktop;

import com.xcvgsystems.hypergiant.PVars.PlatformID;
import com.xcvgsystems.hypergiant.PVarsInterface;

/**
 * An implementation of PVarsInterface for desktop.
 * 
 * @author Chris
 *
 */
public class DesktopPVarsData implements PVarsInterface
{

	@Override
	public PlatformID getPlatform()
	{
		return PlatformID.DESKTOP;
	}

}
