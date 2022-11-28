package com.xrbpowered.jwebs.examples;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.xrbpowered.jwebs.ContentType;
import com.xrbpowered.jwebs.Output;
import com.xrbpowered.jwebs.StaticWebServer;
import com.xrbpowered.jwebs.StringLibrary;

import static com.xrbpowered.jwebs.FileUtils.*;

public class MarkdownWebServer extends StaticWebServer {

	public static StringLibrary str = StringLibrary.load(getResource(MarkdownWebServer.class, "example.str"));
	
	public static int port = 3377;
	public static int threads = 10;
	public static String localPath = "html";
	public static String defaultCss = "/md_ghlike.css";
	
	public String css;
	
	public MarkdownWebServer(String context, String localPath, String css) {
		super(context, localPath, true);
		this.css = css;
	}

	public void sendMarkdown(HttpExchange http, File f) throws IOException {
		String md = loadString(f);
		JParsedown parsedown = new JParsedown();
		String body = parsedown.text(md);
		
		Output out = Output.start();
		out.printf(str.get("page"), parsedown.title, css, body);
		respond(http, ContentType.html, out.finish());
	}
	
	@Override
	public void sendFile(HttpExchange http, File f, String ext) throws IOException {
		if(ext.equals("md"))
			sendMarkdown(http, f);
		else
			super.sendFile(http, f, ext);
	}
	
	@Override
	public void handleGet(HttpExchange http, URI uri) throws IOException {
		String path = uri.getPath();
		if(path.isEmpty())
			path = "index.html";
		
		String ext = fileExt(path);
		File f = new File(local, path);
		if((ext.equals("htm") || ext.equals("html")) && !f.isFile()) {
			path = path.substring(0, path.length()-ext.length())+"md";
			f = new File(local, path);
			if(f.isFile()) {
				sendMarkdown(http, f);
				return;
			}
		}
		super.handleGet(http, uri);
	}
	
	public static void main(String[] args) {
		if(args.length>0)
			str.putAll(StringLibrary.load(new File(args[0])));
		port = str.getInt("port", port);
		threads = str.getInt("threads", threads);
		localPath = str.get("localPath", localPath);
		defaultCss = str.get("css", defaultCss);
		
		startServer("localhost", port, threads, new MarkdownWebServer("/", localPath, defaultCss));
		System.out.println("MarkdownWebServer started on port "+port);
	}
}
