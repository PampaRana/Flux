package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.Beats
import java.util.*

class Expense{
    var expenseId : String?= null;   //val expenseId: String,
    @field:SerializedName("beatTaskId")
    var beatId : String?= null;    // val beatTaskId: String,
    var beatName : String?= null;  //  val beatName: Any,
    var misExpenseAmt : String?= null; // val misExpenseAmt: String,
    var appliedOnDate : String?= null; // val appliedOnDate: String,
    @field:SerializedName("expenseStatus")
    var status : String?= null;      //    val expenseStatus: String,
    @field:SerializedName("approvedByName")
    var approvedBy : String?= null;  //val approvedBy: Any,  val approvedByName: Any,
    @field:SerializedName("Exp_Head_Id")
    var expenseTypeId : String?= null;
    @field:SerializedName("Exp_Head_Name")
    var expenseType : String?= null;
    val ER_Approve_Date: Any?= null;
    val ER_Rejection_Date: Any?= null;
    val ER_Rejector_ID: Any?= null;
    val recPhoto: String?= null;
    val upload_path: String?= null;
    val userId: String?= null;
    val userName: String?= null;

    /*
    *  var expenseTypeId : String?= null;
    var expenseType : String?= null;
    var advanceExpenseAmt : String?= null;
    var kmTravelled : String?= null;
    * */


    fun getEmptyBeatList() : List<Expense> {
        var cal : Calendar = Calendar.getInstance();
        val expenseList = mutableListOf<Expense>()
        return expenseList;
    }
}