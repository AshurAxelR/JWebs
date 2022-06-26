package com.xrbpowered.jwebs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public class StringLibrary {

	private HashMap<String, String> map = new HashMap<>(); 
	
	public StringLibrary(String text) throws IOException {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(text);
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.trim().isEmpty())
				continue;
			String key, value;
			if(line.endsWith("$$")) {
				key = line.substring(0, line.length()-2);
				StringBuilder sb = new StringBuilder();
				boolean first = true;
				while(in.hasNextLine()) {
					String s = in.nextLine();
					if(s.equals("$$"))
						break;
					if(!first)
						sb.append("\n");
					first = false;
					sb.append(s);
				}
				value = sb.toString();
			}
			else {
				int eq = line.indexOf("=");
				if(eq<0)
					throw new IOException("Expected $$ or =");
				key = line.substring(0, eq);
				value = line.substring(eq+1).trim();
			}
			key = key.replaceFirst("\\[.*\\]", "").trim();
			if(key.matches("[A-Za-z0-9_\\.]+\\\\?")) {
				if(key.endsWith("\\")) {
					value = value.replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t")
							.replaceAll("\\\\s", " ").replaceAll("\\\\r", "\r").replaceAll("\\\\\\\\", "\\");
					key = key.substring(0, key.length()-1);
				}
				map.put(key, value);
			}
			else
				throw new IOException("Bad string id format");
		}
		in.close();
	}
	
	public String get(String key) {
		return map.get(key);
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
