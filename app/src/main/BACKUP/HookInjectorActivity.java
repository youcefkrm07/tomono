package com.aline.studioapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.aline.studioapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HookInjectorActivity extends AppCompatActivity {

    private static final String TAG = "HookInjectorActivity";

    private Button selectApkButton, processApkButton;
    private TextView selectedApkText, statusText;

    private Uri inputApkUri;
    private Uri outputApkUri;
    private String selectedHookFileName;

    private final ActivityResultLauncher<Intent> hookSelectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedHookFileName = result.getData().getStringExtra("selectedHookFile");
                    if (selectedHookFileName != null) {
                        Toast.makeText(this, "Hook file: " + selectedHookFileName, Toast.LENGTH_SHORT).show();
                        processApkButton.setEnabled(true);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> selectApkLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    inputApkUri = result.getData().getData();
                    if (inputApkUri != null) {
                        selectedApkText.setText("Selected APK: " + inputApkUri.getPath());

                        // Launch hook selector
                        Intent intent = new Intent(this, HookSelectionActivity.class);
                        hookSelectionLauncher.launch(intent);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> createApkLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    outputApkUri = result.getData().getData();
                    if (outputApkUri != null) {
                        statusText.setText("Output file selected. Processing...");
                //        startApkProcessing();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook_injector);

        selectApkButton = findViewById(R.id.selectApkButton);
        processApkButton = findViewById(R.id.processApkButton);
        selectedApkText = findViewById(R.id.selectedApkText);
        statusText = findViewById(R.id.statusText);

        processApkButton.setEnabled(false);

        selectApkButton.setOnClickListener(v -> openFilePicker());
        processApkButton.setOnClickListener(v -> createOutputFile());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.android.package-archive");
        selectApkLauncher.launch(intent);
    }

    private void createOutputFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.android.package-archive");
        intent.putExtra(Intent.EXTRA_TITLE, "modified_app.apk");
        createApkLauncher.launch(intent);
    }


}
