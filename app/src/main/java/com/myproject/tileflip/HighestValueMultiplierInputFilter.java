package com.myproject.tileflip;

import android.text.InputFilter;
import android.text.Spanned;

public class HighestValueMultiplierInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int min = 2;
        int max = 7;
        int input = Integer.parseInt(dest.toString() + source.toString());
        if (min <= input && input <= max) {
            return null;
        }
        return "";
    }
}
