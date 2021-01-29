package com.velectico.rbm.network.manager;

import com.velectico.rbm.beats.model.AssignTaskRequestParams;
import com.velectico.rbm.beats.model.AssignToListRequestParams;
import com.velectico.rbm.beats.model.AssignToListResponse;
import com.velectico.rbm.beats.model.BeatAllOrderListRequestParams;
import com.velectico.rbm.beats.model.BeatDetailListRequestParams;
import com.velectico.rbm.beats.model.BeatDetailListResponse;
import com.velectico.rbm.beats.model.BeatIdentificationParams;
import com.velectico.rbm.beats.model.BeatIdentificationResponse;
import com.velectico.rbm.beats.model.BeatReportDefaultListRequestParams;
import com.velectico.rbm.beats.model.BeatReportListDetailsResponse;
import com.velectico.rbm.beats.model.BeatReportListRequestParams;
import com.velectico.rbm.beats.model.BeatTaskDetailsListResponse;
import com.velectico.rbm.beats.model.BeatTaskDetailsRequestParams;
import com.velectico.rbm.beats.model.BeatWiseTakListResponse;
import com.velectico.rbm.beats.model.CreateBeatReportRequestParams;
import com.velectico.rbm.beats.model.CreateBeatReportResponse;
import com.velectico.rbm.beats.model.CreateBeatScheduleRequestParams;
import com.velectico.rbm.beats.model.CreateOrderDetailsResponse;
import com.velectico.rbm.beats.model.CreateOrderListRequestParams;
import com.velectico.rbm.beats.model.CreateOrderPRParams;
import com.velectico.rbm.beats.model.CreateOrderResponse;
import com.velectico.rbm.beats.model.DealDistMechListRequestParams;
import com.velectico.rbm.beats.model.DealDistMechListResponse;
import com.velectico.rbm.beats.model.DealDistOrderParams;
import com.velectico.rbm.beats.model.DealListRequestParams;
import com.velectico.rbm.beats.model.DealListResponse;
import com.velectico.rbm.beats.model.DealerDetailsRequestParams;
import com.velectico.rbm.beats.model.DealerDetailsResponse;
import com.velectico.rbm.beats.model.DistListRequestParams;
import com.velectico.rbm.beats.model.DistListResponse;
import com.velectico.rbm.beats.model.GetBeatDeatilsRequestParams;
import com.velectico.rbm.beats.model.LocationByLevelListRequestParams;
import com.velectico.rbm.beats.model.LocationByLevelListResponse;
import com.velectico.rbm.beats.model.OrderHistoryDetailsResponse;
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams;
import com.velectico.rbm.beats.model.OrderVSQualityResponse;
import com.velectico.rbm.beats.model.SaveTaskResponse;
import com.velectico.rbm.beats.model.TaskForListRequestParams;
import com.velectico.rbm.beats.model.TaskForListResponse;
import com.velectico.rbm.complaint.model.ComplaintListRequestParams;
import com.velectico.rbm.complaint.model.ComplaintListResponse;
import com.velectico.rbm.dealer.model.AddDealerRequestParams;
import com.velectico.rbm.dealer.model.AddDealerResponse;
import com.velectico.rbm.dealer.model.AddExistDealerResponse;
import com.velectico.rbm.dealer.model.AreaResponse;
import com.velectico.rbm.dealer.model.DealerAreaParams;
import com.velectico.rbm.dealer.model.DealerDistrictParams;
import com.velectico.rbm.dealer.model.DealerListResponse;
import com.velectico.rbm.dealer.model.DealerRequestParams;
import com.velectico.rbm.dealer.model.DistrictResponse;
import com.velectico.rbm.dealer.model.ExistDealerRequestParams;
import com.velectico.rbm.dealer.model.ExistingDealerResponse;
import com.velectico.rbm.dealer.model.MessageResponse;
import com.velectico.rbm.dealer.model.UpdateDealerRequestParams;
import com.velectico.rbm.dealer.model.UpdateExistingDealerParams;
import com.velectico.rbm.dealer.model.UpdateExistingDealerResponse;
import com.velectico.rbm.dealer.model.ValidateMobileRequestParams;
import com.velectico.rbm.expense.model.ApproveRejectParams;
import com.velectico.rbm.expense.model.BidListResponse;
import com.velectico.rbm.expense.model.CreateExpenseResponse;
import com.velectico.rbm.expense.model.ExpenseCreateRequest;
import com.velectico.rbm.expense.model.ExpenseResponse;
import com.velectico.rbm.expense.model.SuccessResponse;
import com.velectico.rbm.expense.model.UpdateExpenseRequest;
import com.velectico.rbm.leave.model.ApplyLeaveRequest;
import com.velectico.rbm.leave.model.ApplyLeaveResponse;
import com.velectico.rbm.leave.model.ApproveRejectLeaveListRequest;
import com.velectico.rbm.leave.model.LeaveListRequest;
import com.velectico.rbm.leave.model.LeaveListResponse;
import com.velectico.rbm.loginreg.model.ResetPasswordRequestParams;
import com.velectico.rbm.loginreg.model.ResetPasswordResponse;
import com.velectico.rbm.loginreg.model.forgotPasswordRequestParams;
import com.velectico.rbm.loginreg.model.forgotPasswordResponse;
import com.velectico.rbm.menuitems.viewmodel.AttendancOutResponse;
import com.velectico.rbm.menuitems.viewmodel.AttendancResponse;
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestOutParams;
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams;
import com.velectico.rbm.order.model.CreditDaysOutStandingResponse;
import com.velectico.rbm.order.model.CreditDaysOutStandingRequestParams;
import com.velectico.rbm.order.model.DistConfirmRequestParams;
import com.velectico.rbm.order.model.DistConfirmResponse;
import com.velectico.rbm.payment.models.DealDistPaymentParams;
import com.velectico.rbm.payment.models.GetInvoiceRequestParams;
import com.velectico.rbm.payment.models.InvoiceRequestParams;
import com.velectico.rbm.payment.models.PaymentCollectionRequestParams;
import com.velectico.rbm.payment.models.PaymentCollectionResponse;
import com.velectico.rbm.payment.models.PaymentHistoryRequestParams;
import com.velectico.rbm.payment.models.InvoicePaidResponse;
import com.velectico.rbm.payment.models.OutStandingResponse;
import com.velectico.rbm.payment.models.PaymentHistoryResponse;
import com.velectico.rbm.redeem.model.GetRedeemDetailsRequestParams;
import com.velectico.rbm.redeem.model.GetRedeemDetailsResponse;
import com.velectico.rbm.redeem.model.ManualQrRequestParams;
import com.velectico.rbm.redeem.model.ReedemRequestParams;
import com.velectico.rbm.redeem.model.ReedemResponse;
import com.velectico.rbm.redeem.model.SendQrRequestParams;
import com.velectico.rbm.redeem.model.SendQrResponse;
import com.velectico.rbm.redeem.model.TotalPointRequestParams;
import com.velectico.rbm.redeem.model.TotalPointResponse;
import com.velectico.rbm.reminder.model.CreateReminderRequestParams;
import com.velectico.rbm.reminder.model.CreateReminderResponse;
import com.velectico.rbm.reminder.model.ReminderListRequestParams;
import com.velectico.rbm.reminder.model.ReminderListResponse;
import com.velectico.rbm.teamlist.model.TeamListDealDistPerformanceResponse;
import com.velectico.rbm.teamlist.model.TeamListPerformanceRequestParams;
import com.velectico.rbm.teamlist.model.TeamListPerformanceResponse;
import com.velectico.rbm.teamlist.model.TeamListRequestParams;
import com.velectico.rbm.teamlist.model.TeamListResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Add_Dealer_Info;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Add_ExpensImage;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Apply_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Approved_Reject_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Beat_Report_By_Date;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Beat_Report_By_Default;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Complaint_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Beat_Report;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Beat_Schedule;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Expense;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Order;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Reminder;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Dealer_Distrib_Task_Worksheet;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Dealer_Dropdown;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Distrib_Dropdown;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.DoAttend;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINTBeat_Task_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Edit_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Existing_Dealer;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Expense_Approve_Reject;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Forgot_Password;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Expense_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_ALL_District;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_All_Area;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_AssignTo_By_TaskFor_And_Location;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Beat_By_Level;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Created_Dealer_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_CreditDays_Cum_OutStanding;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Deal_Dist_Mech_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Dropdown_Details_byName;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Location_By_Level;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_No_Of_incomplete_Beat;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Order_History;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Order_History_Distrib_or_Deal;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_OutStanding_Invoice;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Payment_History;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Payment_History_Distrib_Deal;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Product_List_By_Cat_Seg;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Reedem_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_ReminderList;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Task_For;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Task_dropdown_in_expens;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Team_Performance_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Total_Available_Points;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Invoice_Paid;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Leave_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Team_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Payment_Collection_Confirmation_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Peformance_Dealer_Distrib;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Reedem;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Reset_Password;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Save_AssignTask;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Save_Complaint;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Send_QR_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Send_QR_Details_Manual;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Update_Dealer_Info;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Update_Existing_Dealer;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Update_Expense;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Update_Order_Status_By_Distrib;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Update_OutTime;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Upload_Dummy_Dealer_Image;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Upload_Existing_Dealer_Image;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Validate_PhoneNumber;

