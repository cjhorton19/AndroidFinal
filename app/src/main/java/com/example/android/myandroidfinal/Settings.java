package com.example.android.myandroidfinal;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by nazlican on 17.12.2017.
 */

public class Settings extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }


    public String getString(String str, String str2) {
        if(str == "work_duration"){
            str2 = "25";
        } else if(str == "break_duration"){
            str2 = "5";
        }
        return str2;
    }
}
