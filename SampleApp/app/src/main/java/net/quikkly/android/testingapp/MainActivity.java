package net.quikkly.android.testingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.quikkly.android.QuikklyBuilder;
import net.quikkly.android.ui.ScanActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new QuikklyBuilder()
            .setApiKey("1GUVj1rMEgAutuphM39aPw6lzvXV6SpDqttlNsq981uIqNRX8LnDo6H334EgZIsjM7")
            .loadDefaultBlueprintFromLibraryAssets(this)
            .build()
            .setAsDefault();


        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), TestScanActivity.class));
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
