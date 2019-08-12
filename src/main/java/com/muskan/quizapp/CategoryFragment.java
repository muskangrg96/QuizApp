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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.muskan.quizapp.api.ApiConnection;
import com.muskan.quizapp.api.OnApiResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CategoryFragment extends Fragment {

    private List<Category> categoryList;
    CategoryAdapter adapter;

    SharedPreferences sharedPrefs;
    int userId = 0;
    Button btnStart, btnNext;
    TextView tvTerms;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        sharedPrefs = getActivity().getSharedPreferences(Const.SHAREDPREFERENCE, MODE_PRIVATE);
        userId = sharedPrefs.getInt(Const.UserId, 0);

        categoryList = new ArrayList<>();
        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new CategoryAdapter(getContext(), categoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        tvTerms = (TextView) v.findViewById(R.id.tvTerms);
        btnStart = (Button) v.findViewById(R.id.btnStart);
        btnNext = (Button) v.findViewById(R.id.btnNext);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setVisibility(View.INVISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                tvTerms.setVisibility(View.VISIBLE);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.setVisibility(View.INVISIBLE);
                tvTerms.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        getCategories();


        return v;
    }

    void getCategories() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/categoryList";
                    Log.e("Url", Url);
                    new ApiConnection().connect(new OnApiResponseListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {


                                Log.e("RESPONSE", jsonObject.toString());


                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                if (jsonArray.length() > 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject jsn = jsonArray.getJSONObject(j);

                                        Category category = new Category(jsn.getInt("cat_id"), jsn.getString("cat_name"));
                                        categoryList.add(category);
                                    }


                                    adapter.notifyDataSetChanged();

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
