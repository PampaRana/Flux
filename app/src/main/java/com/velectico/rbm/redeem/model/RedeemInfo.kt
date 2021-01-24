package com.velectico.rbm.redeem.model

import android.os.Parcelable
import com.velectico.rbm.reminder.model.ReminderList
import kotlinx.android.parcel.Parcelize

@Parcelize

class RedeemInfo : Parcelable {

    fun getDummyBeatList(): List<RedeemInfo> {
        val beatList = mutableListOf<RedeemInfo>()
        for (i in 0..10) {
            var tempBeat = RedeemInfo();

            //tempBeat.beatAssignments = BeatAssignments().getDummyBeatList(startDate as Long,endDate as Long)
            beatList.add(tempBeat)
        }
        return beatList;
    }
}