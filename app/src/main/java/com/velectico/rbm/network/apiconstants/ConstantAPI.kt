package com.velectico.rbm.network.apiconstants

import java.io.File

/**
 * Created by mymacbookpro on 2020-04-30
 * TODO: Add a class header comment!
 */
object ConstantAPI {
    val prod:String get() = ""

    val test:String get() = "https://www.fluxlubricant.in/"

    val dev:String get() = "https://www.fluxlubricant.in/"
}

///////////////////////////////////////////////////////////////////////

const val CONNECT_TIME_OUT:Long = 1
const val READ_TIME_OUT:Long = 15
const val WRITE_TIME_OUT:Long = 15

const val ERROR_CODE_UNKNOWN_HOST = 604
const val ERROR_CODE_TIME_OUT = 408
const val ERROR_CODE_OTHER = 600

///////////////////////////////////////////////////////////////////////

const val READ_ALL_RESOURCES_DATA = "ReadAllResourcesData"
const val READ_ALL_USERS_DATA = "ReadAllUsersData"
const val USER_LOGIN = "UserLogin"
const val PRODUCT_LIST = "ProductList"
const val EXPENSE_LIST = "ExpenseList"
const val MASTER_DATA_LIST = "MasterDataList"
const val EXPENSE_CREATE_EDIT = "CreateEditExpense"
const val EXPENSE_DELETE = "DeleteExpense"

const val COMPLAINT_CREATE = "Save_Complaint"

const val LEAVE_REASON = "LeaveReason"
const val LEAVE_APPLY = "ApplyLeave"
const val LEAVE_EDIT = "EditLeave"
const val LEAVE_DELETE = "DeleteLeave"
const val LEAVE_LIST = "ListLeave"



const val REQ_HEADER_TYPE_KEY = "Type"
const val REQ_HEADER_TYPE_VAL = "App"
const val REQ_HEADER_DEVICE_ID_KEY = "Userdeviceid"
const val REQ_HEADER_DEVICE_ID_VAL = "569966666"
const val REQ_HEADER_DEVICE_TYPE_KEY = "Devicetype"
const val REQ_HEADER_DEVICE_TYPE_VAL = "Android"
const val USER_LOGIN_MOBILE = "userMobile"
const val USER_LOGIN_PASSWORD = "userPassword"

const val USER_ID = "userId"
const val USER_UM_ID = "userUmId"
const val USER_ROLE = "userRole"
const val USER_NAME = "userName"
const val DROP_DOWN_NAME = "DM_Dropdown_Name"
//create expense params
const val BEAT_TASK_ID = "beatTaskId"
const val EXP_HEAD_ID = "Exp_Head_Id"
const val MIS_EXPENSE_AMOUNT = "misExpenseAmt"
const val APPLIED_ON_DATE = "appliedOnDate"
const val APPLIED_BY_USER_ID = "applieedByUserId"
const val FILE_TO_UPLOAD = "recPhoto"
const val EXPENSE_ID = "expenseId"

//create complaint params

const val COMPLAINT_TYPE = "complaintype"
const val DISTID = "CR_Distrib_ID"
const val DEALERID = "CR_Dealer_ID"
const val MECHANICID ="CR_Mechanic_ID"
const val QTY = "CR_Qty"
const val BATCHNO = "CR_Batch_no"
const val REMARKS = "CR_Remarks"
const val PHOTO = "recPhoto"
const val TASKID = "taskId"
const val PRODNAME = "prodName"

//Leave params
const val LEAVE_ID = "leaveId"
const val LEAVE_FROM = "leaveFromDate"
const val LEAVE_TO = "leaveToDate"
const val LEAVE_REASON_ID = "leaveReasonId"
const val LEAVE_REASON_OTHER = "leaveReasonOther"


