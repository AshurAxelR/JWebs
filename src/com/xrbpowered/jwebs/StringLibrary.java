package com.xrbpowered.jwebs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public class StringLibrary {

	private HashMap<String, String> map = new HashMap<>(); 
	
	public StringLibrary(String text) throws IOException {
		try(Scanner in = new Scanner(text)) {
			while(in.hasNextLine()) {
				String line = in.nextLine();
				if(line.trim().isEmpty())
					continue;
				String key, value;
				if(line.endsWith("$$")) {
					key = line.substring(0, line.length()-2);
					StringBuilder sb = new StringBuilder();
					while(in.hasNextLine()) {
						String s = in.nextLine();
						if(s.equals("$$"))
							break;
						sb.append(s);
						sb.append("\n");
					}
					value = sb.toString();
				}
				else {
					int eq = line.indexOf("=");
					if(eq<0) {
						key = line;
						value = null;
					}
					else {
						key = line.substring(0, eq);
						value = line.substring(eq+1).trim();
					}
				}
				if(!key.startsWith("#")) {
					if(value==null)
						throw new IOException("Expected $$ or =");
					key = key.replaceFirst("\\[.*\\]", "").trim();
					if(key.matches("[A-Za-z0-9_\\.]+\\\\?")) {
						if(key.endsWith("\\")) {
							value = value.replace("\\n", "\n").replace("\\t", "\t")
									.replace("\\s", " ").replace("\\r", "\r").replace("\\\\", "\\");
							key = key.substring(0, key.length()-1);
						}
						map.put(key, value);
					}
					else
						throw new IOException("Bad key format");
				}
			}
		}
	}
	
	public StringLibrary putAll(StringLibrary lib) {
		if(lib!=null)
			map.putAll(lib.map);
		return this;
	}
	
	public String get(String key) {
		return map.get(key);
	}
	
	public String get(String key, String def) {
		String s = get(key);
		if(s==null)
			return def;
		else
			return s;
	}
	
	public Integer getInt(String key, Integer def) {
		String s = get(key);
		if(s==null)
			return def;
		else {
			try {
				return Integer.parseInt(s);
			}
			catch (NumberFormatException e) {
				System.err.println("StringLibrary: bad number format for "+key);
				return def;
			}
		}
	}
	
	public static StringLibrary load(InputStream s) {
		try {
			String text = FileUtils.loadString(s);
			return new StringLibrary(text);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static StringLibrary load(File f) {
		try {
			String text = FileUtils.loadString(f);
			return new StringLibrary(text);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
