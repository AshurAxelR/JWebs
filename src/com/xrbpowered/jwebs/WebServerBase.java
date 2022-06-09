package com.xrbpowered.jwebs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public abstract class WebServerBase implements HttpHandler, HttpConstants {

	public final URI context;
	
	public WebServerBase(String context) {
		this.context = URI.create(context);
	}
	
	public static void respond(HttpExchange http, int code, String contentType, byte[] response) throws IOException {
		Headers headers = http.getResponseHeaders();
		headers.set("Content-Type", contentType);
		if(response!=null) {
			http.sendResponseHeaders(code, response.length);
			if(!http.getRequestMethod().equals(HEAD)) {
				OutputStream outputStream = http.getResponseBody();
				outputStream.write(response);
				outputStream.flush();
				outputStream.close();
			}
		}
		else {
			http.sendResponseHeaders(code, 0);
		}
	}

	public static void respond(HttpExchange http, int code, String contentType, String response) throws IOException {
		respond(http, code, contentType, response.getBytes(StandardCharsets.UTF_8));
	}

	public static void respond(HttpExchange http, String contentType, byte[] response) throws IOException {
		respond(http, HTTP_OK, contentType, response);
	}

	public static void respond(HttpExchange http, String contentType, String response) throws IOException {
		respond(http, HTTP_OK, contentType, response.getBytes(StandardCharsets.UTF_8));
	}

	public static void sendError(HttpExchange http, int code, String message) throws IOException {
		String s = String.format("ERROR %d %s\n", code, HttpConstants.errorName(code));
		if(message!=null && !message.isEmpty())
			s += message;
		respond(http, code, ContentType.text, s);
	}

	public static void sendError(HttpExchange http, int code) throws IOException {
		sendError(http, code, null);
	}

	public abstract void handleGet(HttpExchange http, URI uri) throws IOException;
	
	public void handle(HttpExchange http, String method, URI uri) throws IOException {
		switch(method) {
			case GET:
			case HEAD:
				try {
					handleGet(http, uri);
				}
				catch (Exception e) {
					e.printStackTrace();
					sendError(http, HTTP_SEVER_ERROR);
				}
				break;
			default:
				sendError(http, HTTP_NOT_IMPLEMENTED, "Unsupported HTTP method: "+method);
		}
	}

	@Override
	public void handle(HttpExchange http) throws IOException {
		handle(http, http.getRequestMethod(), context.relativize(http.getRequestURI()));
	}

	public static HttpServer startServer(String address, int port, int threads, WebServerBase... web) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(address, port), 0);
			for(WebServerBase w : web)
				server.createContext(w.context.getPath(), w);
			
			ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
			server.setExecutor(threadPoolExecutor);
			server.start();
			
			return server;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
