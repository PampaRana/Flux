package com.velectico.rbm.reminder.model

import android.os.Parcelable
import com.velectico.rbm.beats.model.Beats
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
class ReminderList : Parcelable {

    fun getDummyBeatList() : List<ReminderList> {
        val beatList = mutableListOf<ReminderList>()
       for (i in 0..10) {
           var tempBeat = ReminderList();

           //tempBeat.beatAssignments = BeatAssignments().getDummyBeatList(startDate as Long,endDate as Long)
          beatList.add(tempBeat)
        }
        return beatList;
    }
}