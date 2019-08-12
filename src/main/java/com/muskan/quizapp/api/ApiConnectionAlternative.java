package com.muskan.quizapp.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class ApiConnectionAlternative implements Callback {
    private OnApiResponseAlternativeListener mOnApiResponseAlternativeListener;
    private Call mCallOkHttp;

    public void connect(OnApiResponseAlternativeListener onApiResponseAlternativeListener, RequestBody requestBody, String url) {
        this.mOnApiResponseAlternativeListener = onApiResponseAlternativeListener;
        Request request;
        if (requestBody != null) {
            request = new Request.Builder()
                    .post(requestBody)
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .build();
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        mCallOkHttp = okHttpClient.newCall(request);
        mCallOkHttp.enqueue(this);
    }

    @Override
    public void onFailure(Request request, IOException e) {
        runFailureCallbackOnUiThread(e.getMessage());
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            runFailureCallbackOnUiThread(response.toString());
        } else {
            String responseStr = response.body().string();
            Log.e("Response", responseStr);
            try {
                runSuccessCallbackOnUiThread(responseStr);
            } catch (Exception e) {
                e.printStackTrace();
                runFailureCallbackOnUiThread(e.getMessage());
            }
        }
    }

    private void runFailureCallbackOnUiThread(final String value) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mOnApiResponseAlternativeListener.onFailed(value);
            }
        });
    }

    private void runSuccessCallbackOnUiThread(final String jsonVal) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mOnApiResponseAlternativeListener.onSuccess(jsonVal);
            }
        });
    }

    /*private void test(String jsonStr){
        JSONArray jsonArray=new JSONArray(jsonStr);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            Stirng value=jsonObject
        }
    }*/
}
