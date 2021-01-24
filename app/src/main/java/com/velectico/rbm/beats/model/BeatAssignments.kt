package com.velectico.rbm.beats.model

import com.velectico.rbm.utils.DateUtility
import java.util.*

class BeatAssignments() {
    var assDate: Long? = null
    var assTask: String? = "";
    var isDealer : Boolean = true
    var dealerDistributorId : String? = "";

    constructor(_assDate: Long?, _assTask: String?) : this() {
        assDate = _assDate;
        assTask = _assTask
    }

    public fun getDate(): String {
        if(assDate==null){
            return ""
        }
        return DateUtility.getStringDateFromTimestamp(assDate!!, DateUtility.dd_MM_yy)
    }

    fun getBlankList(): MutableList<BeatAssignments> {
        val assignmentList = mutableListOf<BeatAssignments>()
        assignmentList.add(BeatAssignments())
        return assignmentList
    }

    fun getDummyBeatList(): MutableList<BeatAssignments> {
        //https://beginnersbook.com/2017/10/java-add-days-to-date/
        var cal: Calendar = Calendar.getInstance();

        val assignmentList = mutableListOf<BeatAssignments>()
        for (i in 0..10) {
            assignmentList.add(i, BeatAssignments(cal.timeInMillis, "Testing " + i));
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return assignmentList;
    }

    fun getDummyBeatList(startDate : Long , endDate : Long): MutableList<BeatAssignments> {
        val dateDiff = DateUtility.getDifferenceDays(d1=Date(startDate),d2=Date(endDate))
        val cal = Calendar.getInstance()
        cal.time = Date(startDate as Long)

        val assignmentList = mutableListOf<BeatAssignments>()
        for (i in 0 until dateDiff) {
            assignmentList.add(i, BeatAssignments(cal.timeInMillis, "Testing " + i));
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return assignmentList;
    }
}