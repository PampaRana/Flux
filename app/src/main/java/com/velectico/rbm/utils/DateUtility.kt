package com.velectico.rbm.utils

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object DateUtility {
    const val dd_MM_yy = "dd/MM/yy"
    const val dd_month_comma_year = "dd MMMM,yyyy"
    const val dd_suffix_month_comma_year = "dd_th_MMMM,yyyy"
    const val DD_DASH_MM_DASH_YY = "dd-MM-yyyy"
    const val YYYY_DASH_MM_DASH_DD = "yyyy-MM-dd"
    const val month_comma_year = "MMMM,yyyy"
    fun getDateFromYearMonthDay(year: Int, month: Int, day: Int): Date {
        val c = Calendar.getInstance()
        c[year, month, day, 0] = 0
        Log.e("test", "c.getTimeInMillis()=" + c.timeInMillis)
        return Date(c.timeInMillis)
    }

    fun getStringDateFromTimestamp(unixTimeStamp: Long, dateFormat: String): String {
        val dateTime = Date(unixTimeStamp);
        return if (dd_suffix_month_comma_year == dateFormat) {
            getCompleteDateWithDateSuffix(dateTime)
        } else {
            SimpleDateFormat(dateFormat).format(dateTime)
        }
    }

    /*public static String getStringDateFromTimestamp(long timeStampInMilliSeconds,String dateFormat){
        java.util.Date dateTime = new java.util.Date((long)timeStampInMilliSeconds);
        String dateString =  new SimpleDateFormat(dateFormat).format(dateTime);
        return  dateString;
    }*/
    fun getCompleteDateWithDateSuffix(dateTime: Date?): String {
        //dd_suffix_month_comma_year
        val mmmm_yyyy = SimpleDateFormat("MMMM,yyyy").format(dateTime)
        val dd = SimpleDateFormat("dd").format(dateTime)
        val suffix = getDayNumberSuffix(dd.toInt())
        return "$dd$suffix $mmmm_yyyy"
    }

    fun getDayNumberSuffix(day: Int): String {
        return if (day >= 11 && day <= 13) {
            "th"
        } else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    fun dateStrToCalendar(dateStr: String = ""): Calendar {
        val sdf = SimpleDateFormat(dd_MM_yy)
        var date:Date? = null
        val calendar = Calendar.getInstance()
        if(dateStr.isNotEmpty()){
            try {
                date = sdf.parse(dateStr)
                calendar.time = date
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return calendar
    }

    private fun dateToCalendar(date: Date): Calendar? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    fun getDisabledDatesArr(holidaysList: Array<String>): List<Calendar?> {
        val datesList: MutableList<Calendar?> = ArrayList()
        var date: Date? = null
        val sdf = SimpleDateFormat(DD_DASH_MM_DASH_YY)
        for (item in holidaysList) {
            try {
                date = sdf.parse(item)
                datesList.add(dateToCalendar(date as Date))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return datesList
    }

    fun getMonthDateNo(timestamp: Long): Int {
        val date = Date(timestamp)
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[Calendar.DATE]
    }

    fun getDifferenceDays(d1: Date, d2: Date): Int {
        var daysdiff = 0
        val diff = d2.time - d1.time
        val diffDays = diff / (24 * 60 * 60 * 1000) + 1
        daysdiff = diffDays.toInt()
        return daysdiff
    }

    fun showDatePickerDialog(activity : Context, minDate: Long?,maxDate: Long? ,listeners : DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        //DatePickerDialog(activity, listeners, year, month, dayOfMonth).show()

        val dialog = DatePickerDialog(activity, listeners, year, month, dayOfMonth)
        if(minDate!=null){
            dialog.datePicker.minDate = minDate
        }
        if(maxDate!=null){
            dialog.datePicker.maxDate = maxDate
        }
        dialog.show()
    }

    val unixTimeStamp: Long
        get() = System.currentTimeMillis() / 1000L
}