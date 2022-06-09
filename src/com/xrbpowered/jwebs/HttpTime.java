package com.xrbpowered.jwebs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public abstract class HttpTime {

	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	static {
		dateFormat.setTimeZone(GMT);
	}

	public static String format(long time) {
		synchronized (dateFormat) {
			return dateFormat.format(new Date(time));
		}
	}
	
	public static String now() {
		Calendar cal = Calendar.getInstance(GMT);
		synchronized (dateFormat) {
			return dateFormat.format(cal.getTime());
		}
	}
	
	public static long parse(String s) throws ParseException {
		return dateFormat.parse(s).getTime();
	}
}
