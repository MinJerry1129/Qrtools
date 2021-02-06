package com.kessi.photopipcollagemaker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {
    private static final String DATE_TIME_FORMAT_GMT = "yyyy-MM-dd HH:mm:ss 'GMT'";
    public static final String DATE_TIME_FORMAT_STANDARDIZED_UTC = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_STANDARDIZED_UTC = "yyyy-MM-dd";
    private static final String DATE_FORMAT_MM_DD_YYYY = "MM/dd/yyyy";
    private static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";

    public static String getCurrentDateTimeGMT() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DATE_TIME_FORMAT_GMT, Locale.US);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat(DATE_TIME_FORMAT_STANDARDIZED_UTC).format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_FORMAT_STANDARDIZED_UTC).format(new Date());
    }
}
