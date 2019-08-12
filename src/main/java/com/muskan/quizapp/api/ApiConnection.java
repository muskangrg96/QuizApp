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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ApiConnection implements Callback {
    private OnApiResponseListener mOnApiResponseListener;
    private Call mCallOkHttp;

    public void connect(OnApiResponseListener onApiResponseListener, RequestBody requestBody, String url) {
        this.mOnApiResponseListener = onApiResponseListener;
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
                JSONObject jsonObject = new JSONObject(responseStr);
                runSuccessCallbackOnUiThread(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                runFailureCallbackOnUiThread(e.getMessage());
            }
        }
    }

    private void runFailureCallbackOnUiThread(final String value) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mOnApiResponseListener.onFailed(value);
            }
        });
    }

    private void runSuccessCallbackOnUiThread(final JSONObject jsonObject) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mOnApiResponseListener.onSuccess(jsonObject);
            }
        });
    }
}
