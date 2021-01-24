package com.velectico.rbm.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateList {
    /**
     * @param args
     */
   /* public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<Date> dates = new ArrayList<Date>();

        String str_date ="27/08/2010";
        String end_date ="02/09/2010";

        DateFormat formatter ;

        formatter = new DateFormat("dd/MM/yyyy");
        Date startDate = null;
        try {
            startDate = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = (Date)formatter.parse(end_date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
        long curTime = startDate.getTime();
        while (curTime <= endTime) {
            dates.add(new Date(curTime));
            curTime += interval;
        }
        for(int i=0;i<dates.size();i++){
            Date lDate =(Date)dates.get(i);
            String ds = (String) formatter.format(lDate);
            System.out.println(" Date is ..." + ds);
        }

    }*/

}
