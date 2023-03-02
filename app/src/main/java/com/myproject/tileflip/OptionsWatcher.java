package com.myproject.tileflip;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import org.json.JSONException;

import java.io.IOException;

public class OptionsWatcher implements TextWatcher {

    private Context context;
    private String var;

    public OptionsWatcher(Context context, String var) {
        this.context = context;
        this.var = var;
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
            if (var.equals("boardSize")) {
                odh.setBoardSize(Integer.parseInt(s.toString()));
            } else if (var.equals("highestValueMultiplier")) {
                odh.setHighestValueMultiplier(Integer.parseInt(s.toString()));
            }
            try {
                odh.storeData(context);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
