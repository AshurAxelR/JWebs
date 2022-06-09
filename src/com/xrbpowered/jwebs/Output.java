package com.xrbpowered.jwebs;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Output extends PrintStream {

	public final Charset charset;
	
	private final ByteArrayOutputStream bytes;
	
	public Output(ByteArrayOutputStream out, Charset charset) throws UnsupportedEncodingException {
		super(out, true, charset.name());
		this.charset = charset;
		this.bytes = out;
	}

	public byte[] finish() {
		this.close();
		return bytes.toByteArray();
	}
	
	public String finishAsString() {
		return new String(finish(), charset);
	}
	
	public static Output start(Charset charset) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			return new Output(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			return null; // should never happen
		}
	}
	
	public static Output start() {
		return start(StandardCharsets.UTF_8);
	}
	
}
