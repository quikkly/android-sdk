package net.quikkly.android.testingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import net.quikkly.android.Quikkly;
import net.quikkly.android.ui.ScanActivity;
import net.quikkly.core.ScanResult;
import net.quikkly.core.Tag;

/**
 * Created by fisher on 02/05/2017.
 */

public class TestScanActivity extends ScanActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Quikkly.getDefaultInstance(); // quick ping to check it's all setup correctly
    }

    @Override
    public void onScanResult(@Nullable ScanResult result) {

        if(result != null && result.isEmpty() == false) {
            Log.d("quikkly", "found " + result.tags.length + " codes");
            for(Tag scannedCode : result.tags) {
                Log.d("quikkly", "Code:" + scannedCode.getData());
            }
        }
    }


}
