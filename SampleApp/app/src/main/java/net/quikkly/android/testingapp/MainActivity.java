package net.quikkly.android.testingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.quikkly.android.Quikkly;
import net.quikkly.android.ui.ScanActivity;
import net.quikkly.android.ui.ScanFragment;
import net.quikkly.core.QuikklyCore;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_OVERLAY = "show_overlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(Quikkly.TAG, "Linking test");
        QuikklyCore.checkLinking();
        Log.e(Quikkly.TAG, "Linking OK");

        if (!Quikkly.isConfigured()) {
            Quikkly.configureInstance(MainActivity.this, 2, 0);
        }

        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.launch(MainActivity.this, 1, true, false, ScanFragment.CAMERA_PREVIEW_FIT_NONE);
            }
        });

        findViewById(R.id.gen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RenderActivity.launch(MainActivity.this);
            }
        });
    }

}
