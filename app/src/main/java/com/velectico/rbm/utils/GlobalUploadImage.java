package com.velectico.rbm.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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

public class GlobalUploadImage {
    private static GlobalUploadImage globalDataService = null;


    public static GlobalUploadImage getInstance() {
        if (globalDataService == null)
            globalDataService = new GlobalUploadImage();

        return globalDataService;
    }


    public void uploadAddComplaint(File file,
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
                                   Context context) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("recPhoto", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(file.getAbsolutePath())))
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
