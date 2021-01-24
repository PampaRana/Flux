package com.velectico.rbm.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import com.velectico.rbm.expense.views.FileUploadListener;
import com.velectico.rbm.leave.model.LeaveListModel;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GloblalDataRepository {
    private static GloblalDataRepository globalDataService = null;
    private String scheduleId = "";
    private LeaveListModel leaveListModel = null;
    private String teamUserId = "";

    public static GloblalDataRepository getInstance() {
        if (globalDataService == null)
            globalDataService = new GloblalDataRepository();

        return globalDataService;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }


    public String getTeamUserId() {
        return teamUserId;
    }

    public void setTeamUserId(String teamUserId) {
        this.teamUserId = teamUserId;
    }


    private String distribId = "";
    private String taskId = "";
    private String mechId = "";
    private String delalerId = "";

    public static GloblalDataRepository getGlobalDataService() {
        return globalDataService;
    }

    public static void setGlobalDataService(GloblalDataRepository globalDataService) {
        GloblalDataRepository.globalDataService = globalDataService;
    }

    public String getDistribId() {
        return distribId;
    }

    public void setDistribId(String distribId) {
        this.distribId = distribId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMechId() {
        return mechId;
    }

    public void setMechId(String mechId) {
        this.mechId = mechId;
    }

    public String getDelalerId() {
        return delalerId;
    }

    public void setDelalerId(String delalerId) {
        this.delalerId = delalerId;
    }

    public LeaveListModel getLeaveListModel() {
        return leaveListModel;
    }

    public void setLeaveListModel(LeaveListModel leaveListModel) {
        this.leaveListModel = leaveListModel;
    }

public void test(File file, String expId, String userId, String colnName, Context context){
    try {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fileName",file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                .addFormDataPart("expensId", expId)
                .addFormDataPart("userId", userId)
                .addFormDataPart("colName", colnName)
                .build();
        Request request = new Request.Builder()
                .url("https://www.fluxlubricant.in/API/Add_ExpensImage")
                .method("POST", body)

                .build();
     //   Response response = client.newCall(request).execute();

        //Log.d("TAG",response.body().string());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                Log.d("sender", "Broadcasting message");
                Intent intent = new Intent("custom-event-name");
                intent.putExtra("message", response.body().string());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }
        });

    } catch (Exception e) {
        e.printStackTrace();
    }
}


//////////////////Complaint Create//////////////////////////
public void complainTest(File file,
                         String complaintype,
                         String userId,
                         String CR_Batch_no,
                         String CR_Dealer_ID,
                         String CR_Distrib_ID,
                         String CR_Mechanic_ID,
                         String CR_Remarks,
                         String CR_Qty,
                         String prodName,
                         String taskId,
                         Context context){
    try {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("recPhoto",file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(file.getAbsolutePath())))
//        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("recPhoto", file.getName(),
//                        RequestBody.create(MediaType.parse("application/octet-stream"),
//                                file))
                .addFormDataPart("complaintype", complaintype)
                .addFormDataPart("userId", userId)
                .addFormDataPart("CR_Batch_no", CR_Batch_no)
                .addFormDataPart("CR_Dealer_ID", CR_Dealer_ID)
                .addFormDataPart("CR_Distrib_ID", CR_Distrib_ID)
                .addFormDataPart("CR_Mechanic_ID", CR_Mechanic_ID)
                .addFormDataPart("CR_Remarks", CR_Remarks)
                .addFormDataPart("CR_Qty", CR_Qty)
                .addFormDataPart("prodName", prodName)
                .addFormDataPart("taskId", taskId)
                .build();
        Request request = new Request.Builder()
                .url("https://www.fluxlubricant.in/API/Save_Complaint")
                .method("POST", body)

                .build();
        //   Response response = client.newCall(request).execute();

        //Log.d("TAG",response.body().string());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                Log.d("sender", "Broadcasting message");
                Intent intent = new Intent("custom-event-name");
                intent.putExtra("message", response.body().string());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }
        });

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}