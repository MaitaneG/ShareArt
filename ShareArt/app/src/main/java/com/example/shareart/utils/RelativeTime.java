package com.example.shareart.utils;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RelativeTime extends Application {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Duela gutxi";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "Duela minutu bat";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "Duela " + diff / MINUTE_MILLIS + " minutu";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "Duela ordu bat";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "Duela " + diff / HOUR_MILLIS + " ordu";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Atzo";
        } else {
            return "Duela " + diff / DAY_MILLIS + " egun";
        }
    }

    public static String timeFormatAMPM(long timestamp) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(new Date(timestamp));

        return  dateString;
    }

}

