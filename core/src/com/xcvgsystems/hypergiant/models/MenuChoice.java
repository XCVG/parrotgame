package com.xcvgsystems.hypergiant.models;

public final class MenuChoice {
	
	private String title;
	private String description;
	private String destination;

	public MenuChoice(String title, String description, String destination)
	{
		this.title = title;
		this.description = description;
		this.destination = destination;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getDestination() {
		return destination;
	}
	
	public String toNiceString()
	{
		return title + " (" + description + ") " + destination; 
	}
	

}
