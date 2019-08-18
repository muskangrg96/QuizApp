package com.muskan.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muskan.quizapp.api.ApiConnection;
import com.muskan.quizapp.api.OnApiResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    TextView OptionA, OptionB, OptionC, OptionD, Question, Explanation;
    String[] list_OptionA, list_OptionB, list_OptionC, list_OptionD, list_Question, list_Answer, list_Explaination, list_Answered;
    Button btnNext, btnPrevious, btnSubmit;
    ProgressBar progressBar;
    int QuestionIndex = 0, TotalQuestion = 0, CorrectAnswer = 0, WrongAnswer = 0;
    CardView CardA, CardB, CardC, CardD;
    int QuizId = 0;
    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        questionList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        OptionA = (TextView) findViewById(R.id.tvOptA);
        OptionB = (TextView) findViewById(R.id.tvOptB);
        OptionC = (TextView) findViewById(R.id.tvOptC);
        OptionD = (TextView) findViewById(R.id.tvOptD);
        Explanation = (TextView) findViewById(R.id.tvExplanation);

        Question = (TextView) findViewById(R.id.question);


        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        OptionA.setOnClickListener(this);
        OptionB.setOnClickListener(this);
        OptionC.setOnClickListener(this);
        OptionD.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        CardA = (CardView) findViewById(R.id.optionA);
        CardB = (CardView) findViewById(R.id.optionB);
        CardC = (CardView) findViewById(R.id.optionC);
        CardD = (CardView) findViewById(R.id.optionD);
        list_Answer = new String[20];
        list_Answered = new String[20];
        getQuestions();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnNext:
                try {


                    QuestionIndex++;
                    SetQuestion(QuestionIndex);
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
                break;
            case R.id.btnPrevious:
                if (QuestionIndex > 0) {
                    btnNext.setEnabled(true);
                    QuestionIndex--;
                    SetQuestion(QuestionIndex);
                    CheckAnswer(list_Answered[QuestionIndex]);
                    OptionA.setEnabled(false);
                    OptionB.setEnabled(false);
                    OptionC.setEnabled(false);
                    OptionD.setEnabled(false);
                    btnNext.setVisibility(Button.VISIBLE);
                    btnSubmit.setVisibility(Button.INVISIBLE);

                }

                break;
            case R.id.btnSubmit:
                Intent i = new Intent(QuizActivity.this, MainActivity.class);
                i.putExtra("total_question", TotalQuestion);
                i.putExtra("correct_answer", CorrectAnswer);
                i.putExtra("wrong_answer", WrongAnswer);
                i.putExtra("quiz_id", QuizId);
                startActivity(i);
                finish();
                break;

            case R.id.tvOptA:
                CheckAnswer("A");
                if (TextUtils.equals(list_Answer[QuestionIndex], "A")) {
                    CorrectAnswer++;
                } else {
                    WrongAnswer++;
                }


                break;
            case R.id.tvOptB:
                CheckAnswer("B");
                if (TextUtils.equals(list_Answer[QuestionIndex], "B")) {
                    CorrectAnswer++;
                } else {
                    WrongAnswer++;
                }


                break;
            case R.id.tvOptC:
                CheckAnswer("C");
                if (TextUtils.equals(list_Answer[QuestionIndex], "C")) {
                    CorrectAnswer++;
                } else {
                    WrongAnswer++;
                }
                break;
            case R.id.tvOptD:
                CheckAnswer("D");
                if (TextUtils.equals(list_Answer[QuestionIndex], "D")) {
                    CorrectAnswer++;
                } else {
                    WrongAnswer++;
                }
                break;
        }

    }

    void CheckAnswer(String SelectedOption) {
        try {
            if (TotalQuestion == (QuestionIndex + 1)) {
                btnNext.setEnabled(false);
                btnNext.setVisibility(Button.INVISIBLE);
                btnSubmit.setVisibility(Button.VISIBLE);
            } else {
                btnNext.setEnabled(true);
                btnSubmit.setVisibility(Button.INVISIBLE);
            }

            list_Answered[QuestionIndex] = SelectedOption;

            if (!TextUtils.equals(SelectedOption, list_Answer[QuestionIndex])) {
                if (TextUtils.equals(SelectedOption, "A")) {
                    CardA.setCardBackgroundColor(Color.parseColor("#ff3822"));
                } else if (TextUtils.equals(SelectedOption, "B")) {
                    CardB.setCardBackgroundColor(Color.parseColor("#ff3822"));
                } else if (TextUtils.equals(SelectedOption, "C")) {
                    CardC.setCardBackgroundColor(Color.parseColor("#ff3822"));
                } else if (TextUtils.equals(SelectedOption, "D")) {
                    CardD.setCardBackgroundColor(Color.parseColor("#ff3822"));
                }

            }
            if (TextUtils.equals(list_Answer[QuestionIndex], "A")) {
                CardA.setCardBackgroundColor(Color.parseColor("#92d36e"));
            } else if (TextUtils.equals(list_Answer[QuestionIndex], "B")) {
                CardB.setCardBackgroundColor(Color.parseColor("#92d36e"));
            } else if (TextUtils.equals(list_Answer[QuestionIndex], "C")) {
                CardC.setCardBackgroundColor(Color.parseColor("#92d36e"));
            } else if (TextUtils.equals(list_Answer[QuestionIndex], "D")) {
                CardD.setCardBackgroundColor(Color.parseColor("#92d36e"));
            }
            Explanation.setVisibility(TextView.VISIBLE);
            OptionA.setEnabled(false);
            OptionB.setEnabled(false);
            OptionC.setEnabled(false);
            OptionD.setEnabled(false);
        } catch (NullPointerException n) {
            Log.d("NullPointerException", n.getMessage());
        }

    }

    void SetQuestion(int QuestionIndex) {

        try {

            if (QuestionIndex == 0) {
                btnPrevious.setVisibility(Button.INVISIBLE);
            } else {
                btnPrevious.setVisibility(Button.VISIBLE);
            }

            CardA.setCardBackgroundColor(Color.parseColor("#ffffff"));
            CardB.setCardBackgroundColor(Color.parseColor("#ffffff"));
            CardC.setCardBackgroundColor(Color.parseColor("#ffffff"));
            CardD.setCardBackgroundColor(Color.parseColor("#ffffff"));

            Question question = questionList.get(QuestionIndex);


            Question.setText(question.getQuestion());
            getOptions(question.getQuestionId());

            //  Explanation.setText(list_Explaination[QuestionIndex]);
            //  Explanation.setVisibility(TextView.INVISIBLE);

            // getSupportActionBar().setTitle("Quiz             " + String.valueOf(QuestionIndex + 1) + "/" + String.valueOf(TotalQuestion));
            btnNext.setEnabled(false);
        } catch (NullPointerException n) {
            Log.d("NullPointerException", n.getMessage());
        }


    }

    void getQuestions() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/questionList";
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

                                        Question question = new Question(jsn.getInt("question_id"), jsn.getString("ques_text")
                                        );
                                        questionList.add(question);
                                    }


                                    SetQuestion(QuestionIndex);

                                } else {
                                    Toast.makeText(QuizActivity.this, "No Quiz to display", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }

                        @Override
                        public void onFailed(String message) {
                            // avLoadingIndicatorView.hide();
                            Toast.makeText(QuizActivity.this, "Oops something went wrong..", Toast.LENGTH_SHORT).show();

                        }
                    }, null, Url);

                } catch (Exception e) {


                }
            }
        });
    }

    void getOptions(final int QuestionId) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url = "http://10.0.2.2:8080/QuizApp/main/mobileApp/QuestionOptions&" + QuestionId;
                    Log.e("Url", Url);
                    new ApiConnection().connect(new OnApiResponseListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {


                                Log.e("RESPONSE", jsonObject.toString());


                                JSONArray jsonArray = jsonObject.getJSONArray("Option_List");
                                if (jsonArray.length() > 0) {
                                    for (int j = 0; j < 1; j++) {

                                        JSONObject jsn = jsonArray.getJSONObject(j);

                                        String options = jsn.getString("options_text");

                                        // splitting String by comma, it will return array
                                        String[] array = options.split(",");


                                        OptionA.setText(array[0]);
                                        OptionB.setText(array[1]);
                                        OptionC.setText(array[2]);
                                        OptionD.setText(array[3]);

                                        if (TextUtils.equals(array[0], jsn.getString("right_ans"))) {
                                            list_Answer[QuestionId] = "A";
                                        } else if (TextUtils.equals(array[1], jsn.getString("right_ans"))) {
                                            list_Answer[QuestionId] = "B";
                                        } else if (TextUtils.equals(array[2], jsn.getString("right_ans"))) {
                                            list_Answer[QuestionId] = "C";
                                        } else if (TextUtils.equals(array[3], jsn.getString("right_ans"))) {
                                            list_Answer[QuestionId] = "D";
                                        }


                                    }




                                } else {
                                    Toast.makeText(QuizActivity.this, "No Quiz to display", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }

                        @Override
                        public void onFailed(String message) {
                            // avLoadingIndicatorView.hide();
                            Toast.makeText(QuizActivity.this, "Oops something went wrong..", Toast.LENGTH_SHORT).show();

                        }
                    }, null, Url);

                } catch (Exception e) {


                }
            }
        });
    }
}
