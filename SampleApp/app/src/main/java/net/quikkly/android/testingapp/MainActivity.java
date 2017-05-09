package net.quikkly.android.testingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.quikkly.android.QuikklyBuilder;
import net.quikkly.android.ui.RenderTagView;
import net.quikkly.android.ui.ScanActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_OVERLAY = "show_overlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new QuikklyBuilder()
            .setApiKey("YOUR API KEY HERE")
            .loadDefaultBlueprintFromLibraryAssets(this)
            .build()
            .setAsDefault();

        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TestScanActivity.class);
                intent.putExtra(EXTRA_SHOW_OVERLAY, true);
                startActivity(intent);
            }
        });

        findViewById(R.id.gen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RenderActivity.class));
            }
        });
    }

}
