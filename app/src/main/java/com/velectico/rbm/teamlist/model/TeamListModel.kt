package com.velectico.rbm.teamlist.model

import android.os.Parcelable
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatDate
import kotlinx.android.parcel.Parcelize

@Parcelize
class TeamListModel : Parcelable {

    var teamMemberName : String? = null

    fun getDummyTeamList() : List<TeamListModel> {

        val teamList = mutableListOf<TeamListModel>()
        for (i in 0..10) {
            var teamListTemp = TeamListModel();
            teamListTemp.teamMemberName= "Abhishek"
            teamList.add(teamListTemp)
        }
        return teamList;
    }
}