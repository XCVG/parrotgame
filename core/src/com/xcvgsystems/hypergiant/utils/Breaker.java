package com.xcvgsystems.hypergiant.utils;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

/**
 * A lousy INI parser. Currently does not support comments.
 * @author Chris
 *
 */
public class Breaker {

	/**
	 * Loads the contents of an INI file into a Map.
	 * @param src A String containing the contents of a valid INI file.
	 * @return A Map containing sections which have key-value pairs (also a Map).
	 */
	public static Map<String, Map<String, String>> parseFile(String src)
	{
		Map<String, Map<String, String>> result = new LinkedHashMap<String, Map<String, String>>();
		
		Map<String, String> sections = getSections(src);
		
		Iterator<Entry<String, String>> sIt = sections.entrySet().iterator();
		while(sIt.hasNext())
		{
			Entry<String, String> entry = sIt.next();
			result.put(entry.getKey(), parseSection(entry.getValue()));
		}
		
		return result;
	}
	
	//breaks into sections
	protected static Map<String, String> getSections(String src)
	{
		
		Map<String, String> sections = new LinkedHashMap<String, String>();
		
		Scanner scan = new Scanner(src);
		
		boolean inSection = false;
		String secStr = "", secHead="";

		//find the first heading
		while(scan.hasNext())
		{
			//System.out.println(secStr);
			
			String thisLine = scan.nextLine().trim();
			if(thisLine.startsWith("[") && thisLine.endsWith("]"))
			{
				//this is a heading! close existing section and create a new one!
				
				//if we're in a section, close it first
				if(inSection)
				{
					sections.put(secHead, secStr);
				}
				
				//create a new section
				secStr = "";
				secHead = thisLine.substring(1, thisLine.length() - 1).trim();
				inSection = true;

			}
			else
			{
				//otherwise, add the current line to the section string
				secStr += thisLine + "\n";
			}
		}
		
		//put the last section
		if(inSection)
			sections.put(secHead, secStr);
		
		scan.close();
		
		return sections;
	}
	
	//break section into key/value pairs
	protected static Map<String, String> parseSection(String src)
	{
		Map<String, String> lines = new LinkedHashMap<String, String>();
		
		Scanner scan = new Scanner(src);
		
		while(scan.hasNext())
		{
		
			SimpleEntry<String, String> thisLine = parseLine(scan.nextLine()); //we'll let another method deal with the details
			
			lines.put(thisLine.getKey(), thisLine.getValue());
		}
		
		scan.close();
		
		return lines;
	}
	
	//line breakdown
	protected static SimpleEntry<String, String> parseLine(String src)
	{
		String key, rawValue, value;
		
		String line = src.trim();
		//String line = src;
		
		int pos = line.indexOf('=');
		key = line.substring(0, pos);
		rawValue = line.substring(pos+1, line.length());
		
		//deal with quoted values and non-quoted values separately
		if(rawValue.startsWith("\""))
		{
			value = rawValue.substring(1, rawValue.length() - 1);
		}
		else
		{
			value = rawValue;
		}
		
		return new SimpleEntry<String, String>(key, value);
		
	}
	


}
