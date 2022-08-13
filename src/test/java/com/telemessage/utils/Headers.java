package com.telemessage.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Headers {
    public static String getXDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Calendar.getInstance().getTime());
    }
    public static String getValidXSignature () {

        String xDate = getXDate();
        System.out.println(xDate);
        StringBuilder s = new StringBuilder().append("\"TEST\" , \"POST\", \"").append(xDate).append("\"");
        return DigestUtils.sha512Hex(String.format(s.toString()));
    }
}
