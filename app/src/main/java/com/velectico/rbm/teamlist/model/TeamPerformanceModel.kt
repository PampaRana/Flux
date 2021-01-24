package com.velectico.rbm.teamlist.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TeamPerformanceModel : Parcelable {



    var teamMemberName : String? = null

    fun getDummyTeamPerformaceList() : List<TeamPerformanceModel> {

        val teamList = mutableListOf<TeamPerformanceModel>()
        for (i in 0..10) {
            var teamListTemp = TeamPerformanceModel();
            teamListTemp.teamMemberName= "Abhishek"
            teamList.add(teamListTemp)
        }
        return teamList;
    }

}