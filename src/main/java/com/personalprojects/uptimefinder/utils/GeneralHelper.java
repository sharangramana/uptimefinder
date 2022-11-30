package com.personalprojects.uptimefinder.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GeneralHelper {

	public static String trimHttpFromUrl(String url) {
		return url.replaceFirst("^(http[s]?://)", "");
	}

	public static Timestamp getFormattedDate(long timeInMillis) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String timeString = dateFormat.format(timeInMillis);
		Date date = null;
		try {
			date = dateFormat.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Timestamp(date.getTime());
	}

	public static long getTTLSeconds(String ttl) {
		String timeUnit = ttl.substring(ttl.length() - 1);
		ttl = ttl.substring(0, ttl.length() - 1);
		if (timeUnit.equals("d")) {
			return TimeUnit.DAYS.toSeconds(Integer.parseInt(ttl));
		}
		if (timeUnit.equals("h")) {
			return TimeUnit.HOURS.toSeconds(Integer.parseInt(ttl));
		}
		if (timeUnit.equals("s")) {
			return TimeUnit.SECONDS.toSeconds(Integer.parseInt(ttl));
		}
		if (timeUnit.equals("m")) {
			return TimeUnit.MINUTES.toSeconds(Integer.parseInt(ttl));
		}
		return 0L;
	}
}