public interface ApiInterface {


//    @GET("{affenpinscher}/images")
//    Call<Product> getProductData(@Path("affenpinscher") String breed);



    @POST(ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID)
    Call<BeatWiseTakListResponse> getTaskDetailsByBeat(@Body GetBeatDeatilsRequestParams model);

    @POST(ENDPOINTBeat_Task_Details)
    Call<BeatTaskDetailsListResponse> getScheduleTaskDetailsByBeat(@Body BeatTaskDetailsRequestParams model);

    @POST(Dealer_Distrib_Task_Worksheet)
    Call<DealerDetailsResponse> getDealerDetailsByBeat(@Body DealerDetailsRequestParams model);

    @POST(Get_Order_History)
    Call<OrderHistoryDetailsResponse> getBeatAllOrderHistory(@Body BeatAllOrderListRequestParams model);

    @POST(Get_Product_List_By_Cat_Seg)
    Call<CreateOrderDetailsResponse> getCreateOrderList(@Body CreateOrderListRequestParams model);

    @POST(Beat_Report_By_Date)
    Call<BeatReportListDetailsResponse> getBeatReportList(@Body BeatReportListRequestParams model);

    @POST(Beat_Report_By_Default)
    Call<BeatReportListDetailsResponse> getDefaultBeatReportList(@Body BeatReportDefaultListRequestParams model);

