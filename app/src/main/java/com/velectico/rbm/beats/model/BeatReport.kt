package com.velectico.rbm.beats.model

import android.os.Parcelable
import com.velectico.rbm.teamlist.model.TeamPerformanceModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class BeatReport : Parcelable {

    var teamMemberName : String? = null

    fun getDummyBeatComList() : List<BeatReport> {

        val teamList = mutableListOf<BeatReport>()
        for (i in 0..10) {
            var teamListTemp = BeatReport();
            teamListTemp.teamMemberName= "Abhishek"
            teamList.add(teamListTemp)
        }
        return teamList;
    }


}