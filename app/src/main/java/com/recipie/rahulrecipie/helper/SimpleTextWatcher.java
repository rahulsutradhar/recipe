package com.recipie.rahulrecipie.helper;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by developers on 22/10/16.
 */

public class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextChanged(new StringBuilder(s).toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onTextChanged(String text) {

    }
}
