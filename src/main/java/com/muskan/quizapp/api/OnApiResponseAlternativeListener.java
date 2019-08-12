package com.muskan.quizapp.api;

public interface OnApiResponseAlternativeListener {
    void onSuccess(String jsonStr);
    void onFailed(String message);
}
