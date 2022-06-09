package com.xrbpowered.jwebs;

import static com.xrbpowered.jwebs.FileUtils.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;

public class StaticWebServer extends WebServerBase {

	public File local;
	public boolean supportCache;
	
	public StaticWebServer(String context, String localPath, boolean supportCache) {
		super(context);
		local = new File(localPath);
		this.supportCache = supportCache;
	}

	public StaticWebServer(String context, String localPath) {
		this(context, localPath, true);
	}

	public static void sendFileCached(HttpExchange http, File f, String contentType, boolean supportCache) throws IOException {
		long time = f.lastModified();
		if(supportCache)
			http.getResponseHeaders().set("Last-Modified", HttpTime.format(time));
		if(!supportCache || isNewer(http, time))
			respond(http, contentType, loadBytes(f));
		else
			respondEmpty(http, HTTP_NOT_MODIFIED, contentType);
	}
	
	public void sendFile(HttpExchange http, File f, String ext) throws IOException {
		if(ext.equals("css") && http.getRequestHeaders().getFirst("User-agent").contains("Firefox")) {
			try {
				Thread.sleep(15);  // FIXME why FF blinks without this?
			} catch (InterruptedException e) {
			}
		}

		sendFileCached(http, f, ContentType.get(ext), supportCache);
	}
	
	@Override
	public void handleGet(HttpExchange http, URI uri) throws IOException {
		String path = uri.getPath();
		File f = new File(local, path);
		if(f.isFile())
			sendFile(http, f, fileExt(path));
		else
			sendError(http, HTTP_NOT_FOUND);
	}

}
