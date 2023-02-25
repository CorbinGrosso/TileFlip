package com.myproject.tileflip;

import android.text.InputFilter;
import android.text.Spanned;

public class BoardSizeInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int min = 3;
        int max = 5;
        int input = Integer.parseInt(dest.toString() + source.toString());
        if (min <= input && input <= max) {
            return null;
        }
        return "";
    }
}