const val GOOGLE_BASE_URL = "https://www.google.com"
const val ENDPOINT_GET_RATES = "/pranaycare/server/getRate.php"
const val ENDPOINT_GET_PHOTOS = "photos"
const val ENDPOINT_GET_RESOURCES = "/api/unknown"
const val ENDPOINT_GET_ALL_USERS = "/api/users"

const val APP_API_NAME = "/API/"

const val ENDPOINT_USER_LOGIN   = APP_API_NAME+"Login"
const val ENDPOINT_PRODUCT_LIST = APP_API_NAME+"ProductDetails"
const val ENDPOINT_BEAT_MASTER_DATA   = APP_API_NAME+"GetBeatMasterData"
const val ENDPOINT_EXPENSE_LIST  = APP_API_NAME+"Expense_List"
const val ENDPOINT_EXPENSE_CREATE_EDIT  = APP_API_NAME+"Create_Expense"
const val ENDPOINT_DELETE_EXPENSE  = APP_API_NAME+"Delete_Expense"
const val ENDPOINT_MASTER_DATA_LIST  = APP_API_NAME+"Get_Dropdown_Details_byName"
const val ENDPOINT_COMPLAINT_CREATE  = APP_API_NAME+"Save_Complaint"

//LEAVE ENDPOINTS
const val ENDPOINT_LEAVE_REASON = APP_API_NAME+"Get_Leave_Reason"
const val ENDPOINT_LEAVE_APPLY = APP_API_NAME+"Apply_Leave"
const val ENDPOINT_LEAVE_EDIT = APP_API_NAME+"Edit_Leave"
const val ENDPOINT_LEAVE_DELETE = APP_API_NAME+"Delete_Leave"
const val ENDPOINT_LEAVE_LIST = APP_API_NAME+"Leave_List"

const val GET_ALL_BEAT_DATES = "GET_ALL_BEAT_DATES"

const val ENDPOINT_BEAT_DATES= APP_API_NAME+"Get_Beat_Schedule_Dates_By_userId"

const val GET_TASK_DETAILS_LIST_BY_BEAT_ID = "GET_TASK_DETAILS_LIST_BY_BEAT_ID"

const val ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID = APP_API_NAME+"Get_Beat_Schedule_By_Date"

const val ENDPOINTBeat_Task_Details = APP_API_NAME+"Beat_Task_Details"

const val Dealer_Distrib_Task_Worksheet = APP_API_NAME+"Dealer_Distrib_Task_Worksheet"

const val Get_Order_History = APP_API_NAME+"Get_Order_History"

const val Get_Product_List_By_Cat_Seg = APP_API_NAME+"Get_Product_List_By_Cat_Seg"

const val Beat_Report_By_Date = APP_API_NAME+"Beat_Report_By_Date"

const val Beat_Report_By_Default = APP_API_NAME+"Get_Beat_Report_Default"

const val Get_Dropdown_Details_byName = APP_API_NAME+"Get_Dropdown_Details_byName"

const val Create_Beat_Report = APP_API_NAME+"Beat_Report"
const val Get_Beat_By_Level = APP_API_NAME+"Get_Beat_By_Level"

const val Get_Task_For = APP_API_NAME+"Get_Task_For"

const val Get_Location_By_Level = APP_API_NAME+"Get_Location_By_Level"

const val Get_AssignTo_By_TaskFor_And_Location = APP_API_NAME+"Get_AssignTo_By_TaskFor_And_Location"

const val Get_Deal_Dist_Mech_List = APP_API_NAME+"Get_Deal_Dist_Mech_List"
const val Create_Beat_Schedule = APP_API_NAME+"Create_Beat_Schedule"

const val Complaint_List = APP_API_NAME+"Complaint_List"

const val Save_AssignTask = APP_API_NAME+"Save_AssignTask"

const val Create_Order = APP_API_NAME+"Create_Order"

const val Get_Team_List = APP_API_NAME+"Get_Team_List"

const val Get_Team_Performance_List = APP_API_NAME+"Performance"

const val Apply_Leave = APP_API_NAME+"Apply_Leave"

