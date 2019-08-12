package com.muskan.quizapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.muskan.quizapp.api.ApiConnection;
import com.muskan.quizapp.api.OnApiResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    SharedPreferences sharedPrefs;
    int userId = 0;
    String Email;
    TextView tvName, tvGender, tvEmail, tvMobile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvGender = (TextView) v.findViewById(R.id.tvGender);
        tvEmail = (TextView) v.findViewById(R.id.tvEmail);
        tvMobile = (TextView) v.findViewById(R.id.tvPhone);

        sharedPrefs = getActivity().getSharedPreferences(Const.SHAREDPREFERENCE, MODE_PRIVATE);
        userId = sharedPrefs.getInt(Const.UserId, 0);
        Email = sharedPrefs.getString(Const.Email, "");
        getProfile();
        return v;
    }

    void getProfile() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/user_Profile&" + Email;
                    Log.e("Url", Url);
                    new ApiConnection().connect(new OnApiResponseListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {


                                Log.e("RESPONSE", jsonObject.toString());


                                JSONArray jsonArray = jsonObject.getJSONArray("UserInfo");
                                if (jsonArray.length() > 0) {
                                    for (int j = 0; j < 1; j++) {

                                        JSONObject jsn = jsonArray.getJSONObject(j);
                                        tvEmail.setText("Email : " + jsn.getString("EMAILID"));
                                        tvGender.setText("Gender : " + jsn.getString("GENDER"));
                                        tvMobile.setText("Phone : " + jsn.getString("PHONENUMBER"));
                                        tvName.setText(jsn.getString("FIRSTNAME") + " " + jsn.getString("LASTNAME"));


                                    }


                                } else {
                                    Toast.makeText(getActivity(), "No Category to display", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }

                        @Override
                        public void onFailed(String message) {
                            // avLoadingIndicatorView.hide();
                            Toast.makeText(getActivity(), "Oops something went wrong..", Toast.LENGTH_SHORT).show();

                        }
                    }, null, Url);

                } catch (Exception e) {


                }
            }
        });
    }
}
