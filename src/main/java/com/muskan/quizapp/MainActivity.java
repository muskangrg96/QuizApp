package com.muskan.quizapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.muskan.quizapp.api.ApiConfig;
import com.muskan.quizapp.api.ApiConnection;
import com.muskan.quizapp.api.ApiConnectionAlternative;
import com.muskan.quizapp.api.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLogin();
    }


    void userLogin() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/Login&akash@gmail.com&password";
                    Log.e("Url", Url);
                    new ApiConnection().connect(new OnApiResponseListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            //   try {


                            Log.e("RESPONSE", jsonObject.toString());

/*
                                JSONArray jsonArray = new JSONArray(jsonObject);
                                if (jsonArray.length() > 0) {

                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject jsn = jsonArray.getJSONObject(j);

                                        userId = jsn.getInt("userid");

                                        Intent i = new Intent(LoginActivity.this, OTPActivity.class);
                                        i.putExtra("userid", userId);
                                        startActivity(i);
                                        finish();


                                    }


                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorrect Mobile or Email", Toast.LENGTH_SHORT).show();
                                }*/


                         /*   } catch (JSONException e) {
                                e.printStackTrace();

                            }
*/
                        }

                        @Override
                        public void onFailed(String message) {

                               Toast.makeText(MainActivity.this, "Oops something went wrong..", Toast.LENGTH_SHORT).show();

                        }
                    }, null, Url);

                } catch (Exception e) {


                }
            }
        });
    }


}
