package com.aline.studioapp.general;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.aline.studioapp.R;

public class OtherSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);
        setTitle("Other Settings");
    }
}
