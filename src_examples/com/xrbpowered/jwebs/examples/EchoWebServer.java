package com.xrbpowered.jwebs.examples;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.xrbpowered.jwebs.ContentType;
import com.xrbpowered.jwebs.FavIconServer;
import com.xrbpowered.jwebs.FileUtils;
import com.xrbpowered.jwebs.Output;
import com.xrbpowered.jwebs.StaticWebServer;
import com.xrbpowered.jwebs.WebServerBase;

public class EchoWebServer extends WebServerBase {

	public static final int port = 3377;
	public static final int threads = 10;

	public final String info; 
	
	public EchoWebServer(String context, String info) {
		super(context);
		this.info = info;
	}

	@Override
	public void handleGet(HttpExchange http, URI uri) throws IOException {
		Output out = Output.start();
		out.printf("EchoWebServer (%s)\n\n", info);
		out.printf("Remote address: %s\n", http.getRemoteAddress().toString());
		out.printf("Local address: %s\n\n", http.getLocalAddress().toString());
		out.printf("Context: %s\n", context.getPath());
		out.printf("Request URI: %s\n", uri.toString());
		out.printf("Path: %s\n\n", uri.getPath());
		out.printf("Query: %s\n", uri.getQuery());
		out.printf("Suggested content type: %s\n\n", ContentType.get(FileUtils.fileExt(uri.getPath())));
		out.println("Request headers:");

		for(Map.Entry<String, List<String>> hdr : http.getRequestHeaders().entrySet()) {
			out.printf("%s : [", hdr.getKey());
			boolean first = true;
			for(String s : hdr.getValue()) {
				if(!first) out.print(",");
				first = false;
				out.print(s);
			}
			out.println("]");
		}
		respond(http, ContentType.text, out.finish());
	}

	public static void main(String[] args) {
		startServer("localhost", port, threads,
			new StaticWebServer("/s/", "html"),
			new EchoWebServer("/echo", "ECHO"),
			new FavIconServer("html/favicon.png"),
			new EchoWebServer("/", "ROOT")
		);
		System.out.println("EchoWebServer started on port "+port);
	}
	
}