const val Edit_Leave = APP_API_NAME+"Edit_Leave"

const val Leave_List = APP_API_NAME+"Leave_List"

const val Approved_Reject_Leave = APP_API_NAME+"Approved_Reject_Leave"

const val DoAttend = APP_API_NAME+"DoAttend"

const val Distrib_Dropdown = APP_API_NAME+"Distrib_Dropdown"
const val Get_Task_dropdown_in_expens = APP_API_NAME+"Get_Task_dropdown_in_expens"

const val Create_Expense = APP_API_NAME+"Create_Expense"
const val Add_ExpensImage = APP_API_NAME+"Add_ExpensImage"
const val Expense_List = APP_API_NAME+"Expense_List"
const val Expense_Approve_Reject = APP_API_NAME+"Approve_Expense"
const val Update_Expense= APP_API_NAME+"Update_Expense"

const val Dealer_Dropdown = APP_API_NAME+"Dealer_Dropdown"

const val Get_ReminderList = APP_API_NAME+"Get_ReminderList"

const val Create_Reminder = APP_API_NAME+"Create_Reminder"

const val Forgot_Password = APP_API_NAME+"Forgot_Password"

const val Reset_Password = APP_API_NAME+"Reset_Password"

const val Send_QR_Details = APP_API_NAME+"Send_QR_Details"
const val Send_QR_Details_Manual = APP_API_NAME+"Send_QR_Details_Manual"

const val Get_Total_Available_Points = APP_API_NAME+"Get_Total_Available_Points"
const val Reedem = APP_API_NAME+"Reedem"
const val Get_Reedem_Details = APP_API_NAME+"Get_Reedem_Details"

const val Get_OutStanding_Invoice = APP_API_NAME+"Get_OutStanding_Invoice"
const val Get_Payment_History = APP_API_NAME+"Get_Payment_History"
const val Invoice_Paid = APP_API_NAME+"Invoice_Paid"

const val Get_CreditDays_Cum_OutStanding = APP_API_NAME+"Get_CreditDays_Cum_OutStanding"

const val Save_Complaint = APP_API_NAME+"Save_Complaint"

const val Payment_Collection_Confirmation_List = APP_API_NAME+"Payment_Collection_Confirmation_List"

const val Get_Due_Date_Failed_Report= APP_API_NAME+"Get_Due_Date_Failed_Report"

const val Get_No_Of_incomplete_Beat = APP_API_NAME+"Get_No_Of_incomplete_Beat"

const val Get_Order_History_Distrib_or_Deal = APP_API_NAME+"Get_Order_History_Distrib_or_Deal"

const val Get_Payment_History_Distrib_Deal = APP_API_NAME+"Get_Payment_History_Distrib_Deal"

const val Update_OutTime = APP_API_NAME+"Update_OutTime"

const val Update_Order_Status_By_Distrib = APP_API_NAME+"Update_Order_Status_By_Distrib"

const val Add_Dealer_Info = APP_API_NAME+"Add_Dealer_Info"

const val Upload_Dummy_Dealer_Image = APP_API_NAME+"Upload_Dummy_Dealer_Image"

const val Upload_Existing_Dealer_Image= APP_API_NAME+"Upload_Existing_Dealer_Image"

const val Get_All_Area = APP_API_NAME+"Get_All_Area"

const val Get_ALL_District = APP_API_NAME+"Get_ALL_District"

const val Get_Created_Dealer_Details = APP_API_NAME+"Get_Created_Dealer_Details"

const val Update_Dealer_Info= APP_API_NAME+"Update_Dealer_Info"

const val Validate_PhoneNumber= APP_API_NAME +"Validate_PhoneNumber"

const val Peformance_Dealer_Distrib = APP_API_NAME+"Peformance_Dealer_Distrib"

const val Existing_Dealer= APP_API_NAME+"Existing_Dealer"

const val Update_Existing_Dealer= APP_API_NAME+"Update_Existing_Dealer"


