package com.myproject.tileflip;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import org.json.JSONException;

import java.io.IOException;

public class HighestValueMultiplierWatcher implements TextWatcher {

    private Context context;

    public HighestValueMultiplierWatcher(Context context) {
        this.context = context;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // This method is called before the text is changed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // This method is called during the text change
    }

    @Override
    public void afterTextChanged(Editable s) {
        // This method is called after text is changed
        if (!s.toString().isEmpty()) {
            OptionsDataHandler odh;
            try {
                odh = new OptionsDataHandler(context);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
            odh.setHighestValueMultiplier(Integer.parseInt(s.toString()));
            try {
                odh.storeData(context);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
