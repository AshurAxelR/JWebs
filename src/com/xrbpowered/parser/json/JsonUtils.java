package com.xrbpowered.parser.json;

import java.util.List;
import java.util.Map;

public class JsonUtils {

	private JsonUtils() {
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> object(Object json) {
		return (Map<String, Object>) json;
	}

	@SuppressWarnings("unchecked")
	public static List<Object> array(Object json) {
		List<Object> array = (List<Object>) json;
		return (array == null) ? List.of() : array;
	}

	@SuppressWarnings("unchecked")
	public static List<String> stringArray(Object json) {
		if(json != null && json instanceof String s)
			return List.of(s);
		List<String> array = (List<String>) json;
		return (array == null) ? List.of() : array;
	}

	public static String string(Map<String, Object> json, String key) {
		return (String) json.get(key);
	}

	public static String string(Map<String, Object> json, String key, String def) {
		String s = string(json, key);
		return (s == null) ? def : s;
	}

	public static boolean isBool(Map<String, Object> json, String key) {
		Object v = json.get(key);
		return v == null || v instanceof Boolean;
	}

	public static boolean bool(Map<String, Object> json, String key, boolean def) {
		Boolean b = (Boolean) json.get(key);
		return (b == null) ? def : b;
	}

	public static double number(Map<String, Object> json, String key, double def) {
		Object v = json.get(key);
		if(v == null)
			return def;
		else if(v instanceof Double n)
			return n;
		else if(v instanceof Float n)
			return n;
		else if(v instanceof Long n)
			return n;
		else if(v instanceof Integer n)
			return n;
		else if(v instanceof String s) {
			try {
				return Double.parseDouble(s);
			}
			catch(NumberFormatException e) {
				return def;
			}
		}
		else
			return def;
	}

}
