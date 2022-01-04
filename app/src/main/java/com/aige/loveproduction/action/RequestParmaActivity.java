package com.aige.loveproduction.action;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public interface RequestParmaActivity {
    default boolean parmaIsEmpty(View view) {
        if(view instanceof EditText) {
            EditText editText = (EditText) view;
            return editText.getText().toString().isEmpty();
        }
        if(view instanceof TextView) {
            TextView textView = (TextView) view;
            return textView.getText().toString().isEmpty();
        }
        throw new IllegalArgumentException("非法参数");
    }
}
