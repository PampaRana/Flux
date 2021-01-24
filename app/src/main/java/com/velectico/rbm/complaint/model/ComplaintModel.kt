package com.velectico.rbm.complaint.model

import android.os.Parcelable
import com.velectico.rbm.beats.model.Beats
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class ComplaintModel : Parcelable {

    var complaintTypeName : String? = null
    var complaintCustType : String? = null
    var complaintCustTypeId : String? = null
    var batchNo : String? = null
    var remarks : String? = null
    var resolution : String? = null
    var resolutionDate : String? = null
    var resolutionStatus : String? = null

    fun getDummyComplaintList() : List<ComplaintModel> {
        var cal : Calendar = Calendar.getInstance();
        val complaintList = mutableListOf<ComplaintModel>()
        for (i in 0..10) {
            var tempBeat = ComplaintModel();
            tempBeat.complaintTypeName= "beatId"
            tempBeat.complaintCustType = "beatName Test 1"
            tempBeat.complaintCustTypeId = "Test Beat Area ID"
            tempBeat.batchNo = "Test Beat Area"
            tempBeat.remarks = "Test Beat Area ID"
            tempBeat.resolution = "Test Beat Area"
            tempBeat.resolutionDate = "Test Dealrer ID"
            tempBeat.resolutionStatus = "Test Dealer Name"

            complaintList.add(tempBeat)
        }
        return complaintList;
    }
}