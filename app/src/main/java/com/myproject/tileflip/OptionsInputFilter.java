package com.myproject.tileflip;

import android.text.InputFilter;
import android.text.Spanned;

public class OptionsInputFilter implements InputFilter {

    private int min, max;

    public OptionsInputFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int input = Integer.parseInt(dest.toString() + source.toString());
        if (min <= input && input <= max) {
            return null;
        }
        return "";
    }
}
