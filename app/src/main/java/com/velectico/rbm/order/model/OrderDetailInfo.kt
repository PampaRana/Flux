package com.velectico.rbm.order.model

import android.os.Parcelable
import com.velectico.rbm.payment.models.PaymentInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderDetailInfo : Parcelable {
    var info: String? = null
    fun getDummyBeatList(): List<OrderDetailInfo> {
        val beatList = mutableListOf<OrderDetailInfo>()
        for (i in 0..9) {
            var tempBeat = OrderDetailInfo();
            tempBeat.info = "test"
            beatList.add(tempBeat)
        }
        return beatList;
    }
}
