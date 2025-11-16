package com.aline.studioapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.aline.studioapp.R;
import com.aline.studioapp.activity.base.BuildPropEditorActivity;

public class HookSelectionActivity extends AppCompatActivity {

    private final String[] hookOptions = {"Build.prop Hook", "Android ID Hook"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook_selection); // Uses the XML layout

        ListView listView = findViewById(R.id.hookListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                hookOptions
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedOption = hookOptions[position];

            switch (selectedOption) {
                case "Build.prop Hook":
                    // Launch editable screen for spoofing Build props
                    startActivity(new Intent(this, BuildPropEditorActivity.class));
                    break;

                case "Android ID Hook":
                    // You can launch another activity or handle it here
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedHookFile", "hook_androidid.dex");
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    break;

                default:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
            }
        });
    }
}
