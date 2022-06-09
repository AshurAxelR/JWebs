package com.xrbpowered.jwebs;

import static com.xrbpowered.jwebs.FileUtils.loadBytes;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;

public class FavIconServer extends WebServerBase {

	public File local;
	public String contentType;
	
	public FavIconServer(String localPath, String contentType) {
		super("/favicon.ico");
		local = new File(localPath);
		this.contentType = contentType;
	}

	public FavIconServer(String localPath) {
		this(localPath, ContentType.get(FileUtils.fileExt(localPath)));
	}
		
	@Override
	public void handleGet(HttpExchange http, URI uri) throws IOException {
		if(uri.getPath().isEmpty() && local.isFile())
			respond(http, contentType, loadBytes(local));
		else
			sendError(http, HTTP_NOT_FOUND);
	}

}
