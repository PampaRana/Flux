package com.velectico.rbm.network

import com.velectico.rbm.beats.model.BeatDateListResponse
import com.velectico.rbm.beats.model.BeatWiseTakListResponse
import com.velectico.rbm.beats.model.GetBeatDeatilsRequestParams
import com.velectico.rbm.expense.model.ExpenseCreateResponse
import com.velectico.rbm.expense.model.ExpenseDeleteRequest
import com.velectico.rbm.expense.model.ExpenseDeleteResponse
import com.velectico.rbm.expense.model.ExpenseListResponse
import com.velectico.rbm.leave.model.*
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.masterdata.model.MasterDataResponse
import com.velectico.rbm.menuitems.model.ResourceListResponse
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.manager.Retry
import com.velectico.rbm.network.manager.RetryCallAdapterFactory
import com.velectico.rbm.products.models.ProductListResponse
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


/**
 * Created by mymacbookpro on 2020-04-30
 * TODO: Add a class header comment!
 */
interface WebService {

    @Retry
    @GET(ENDPOINT_GET_RATES)
    fun getRateAPI(@Query("rateID") rateId:String): Any
    @Retry
    @GET(ENDPOINT_GET_PHOTOS)
    fun getRateAPI(): Any
    @Retry
    @GET(ENDPOINT_GET_RESOURCES)
    fun getAllResources(): Call<ResourceListResponse>
    @Retry
    @GET(ENDPOINT_GET_ALL_USERS)
    fun getAllUsers(@QueryMap params:Map<String, String>): Call<ResourceListResponse>
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_USER_LOGIN)
    fun getUserLoginData(@FieldMap fieldMap: Map<String, String>,
                         @HeaderMap header:Map<String, String>): Call<LoginResponse>
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_PRODUCT_LIST)
    fun getProductList(@FieldMap fieldMap: Map<String, String>,
                         @HeaderMap header:Map<String, String>): Call<ProductListResponse>

    //Expense related urls starts here
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_BEAT_MASTER_DATA)
    fun getBeatMasterDataList(@FieldMap fieldMap: Map<String, String>,
                       @HeaderMap header:Map<String, String>): Call<ProductListResponse>
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_EXPENSE_LIST)
    fun getExpenseList(@FieldMap fieldMap: Map<String, String>,
                              @HeaderMap header:Map<String, String>): Call<ExpenseListResponse>
    /*@Multipart
    @FormUrlEncoded
    @POST(ENDPOINT_EXPENSE_CREATE_EDIT)
    fun createEditExpense(@FieldMap fieldMap: Map<String, String>,
                          @Part filePart : MultipartBody.Part?,
                          @HeaderMap header:Map<String, String>): Call<ExpenseCreateResponse>*/

    //https://stackoverflow.com/questions/52553210/retrofit-2-multipart-image-upload-with-data
    @Retry
    @POST(ENDPOINT_EXPENSE_CREATE_EDIT)
    fun createEditExpense(
        @HeaderMap header:Map<String, String>,
        @Body reqBody: RequestBody?
    ): Call<ExpenseCreateResponse>


    @Retry
    @POST(ENDPOINT_DELETE_EXPENSE)
    fun deleteExpense(
        @HeaderMap header:Map<String, String>,
        @Body reqBody: RequestBody?
    ): Call<ExpenseDeleteResponse>

    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_MASTER_DATA_LIST)
    fun getMasterData(@FieldMap fieldMap: Map<String, String>,
                      @HeaderMap header:Map<String, String>): Call<MasterDataResponse>

    //Expense related urls ends here

    //Leave related url starts here

    @Retry
    @POST(ENDPOINT_LEAVE_REASON)
    fun getLeaveReason(@HeaderMap header:Map<String, String>): Call<LeaveReasonResponse>

    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_LEAVE_APPLY)
    fun applyLeave(@FieldMap fieldMap: Map<String, String>,
                       @HeaderMap header:Map<String, String>): Call<ApplyLeaveResponse>
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_LEAVE_EDIT)
    fun editLeave(@FieldMap fieldMap: Map<String, String>,
                   @HeaderMap header:Map<String, String>): Call<ApplyLeaveResponse>
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_LEAVE_DELETE)
    fun deleteLeave(@FieldMap fieldMap: Map<String, String>,
                  @HeaderMap header:Map<String, String>): Call<DeleteLeaveResponse>
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_LEAVE_LIST)
    fun getLeaveList(@FieldMap fieldMap: Map<String, String>,
                    @HeaderMap header:Map<String, String>): Call<LeaveListResponse>

    //Leave related url ends here

    //Beat Related urls here
    @Retry
    @FormUrlEncoded
    @POST(ENDPOINT_BEAT_DATES)
    fun getDatesForBeat(@FieldMap fieldMap: Map<String, String>,
                     @HeaderMap header:Map<String, String>): Call<BeatDateListResponse>
    @Retry
    @POST(ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID)
    fun getTaskDetailsByBeat(@Body model: GetBeatDeatilsRequestParams): Call<BeatWiseTakListResponse>
    //Beat Related urls END here

    companion object{
        fun getService(): WebService {
            return Retrofit.Builder()
                .baseUrl(BaseUrlInfo.baseUrl)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RetryCallAdapterFactory.create())
                .build()
                .create(WebService::class.java)
        }

        fun getHttpClient() : OkHttpClient{
            return  OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build()
        }

        fun getTestService(): WebService {
            return Retrofit.Builder()
                .baseUrl(ConstantAPI.test)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebService::class.java)
        }
    }
}