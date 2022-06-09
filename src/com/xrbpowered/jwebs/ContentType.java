package com.xrbpowered.jwebs;

import java.util.HashMap;

public abstract class ContentType {

	public static final String text = "text/plain";
	public static final String html = "text/html";
	public static final String css = "text/css";
	public static final String javascript = "text/javascript";

	public static final String jpeg = "image/jpeg";
	public static final String png = "image/png";
	
	public static final String binary = "application/octet-stream";

	private static HashMap<String, String> map = new HashMap<>();
	static {
		map.put("css", css);
		map.put("htm", html);
		map.put("html", html);
		map.put("js", javascript);
		map.put("mjs", javascript);
		map.put("txt", text);
		
		map.put("bmp", "image/bmp");
		map.put("gif", "image/gif");
		map.put("jpeg", jpeg);
		map.put("jpg", jpeg);
		map.put("ico", "image/x-icon");
		map.put("png", png);
		map.put("svg", "image/svg+xml");
		map.put("webp", "image/webp");

		map.put("otf", "font/otf");
		map.put("ttf", "font/ttf");
		map.put("woff", "font/woff");

		map.put("avi", "video/x-msvideo");
		map.put("mid", "audio/midi");
		map.put("midi", "audio/midi");
		map.put("mp3", "audio/mpeg");
		map.put("mp4", "audio/mp4");
		map.put("mpeg", "video/mpeg");
		map.put("oga", "audio/ogg");
		map.put("ogg", "audio/ogg");
		map.put("ogv", "video/ogg");
		map.put("swf", "application/x-shockwave-flash");
		map.put("wav", "audio/wav");
		map.put("weba", "audio/webm");
		map.put("webm", "video/webm");

		map.put("csv", "text/csv");
		map.put("json", "application/json");
		map.put("xml", "application/xml");

		map.put("gz", "application/gzip");
		map.put("jar", "application/java-archive");
		map.put("mpkg", "application/vnd.apple.installer+xml");
		map.put("rar", "application/vnd.rar");
		map.put("tar", "application/x-tar");
		map.put("zip", "application/zip");
		map.put("7z", "application/x-7z-compressed");
		
		map.put("doc", "application/msword");
		map.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		map.put("odp", "application/vnd.oasis.opendocument.presentation");
		map.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		map.put("odt", "application/vnd.oasis.opendocument.text");
		map.put("pdf", "application/pdf");
		map.put("ppt", "application/vnd.ms-powerpoint");
		map.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		map.put("rtf", "application/rtf");
		map.put("xls", "application/vnd.ms-excel");
		map.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}
	
	public static String get(String ext) {
		String s = map.get(ext);
		return (s==null) ? binary : s;
	}
	
}
