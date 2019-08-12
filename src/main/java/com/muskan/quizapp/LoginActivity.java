package com.muskan.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.muskan.quizapp.api.ApiConnection;
import com.muskan.quizapp.api.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    AppCompatEditText etEmail, etPassword;
    AppCompatButton btnLogin, btnRegister;
    String Email, Password;
    SharedPreferences sharedPrefs;
    int userId = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefs = getSharedPreferences(Const.SHAREDPREFERENCE, MODE_PRIVATE);
        userId = sharedPrefs.getInt(Const.UserId, 0);

        if (userId > 0) {

            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();

        }


        etEmail = (AppCompatEditText) findViewById(R.id.et_email_login);
        etPassword = (AppCompatEditText) findViewById(R.id.et_password_login);
        btnLogin = (AppCompatButton) findViewById(R.id.btn_sigin);
        btnRegister = (AppCompatButton) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();

                if (!isValidEmailId(Email)) {
                    etEmail.setError("Invalid");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    etPassword.setError("Invalid");
                    return;
                }
                userLogin();

            }
        });


    }


    void userLogin() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/Login&" + Email + "&" + Password;
                    Log.e("Url", Url);
                    new ApiConnection().connect(new OnApiResponseListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {


                                Log.e("RESPONSE", jsonObject.toString());

                                if (TextUtils.equals(jsonObject.getString("Status"), "OK")) {
                                    userId = jsonObject.getInt("UserId");

                                    sharedPrefs = getSharedPreferences(Const.SHAREDPREFERENCE, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPrefs.edit();
                                    editor.putInt(Const.UserId, userId);
                                    editor.putString(Const.Email, Email);
                                    editor.apply();


                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();


                                } else {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }

                        @Override
                        public void onFailed(String message) {

                            Toast.makeText(LoginActivity.this, "Oops something went wrong..", Toast.LENGTH_SHORT).show();

                        }
                    }, null, Url);

                } catch (Exception e) {


                }
            }
        });
    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
