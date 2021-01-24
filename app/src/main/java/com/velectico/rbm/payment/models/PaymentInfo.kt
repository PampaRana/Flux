package com.velectico.rbm.payment.models

import android.os.Parcelable
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.teamlist.model.TeamListModel
import kotlinx.android.parcel.Parcelize

@Parcelize

class PaymentInfo : Parcelable {
    var info : String? = null
    fun getDummyBeatList() : List<PaymentInfo> {
        val beatList = mutableListOf<PaymentInfo>()
        for (i in 0..2) {
            var tempBeat = PaymentInfo();
            tempBeat.info = "test"
            beatList.add(tempBeat)
        }
        return beatList;
    }



}