package com.example.shareart.utils;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RelativeTime extends Application {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long MONTH_MILLIS = (long) (30.4375 * DAY_MILLIS);
    private static final long YEAR_MILLIS = (long) (365.25 * DAY_MILLIS);

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
        } else if (diff < MONTH_MILLIS) {
            return "Duela " + diff / DAY_MILLIS + " egun";
        } else if (diff < YEAR_MILLIS) {
            return dayMonth(time);
        } else{
            return timeFormatAMPM(time);
        }
    }

    public static String timeFormatAMPM(long timestamp) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date(timestamp));

        return dateString;
    }

    public static String dayMonth(long timestamp) {
        SimpleDateFormat day = new SimpleDateFormat("d");
        SimpleDateFormat month = new SimpleDateFormat("M");
        
        String dayString = day.format(new Date(timestamp));
        String monthNumberString = month.format(new Date(timestamp));
        String monthNameString="";
        switch (monthNumberString){
            case "1":
                monthNameString="Urtarrila";
                break;
            case "2":
                monthNameString="Otsaila";
                break;
            case "3":
                monthNameString="Martxoa";
                break;
            case "4":
                monthNameString="Apirila";
                break;
            case "5":
                monthNameString="Maiatza";
                break;
            case "6":
                monthNameString="Ekaina";
                break;
            case "7":
                monthNameString="Uztaila";
                break;
            case "8":
                monthNameString="Abuztua";
                break;
            case "9":
                monthNameString="Iraila";
                break;
            case "10":
                monthNameString="Urria";
                break;
            case "11":
                monthNameString="Azaroa";
                break;
            case "12":
                monthNameString="Abendua";
                break;
        }
        
        return monthNameString+"ren " + dayString + "a";
    }
}

