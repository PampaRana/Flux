package com.velectico.rbm.beats.model

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import java.util.*
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
class Beats : Parcelable {
    var beatId : String? = null
    var beatName : String? = null
    var areaId : String? = null
    var areaName : String? = null
    var distId : String? = null
    var distName : String? = null
    var dealerId : String? = null
    var dealerName : String? = null
    var startDate : Long? = null
    var endDate : Long? = null
    var salesPersonName : String? = null //assigned To
    var salesPersonId : String? = null
    var beatAssignments : List<BeatAssignments>? =null;

//    @RequiresApi(Build.VERSION_CODES.O)
//    @SuppressLint("NewApi")
    var current = Date()


    public fun getStartDateStr() : String{
        return DateUtility.getStringDateFromTimestamp(startDate!!, DateUtility.dd_MM_yy)
    }

    public fun getendDateStr() : String{
        return DateUtility.getStringDateFromTimestamp(endDate!!, DateUtility.dd_MM_yy)
    }

    public fun getNoOfDays(){
        val dateDiff = DateUtility.getDifferenceDays(d1=Date(startDate as Long),d2=Date(endDate as Long))
        Log.e("test","dateDiff="+dateDiff)
    }

    fun getDummyBeatList() : List<Beats> {
        var cal : Calendar = Calendar.getInstance();
        val beatList = mutableListOf<Beats>()
        for (i in 0..10) {
            var tempBeat = Beats();
            tempBeat.beatId= "beatId"
            tempBeat.beatName = "beatName Test 1"
            tempBeat.areaId = "Test Beat Area ID"
            tempBeat.areaName = "Test Beat Area"
            tempBeat.distId = "Test Beat Area ID"
            tempBeat.distName = "Test Beat Area"
            tempBeat.dealerId = "Test Dealrer ID"
            tempBeat.dealerName = "Test Dealer Name"
            tempBeat.startDate = cal.timeInMillis
            cal.add(Calendar.DAY_OF_MONTH, 10)
            tempBeat.endDate = cal.timeInMillis
            tempBeat.salesPersonName = "Test User"
            //tempBeat.beatAssignments = BeatAssignments().getDummyBeatList(startDate as Long,endDate as Long)
            beatList.add(tempBeat)
        }
        return beatList;
    }


    fun isValidaData(): String? {
       // val inpFormat =  SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
        //val formatted = DateUtils.str()
        var cal : Calendar = Calendar.getInstance();
        val curD = cal.timeInMillis
        println("77777777=======   $startDate,${cal.timeInMillis}")

        var errMsg : String? = null;
        if(startDate==null || endDate==null){
            errMsg = "Please provide start date and endDate";
        }
        if(startDate!=null && endDate!=null){
            if(startDate!! > endDate!!){
                errMsg = "Start date cannot be greater than end date";
            }
        }
//        if(startDate!=null ){
//            if(startDate!! > curD!!){
//                errMsg = "Start date cannot be less than currentDate";
//            }
//        }

        return errMsg;
    }
}

