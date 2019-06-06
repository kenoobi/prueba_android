package com.testconcept.android_test.PreferenceItems;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference{

    Context context;

    public Preference(Context context) {
        this.context = context;
    }

    public int reloadPreferences(){
        SharedPreferences preferences = context.getSharedPreferences
                ("Preferences",context.MODE_PRIVATE);
        int answer = Integer.parseInt(preferences.getString("State", "1"));
        return answer;
    }

    public void savePreferences(){
        SharedPreferences preferences= context.getSharedPreferences
                ("Preferences",context.MODE_PRIVATE);

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("State","1");
        editor.commit();
    }
}
