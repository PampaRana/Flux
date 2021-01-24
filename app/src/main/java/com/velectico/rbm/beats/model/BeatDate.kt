package com.velectico.rbm.beats.model

import android.os.Parcelable
import android.util.Log
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.utils.DateUtility
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class BeatDate : Parcelable {
    var beatDate : String? = null

    fun getDummyBeatDateList() : List<BeatDate> {

        val beatDateList = mutableListOf<BeatDate>()
        for (i in 0..10) {
            var beatDateListTemp = BeatDate();
            if(i == 2){
                beatDateListTemp.beatDate= "22 June 2020 , Yesterday"
            }else if(i == 3){
                beatDateListTemp.beatDate= "23 June 2020 , Today"
            }else if(i == 4){
                beatDateListTemp.beatDate= "24 June 2020 , Tomorrow"
            }
            else {
                var temp = (20 + i)
                beatDateListTemp.beatDate= temp.toString()+" June 2020 "
            }
            beatDateList.add(beatDateListTemp)
        }
        return beatDateList;
    }
}