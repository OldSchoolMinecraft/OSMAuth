package me.moderator_man.srv.pages;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD.Response;

public abstract class Page
{
	public abstract Response exec(Map<String, String> params);
}