    @POST(Get_Dropdown_Details_byName)
    Call<OrderVSQualityResponse> getOrdervsQualityList(@Body OrderVSQualityRequestParams model);

    @POST(Create_Beat_Report)
    Call<CreateBeatReportResponse> createBeatReport(@Body CreateBeatReportRequestParams model);


    //sec drop down beat create
    @POST(Get_Beat_By_Level)
    Call<BeatDetailListResponse> getBeatDetailList(@Body BeatDetailListRequestParams model);
    //task for secon beat
    @POST(Get_Task_For)
    Call<TaskForListResponse> getTaskForList(@Body TaskForListRequestParams model);

    //3rd drop down
    @POST(Get_Location_By_Level)
    Call<LocationByLevelListResponse> getLocationByLevelList(@Body LocationByLevelListRequestParams model);

    //member list drop down
    @POST(Get_AssignTo_By_TaskFor_And_Location)
    Call<AssignToListResponse> getAssignToList(@Body AssignToListRequestParams model);

    //task
    @POST(Get_Deal_Dist_Mech_List)
    Call<DealDistMechListResponse> getDealDistMechList(@Body DealDistMechListRequestParams model);

    @POST(Create_Beat_Schedule)
    Call<CreateBeatReportResponse> createBeatSchedule(@Body CreateBeatScheduleRequestParams model);

    @POST(Complaint_List)
    Call<ComplaintListResponse> getComplaintList(@Body ComplaintListRequestParams model);

    @POST(Save_AssignTask)
    Call<SaveTaskResponse> assignTask(@Body AssignTaskRequestParams model);

    @POST(Create_Order)
    Call<CreateOrderResponse> createOrder(@Body CreateOrderPRParams model);

    @POST(Get_Team_List)
    Call<TeamListResponse> getTeamList(@Body TeamListRequestParams model);

