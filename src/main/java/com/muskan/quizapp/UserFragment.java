package com.muskan.quizapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muskan.quizapp.api.ApiConnection;
import com.muskan.quizapp.api.OnApiResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment {

    private List<User> userList;
    UserAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users, container, false);


        userList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new UserAdapter(getContext(), userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        getUsers();


        return v;
    }

    void getUsers() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/UsersList";
                    Log.e("Url", Url);
                    new ApiConnection().connect(new OnApiResponseListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {


                                Log.e("RESPONSE", jsonObject.toString());


                                JSONArray jsonArray = jsonObject.getJSONArray("List");
                                if (jsonArray.length() > 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject jsn = jsonArray.getJSONObject(j);

                                        User user = new User(jsn.getInt("USERID"), jsn.getString("FIRSTNAME") + " " +jsn.getString("LASTNAME"),
                                                jsn.getString("EMAILID"));
                                        userList.add(user);
                                    }


                                    adapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(getActivity(), "No User to display", Toast.LENGTH_SHORT).show();
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
