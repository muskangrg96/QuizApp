package com.muskan.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.muskan.quizapp.api.ApiConnection;
import com.muskan.quizapp.api.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText etFirstName, etLastName, etEmail, etMobile, etPassword, etConfirm;
    String FirstName, LastName, Email, Moble, Password, Confirm, Gender;
    RadioButton rdbMale, rdbFemale;

    Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.et_email);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirm = (EditText) findViewById(R.id.et_conf_password);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbFemale);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirstName = etFirstName.getText().toString();
                LastName = etLastName.getText().toString();
                Email = etEmail.getText().toString();
                Moble = etMobile.getText().toString();
                Password = etPassword.getText().toString();
                Confirm = etConfirm.getText().toString();

                if (TextUtils.isEmpty(FirstName)) {
                    etFirstName.setError("");
                    return;
                }
                if (TextUtils.isEmpty(LastName)) {
                    etLastName.setError("");
                    return;
                }
                if (!isValidEmailId(Email)) {
                    etEmail.setError("");
                    return;
                }
                if (TextUtils.isEmpty(Moble)) {
                    etMobile.setError("");
                    return;
                }
                if (TextUtils.isEmpty(Password) && Password.length() < 6) {
                    etPassword.setError("Min. Length 5");
                    return;
                }
                if (TextUtils.isEmpty(Confirm)) {
                    etConfirm.setError("");
                    return;
                }
                if (!TextUtils.equals(Password, Confirm)) {
                    etConfirm.setError("");
                    return;
                }
                if (rdbMale.isChecked()) {
                    Gender = "m";
                } else if (rdbFemale.isChecked()) {
                    Gender = "f";
                }
                userRegister();
            }
        });

    }

    void userRegister() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/Registeration&" + gen() + "&" + FirstName + "&" + LastName + "&" + Gender + "&" + Email
                            + "&" + Password + "&" + Moble;
                    Log.e("Url", Url);
                    new ApiConnection().connect(new OnApiResponseListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {


                                Log.e("RESPONSE", jsonObject.toString());

                                if (TextUtils.equals(jsonObject.getString("Status"), "Ok")) {

                                    Toast.makeText(RegistrationActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();


                                } else {
                                    Toast.makeText(RegistrationActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }

                        @Override
                        public void onFailed(String message) {

                            Toast.makeText(RegistrationActivity.this, "Oops something went wrong..", Toast.LENGTH_SHORT).show();

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

    public int gen() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }
}