    @POST(Get_Team_Performance_List)
    Call<TeamListPerformanceResponse> getTeamPerformanceList(@Body TeamListPerformanceRequestParams model);


    @POST(Apply_Leave)
    Call<ApplyLeaveResponse> createLeave(@Body ApplyLeaveRequest model);

    @POST(Edit_Leave)
    Call<ApplyLeaveResponse> updateLeave(@Body ApplyLeaveRequest model);

    @POST(Leave_List)
    Call<LeaveListResponse> getLeaveList(@Body LeaveListRequest model);

    @POST(Approved_Reject_Leave)
    Call<ApplyLeaveResponse> accepeRejectLeave(@Body ApproveRejectLeaveListRequest model);

    @POST(DoAttend)
    Call<AttendancResponse> doAttendance(@Body AttendanceRequestParams model);

    @POST(Get_Task_dropdown_in_expens)
    Call<BidListResponse> getBitList(@Body AttendanceRequestParams model);

    @POST(Create_Expense)
    Call<CreateExpenseResponse> createExpense(@Body ExpenseCreateRequest model);

    @POST(Update_Expense)
    Call<CreateExpenseResponse> updateExpense(@Body UpdateExpenseRequest model);

    @Multipart
    @POST(Add_ExpensImage)
    Call<CreateExpenseResponse> uploadpic(
                        @Part("recPhoto1\"; filename=\"pp.png\" ") RequestBody file,
                        @Part("expensId") RequestBody expensId,
                        @Part("userId") RequestBody userId);

    @POST(Distrib_Dropdown)
    Call<DistListResponse> distDropDownList(@Body DistListRequestParams model);

    @POST(Dealer_Dropdown)
    Call<DealListResponse> dealDropDownList(@Body DealListRequestParams model);

    @POST(Get_ReminderList)
    Call<ReminderListResponse> getReminderList(@Body ReminderListRequestParams model);

    @POST(Create_Reminder)
    Call<CreateReminderResponse> addReminder(@Body CreateReminderRequestParams model);

    @POST(Forgot_Password)
    Call<forgotPasswordResponse> forgotPassword(@Body forgotPasswordRequestParams model);

