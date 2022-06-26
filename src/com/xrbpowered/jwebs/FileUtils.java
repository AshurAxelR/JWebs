package com.xrbpowered.jwebs;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface FileUtils {

	public static byte[] loadBytes(InputStream s) throws IOException {
		DataInputStream in = new DataInputStream(s);
		byte bytes[] = new byte[in.available()];
		in.readFully(bytes);
		in.close();
		return bytes;
	}

	public static byte[] loadBytes(File f) throws IOException {
		return loadBytes(new FileInputStream(f));
	}
	
	public static String loadString(InputStream s) throws IOException {
		return new String(loadBytes(s), StandardCharsets.UTF_8);
	}

	public static String loadString(File f) throws IOException {
		return new String(loadBytes(f), StandardCharsets.UTF_8);
	}

	public static InputStream getResource(String path) {
		return ClassLoader.getSystemResourceAsStream(path);
	}
	
	public static String loadStringDef(File f, String def) {
		try {
			return loadString(f);
		}
		catch (IOException e) {
			return def;
		}
	}

	public static String fileExt(String path) {
		int i = path.lastIndexOf('.');
		return (i<0) ? "" : path.substring(i+1).toLowerCase();
	}

}
