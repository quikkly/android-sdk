package net.quikkly.android.testingapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import net.quikkly.android.Quikkly;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Export {

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final String PROVIDER_AUTHORITY = "net.quikkly.android.testingapp.fileprovider";


    public static void exportSvg(Activity context, String name, String svgContent) {
        if (svgContent == null) {
            Toast.makeText(context, "No data to export.", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] bytes = svgContent.getBytes(UTF8);
        exportBytes(context, name + ".svg", "image/svg+xml", bytes);
    }

    public static void exportPng(Activity context, String name, Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(context, "No data to export.", Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        exportBytes(context, name + ".png", "image/png", byteStream.toByteArray());
    }

    public static void exportBytes(Activity context, String filename, String mime, byte[] bytes) {
        File saved = saveToCacheFile(context, filename, bytes);
        if (saved != null) {
            Uri contentUri = FileProvider.getUriForFile(context, PROVIDER_AUTHORITY, saved);
            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.setType(mime);
                context.startActivity(Intent.createChooser(shareIntent, "Choose app for export"));
            } else {
                Toast.makeText(context, "Error creating URI for local file.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Error saving content to a local file.", Toast.LENGTH_SHORT).show();
        }
    }

    private static File saveToCacheFile(Context context, String filename, byte[] bytes) {
        File cacheDir = new File(context.getCacheDir(), "shared");
        File file = new File(cacheDir, filename);
        try {
            cacheDir.mkdirs();  // don't forget to make the directory
            FileUtils.writeByteArrayToFile(file, bytes);
            return file;
        } catch (IOException e) {
            Log.e(Quikkly.TAG, "Error writing to file " + file, e);
            return null;
        }
    }
}
