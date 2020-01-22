package me.moderator_man.srv.pages;

import java.util.HashMap;
import java.util.Map;

public class PageManager
{
	private Map<String, Page> pages;
	
	public PageManager()
	{
		pages = new HashMap<String, Page>();
	}
	
	public void register(String uri, Page page)
	{
		pages.put(uri, page);
	}
	
	public Page getPage(String uri)
	{
		return pages.get(uri);
	}
}
