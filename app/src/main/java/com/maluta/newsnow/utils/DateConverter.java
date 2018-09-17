package com.maluta.newsnow.utils;

import android.content.Context;

import com.maluta.newsnow.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by admin on 9/14/2018.
 */

public class DateConverter {
    private static final String Date_TMDB = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static Date getDate(String date,String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException ex) {
            Timber.d(String.valueOf(R.string.release_date_error), ex);
        }
        return null;
    }

    public static String dateToShortDateFormat(Context context,String date) {
        Locale locale = context.getResources().getConfiguration().locale;

        return DateFormat.getDateInstance(DateFormat.SHORT, locale).format(getDate(date, Date_TMDB));
    }
}
