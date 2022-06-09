package com.xrbpowered.jwebs;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;

import static com.xrbpowered.jwebs.FileUtils.*;

public class StaticWebServer extends WebServerBase {

	public File local;
	
	public StaticWebServer(String context, String localPath) {
		super(context);
		local = new File(localPath);
	}

	public void sendFile(HttpExchange http, File f, String ext) throws IOException {
		if(http.getRequestHeaders().getFirst("User-agent").contains("Firefox")
				&& ext.equals("css")) {
			try {
				Thread.sleep(15);  // FIXME why FF blinks without this?
			} catch (InterruptedException e) {
			}
		}

		respond(http, ContentType.get(ext), loadBytes(f));
	}
	
	@Override
	public void handleGet(HttpExchange http, URI uri) throws IOException {
		String path = uri.getPath();
		File f = new File(local, path);
		if(f.isFile())
			sendFile(http, f, fileExt(path));
		else
			sendError(http, HTTP_NOT_FOUND, "Not found");
	}

}
