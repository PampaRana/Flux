package com.velectico.rbm.loginreg.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class UserDetail(
    @SerializedName("UM_ID")
    val uMID: String?,

    @SerializedName("UM_Name")
    val uMName: String?,

    @SerializedName("UM_Phone")
    val uMPhone: String?,

    @SerializedName("UM_Email")
    val uMEmail: String?,

    @SerializedName("UM_Login_Id")
    val uMLoginId: String?,

    @SerializedName("UM_Password")
    val uMPassword: String?,

    @SerializedName("UM_Is_Password_New")
    val uMIsPasswordNew: String?,

    @SerializedName("UM_Login_Status")
    val uMLoginStatus: String?,

    @SerializedName("UM_Active_Inactive")
    val uMActiveInactive: String?,

    @SerializedName("UM_Role")
    val uMRole: String?,

    @SerializedName("Create_Date")
    val createDate: String?,

    @SerializedName("Created_By")
    val createdBy: String?,

    @SerializedName("Modified_Date")
    val modifiedDate: String?,

    @SerializedName("Modified_By")
    val modifiedBy: String?,

    @SerializedName("SUM_ID")
    val mSMId: String?,

    @SerializedName("SUM_UM_ID")
    val mSMuMId: String?,

    @SerializedName("SUM_Address")
    val mSMAddress: String?,

    @SerializedName("SUM_DOB")
    val mSMDob: String?,

    @SerializedName("SUM_KYC_Doc_Type")
    val mSMKycDocType: String?,

    @SerializedName("SUM_KYC_Doc_No")
    val mSMKycDocNo: String?,

    @SerializedName("SUM_EMER_Phone")
    val mSMPh: String?,

    @SerializedName("SUM_Blood_Grp")
    val mSMBldGrp: String?,

    @SerializedName("SUM_Target")
    val mSMTarget: String?,

    @SerializedName("SUM_Incentive")
    val mSMInsentive: String?,

    @SerializedName("SUM_Manager")
    val mSManger: String?,

    @SerializedName("SUM_Desig")
    val mSMDesig: String?,

    @SerializedName("SUM_Loc_Hier_Lvl")
    val mSMLocHierLvl: String?,

    @SerializedName("SUM_Loc_Hier_Lvl_Val")
    val mSMLocHierLvlVal: String?,

    @SerializedName("SUM_Doc_File1")
    val mSMDocFile1: String?,

    @SerializedName("SUM_Doc_File2")
    val mSMDocFile2: String?,

    @SerializedName("SUM_Monthy_Target_Qty")
    val mSMonthTrgtQty: String?,

    @SerializedName("SUM_Monthy_Target_Amt")
    val mSMonthTrgtAmt: String?,

    @SerializedName("SUM_Quaterly_Target_Qty")
    val mSQtrlyTrgtQty: String?,

    @SerializedName("SUM_Quaterly_Target_Amt")
    val mSQtrlyTrgtAmt: String?,

    @SerializedName("SUM_Half_Yearly_Target_Qty")
    val mSHfYrlyTrgtQty: String?,

    @SerializedName("SUM_Half_Yearly_Target_Amt")
    val mSHfYrlyTrgtAmt: String?,

    @SerializedName("SUM_Yearly_Target_Qty")
    val mSYrlyTrgtQty: String?,

    @SerializedName("SUM_Attendance_Lock")
    val SUM_Attendance_Lock: String?,

    @SerializedName("SUM_Location_Lock")
    val SUM_Location_Lock: String?,

    @SerializedName("SUM_ScreenShot_Lock")
    val SUM_ScreenShot_Lock: String?

) : Parcelable, BaseModel()