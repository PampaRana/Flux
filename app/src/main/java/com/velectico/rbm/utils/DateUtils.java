package com.velectico.rbm.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.velectico.rbm.beats.model.ScheduleDates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static ArrayList<ScheduleDates> getNextFifteenDays() throws ParseException {
        ArrayList<ScheduleDates> datearr = new ArrayList<ScheduleDates>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
// get starting date
        //    cal.add(Calendar.DAY_OF_YEAR, -6);
        cal.add(Calendar.DAY_OF_YEAR, 0);

// loop adding one day in each iteration
        for (int i = 0; i < 15; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            datearr.add(new ScheduleDates(sdf.format(cal.getTime())));


        }
        return datearr;
    }

    public static ArrayList<ScheduleDates> getLast3Days() throws ParseException {
        ArrayList<ScheduleDates> datearr = new ArrayList<ScheduleDates>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        // get starting date
           cal.add(Calendar.DAY_OF_YEAR, -5);

// loop adding one day in each iteration
        for (int i = 0; i <5 ; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            datearr.add(new ScheduleDates(sdf.format(cal.getTime())));
            Log.d("uuuuuuuuuu",formatDate(sdf.format(cal.getTime())));


        }
        return datearr;
    }

    public static String formatDate(String fecha) {

        String Rfecha = new String();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        //SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE d MMM");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date hoy = new Date();

            Date date = formatter.parse(fecha);


            String pref = "";
            Log.d("hoy long", "" + (hoy.getTime() / (1000 * 60 * 60 * 24)));
            Log.d("date long", "" + (date.getTime() / (1000 * 60 * 60 * 24)));

            int ihoy = (int) (hoy.getTime() / (1000 * 60 * 60 * 24));
            int idate = (int) (date.getTime() / (1000 * 60 * 60 * 24));
            int dif = idate - ihoy;


            if (dif == 0)
                pref = "Today";
            if (dif == 1)
                pref = "Tomorrow";
            if (dif == -1)
                pref = "Yesterday";

            Rfecha = pref + " " + formatter2.format(date);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return Rfecha;
    }

    public String getFormattedDate( long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "dd-MMM-yyyy";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }

    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }
}