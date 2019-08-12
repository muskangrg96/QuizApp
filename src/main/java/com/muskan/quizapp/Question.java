package com.muskan.quizapp;

public class Question {

    int questionId;
    String question;

    public Question(int questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
