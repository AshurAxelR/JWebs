package com.xrbpowered.jwebs.start;

import static com.xrbpowered.parser.json.JsonUtils.*;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xrbpowered.jwebs.core.FavIconServer;
import com.xrbpowered.jwebs.core.StaticWebServer;
import com.xrbpowered.jwebs.core.WebServerBase;
import com.xrbpowered.jwebs.markdown.MarkdownWebServer;
import com.xrbpowered.parser.json.JsonParser;

public class StartJWebs {

	public static String address = "localhost";
	public static int port = 3377;
	public static int threads = 3;

	public static List<WebServerBase> web = new ArrayList<>();

	private static class ConfigException extends Exception {
		public ConfigException(String msg) {
			super(msg);
		}
	}

	private static String require(Map<String, Object> o, String param) throws ConfigException {
		String s = string(o, param);
		if(s == null)
			throw new ConfigException("Module requires " + param);
		return s;
	}

	private static WebServerBase parseWebConfig(Object json) throws ConfigException {
		Map<String, Object> o = object(json);
		String server = string(o, "server");
		if(server == null)
			throw new ConfigException("Server module required");
		switch(server) {

			case "favicon": {
				System.out.printf("FavIconServer module at /favicon.ico\n");
				String localPath = require(o, "localPath");
				return new FavIconServer(localPath);
			}

			case "static": {
				String context = require(o, "context");
				System.out.printf("StaticWebServer module at %s\n", context);
				String localPath = require(o, "localPath");
				return new StaticWebServer(context, localPath);
			}

			case "markdown": {
				String context = require(o, "context");
				System.out.printf("MarkdownWebServer module at %s\n", context);
				String localPath = require(o, "localPath");
				String css = require(o, "css");
				return new MarkdownWebServer(context, localPath, css);
			}

			default:
				throw new ConfigException("Unknown module " + server);
		}
	}

	private static void parseConfig(String[] args) throws ConfigException {
		if(args.length < 1)
			throw new ConfigException("Server config not specified");

		Object json = new JsonParser().parse(new File(args[0]));
		if(json == null)
			throw new ConfigException("Bad server config syntax");

		try {
			Map<String, Object> o = object(json);
			address = string(o, "address", address);
			port = (int) number(o, "port", port);
			threads = (int) number(o, "threads", threads);

			for(Object wjson : array(o.get("web"))) {
				web.add(parseWebConfig(wjson));
			}
			if(web.isEmpty())
				throw new ConfigException("No web base in server config");
		}
		catch(ConfigException e) {
			throw e;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new InvalidParameterException("Server config error");
		}
	}

	public static void main(String[] args) {
		try {
			parseConfig(args);
		}
		catch(ConfigException e) {
			System.err.println(e.getMessage());
			return;
		}

		if(WebServerBase.startServer("localhost", port, threads, web) != null)
			System.out.printf("JWebs started on %s port %d\n", address, port);
	}

}