    @POST(Reset_Password)
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequestParams model);


    @Multipart
    @POST(Save_Complaint)
    Call<SuccessResponse> addComplaint(@Part("userId") RequestBody user_Id,
                                       @Part("taskId") RequestBody task_Id,
                                       @Part("complaintype") RequestBody compln_Type,
                                       @Part("prodName") RequestBody prod_Name,
                                       @Part("CR_Dealer_ID") RequestBody dealer_Id,
                                       @Part("CR_Distrib_ID") RequestBody distributor_Id,
                                       @Part("CR_Mechanic_ID") RequestBody mechanic_Id,
                                       @Part("CR_Qty") RequestBody quantity,
                                       @Part("CR_Batch_no") RequestBody batch_No,
                                       @Part("CR_Remarks") RequestBody reMark,
                                       @Part MultipartBody.Part filePart);//recPhoto

    @Multipart
    @POST(Add_ExpensImage)
    Call<CreateExpenseResponse> uploadpic(
            @Part MultipartBody.Part filePart,@Part MultipartBody.Part filePart1,@Part MultipartBody.Part filePar2,@Part MultipartBody.Part filePar3);

    @Headers({"Content-Type:text/html; charset=UTF-8","Connection:keep-alive"})
    @Multipart
    @POST(Add_ExpensImage)
    Call<CreateExpenseResponse> uploadpic(

            @PartMap Map<String, RequestBody> map);


    @POST(Expense_List)
    Call<ExpenseResponse> getLeaveList(AttendanceRequestParams model);

    @POST(Expense_List)
    Call<ExpenseResponse> getChuttiList(@Body AttendanceRequestParams model);

    @POST(Expense_Approve_Reject)
    Call<SuccessResponse> getApproveReject(@Body ApproveRejectParams model);

    @POST(Send_QR_Details)
    Call<SendQrResponse> sendQrDetails(@Body SendQrRequestParams model);

    @POST(Send_QR_Details_Manual)
    Call<SendQrResponse> sendManualQrDetails(@Body ManualQrRequestParams model);

    @POST(Get_Total_Available_Points)
    Call<TotalPointResponse> getTotalPoint(@Body TotalPointRequestParams model);

    @POST(Reedem)
    Call<ReedemResponse> redeemPoints(@Body ReedemRequestParams model);

    @POST(Get_Reedem_Details)
    Call<GetRedeemDetailsResponse> getRedeemList(@Body GetRedeemDetailsRequestParams model);

    @POST(Get_OutStanding_Invoice)
    Call<OutStandingResponse> getPaymentOutStandingList(@Body PaymentHistoryRequestParams model);

    @POST(Get_Payment_History)
    Call<PaymentHistoryResponse> getPaymentHistoryList(@Body PaymentHistoryRequestParams model);

    @POST(Invoice_Paid)
    Call<InvoicePaidResponse> getInvoiceList(@Body InvoiceRequestParams model);

    @POST(Get_CreditDays_Cum_OutStanding)
    Call<CreditDaysOutStandingResponse> getCreditOutStandingList(@Body CreditDaysOutStandingRequestParams model);

    @POST(Payment_Collection_Confirmation_List)
    Call<PaymentCollectionResponse> getPaymentConfirmationList(@Body PaymentCollectionRequestParams model);

    @POST(Get_No_Of_incomplete_Beat)
    Call<BeatIdentificationResponse> getBeatIdentificationList(@Body BeatIdentificationParams model);

    @POST(Get_Order_History_Distrib_or_Deal)
    Call<OrderHistoryDetailsResponse> getDealDistOrderList(@Body DealDistOrderParams model);

    @POST(Get_Payment_History_Distrib_Deal)
    Call<PaymentHistoryResponse> getDealDistPaymentList(@Body DealDistPaymentParams model);

    @POST(Update_OutTime)
    Call<AttendancOutResponse> attendanceOut(@Body AttendanceRequestOutParams model);

    @POST(Update_Order_Status_By_Distrib)
    Call<DistConfirmResponse> distConfirmOrder(@Body DistConfirmRequestParams model);

    @POST(Add_Dealer_Info)
    Call<AddDealerResponse> addDealerInfo(@Body AddDealerRequestParams model);

    @Multipart
    @POST(Upload_Dummy_Dealer_Image)
    Call<AddDealerResponse> addDealerInfoImage(@Part("DD_ID") RequestBody DD_ID,
                                               @Part("userId") RequestBody userId,
                                               @Part MultipartBody.Part filePart);//fileName
    @Multipart
    @POST(Upload_Existing_Dealer_Image)
    Call<AddExistDealerResponse> addExistDealerInfoImage(@Part("DM_UM_ID") RequestBody DM_UM_ID,
                                                         @Part("userId") RequestBody userId,
                                                         @Part MultipartBody.Part filePart);//fileName

    @POST(Get_Created_Dealer_Details)
    Call<DealerListResponse> dealerListInfo(@Body DealerRequestParams model);


    @POST(Update_Dealer_Info)
    Call<AddDealerResponse> updateDealerListInfo(@Body UpdateDealerRequestParams model);


    @POST(Validate_PhoneNumber)
    Call<MessageResponse> validateMobile(@Body ValidateMobileRequestParams model);

    @POST(Get_All_Area)
    Call<AreaResponse> getArea(@Body DealerAreaParams model);

    @POST(Get_ALL_District)
    Call<DistrictResponse> getDistrict(@Body DealerDistrictParams model);

    @POST(Peformance_Dealer_Distrib)
    Call<TeamListDealDistPerformanceResponse> dealerDistPerformance(@Body TeamListPerformanceRequestParams model);

    @Multipart
    @POST(Add_ExpensImage)
    Call<CreateExpenseResponse> addExpenseImage(@Part("expensId") RequestBody userId,
                                                @Part("userId") RequestBody DD_ID,
                                                @Part("colName") RequestBody colName,
                                                @Part MultipartBody.Part filePart);//recPhoto1, recPhoto2, recPhoto3

    @POST(Existing_Dealer)
    Call<ExistingDealerResponse> existingDealerList(@Body ExistDealerRequestParams model);

    @POST(Update_Existing_Dealer)
    Call<UpdateExistingDealerResponse> updateExistingDealerList(@Body UpdateExistingDealerParams model);
}
