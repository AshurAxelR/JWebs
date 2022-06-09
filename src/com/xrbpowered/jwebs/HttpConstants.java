package com.xrbpowered.jwebs;

public interface HttpConstants {

	public static final int HTTP_OK = 200; 

	public static final int HTTP_NOT_MODIFIED = 304; 

	public static final int HTTP_BAD_REQUEST = 400; 
	public static final int HTTP_UNAUTHORIZED = 401; 
	public static final int HTTP_FORBIDDEN = 403; 
	public static final int HTTP_NOT_FOUND = 404;

	public static final int HTTP_SEVER_ERROR = 500;
	public static final int HTTP_NOT_IMPLEMENTED = 501;
	public static final int HTTP_SERVICE_UNAVAILABLE = 503;

	public static final String GET = "GET";
	public static final String HEAD = "HEAD";
	public static final String POST = "POST";
	
	public static String errorName(int code) {
		switch(code) {
			case HTTP_BAD_REQUEST:
				return "Bad Request";
			case HTTP_UNAUTHORIZED:
				return "Unauthorized";
			case HTTP_FORBIDDEN:
				return "Forbidden";
			case HTTP_NOT_FOUND:
				return "Not Found";
			case HTTP_SEVER_ERROR:
				return "Internal Server Error";
			case HTTP_NOT_IMPLEMENTED:
				return "Not Implemented";
			case HTTP_SERVICE_UNAVAILABLE:
				return "Service Unavailable";
			default:
				return "";
		}
	}
	
}
