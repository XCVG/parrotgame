package com.xcvgsystems.hypergiant;

/**
 * PVars contains platform-specific variables.
 * The design is rather bad, but... consistency.
 * 
 * @author Chris
 *
 */
public class PVars
{
	public enum PlatformID
	{
		DESKTOP, ANDROID, IOS, WEB
	}
	
	public static PlatformID PLATFORM_ID;
}


