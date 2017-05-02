package net.quikkly.android.testingapp;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import net.quikkly.android.Quikkly;
import net.quikkly.android.ui.RenderTagView;
import net.quikkly.core.Skin;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


/**
 * An example use of the Quikkly.generateSvg() / Quikkly.generateBitmap() calls and RenderView.
 *
 * In your own app, you probably want to use these calls or views directly on a "share" or "profile" screen.
 */
public class RenderActivity extends AppCompatActivity {

    // TODO: this should be loaded from the blueprint
    private static String[] TEMPLATES = new String[] {
        "template0001style1",
        "template0001style2",
        "template0001style3",
        "template0002style1",
        "template0002style2",
        "template0002style3",
        "template0002style4",
        "template0002style5",
        "template0002style6",
        "template0002style7",
        "template0002style8",
        "template0002style9",
        "template0002style10",
        "template0002style11",
        "template0002style12",
        "template0002style13",
        "template0002style14",
        "template0002style15",
        "template0002style16",
        "template0002style19",
        "template0004style1",
        "template0004style2",
        "template0004style3",
        "template0004style4",
        "template0004style5",
        "template0004style6",
        "template0004style7",
        "template0004style8",
        "template0005style1",
        "template0005style2",
        "template0006style1",
        "template0010style1",
        "template0012style1",
        "template9999style1",
    };

    private static String[] IMAGE_FITS = new String[]{
            "Default",
            "Stretch",
            "Meet",
            "Slice",
    };

    public static void launch(Context context) {
        context.startActivity(new Intent(context, RenderActivity.class));
    }

    RenderTagView renderView;
    EditText dataEdit;
    PopupChoiceView templateChoice;
    PopupChoiceView fitChoice;
    ColorPicker borderColor;
    ColorPicker backgroundColor;
    ColorPicker maskColor;
    ColorPicker overlayColor;
    ColorPicker dataColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.render_activity);

        Quikkly.getDefaultInstance();  // Check that Quikkly is set up

        setupViews();
        inputChanged();
    }

    private void setupViews() {
        renderView = (RenderTagView)findViewById(R.id.render_tag);

        templateChoice = (PopupChoiceView)findViewById(R.id.render_template);
        templateChoice.setTitle("Template");
        templateChoice.setChoices(TEMPLATES);
        templateChoice.setSelectedChangedListener(selectedChangedListener);

        fitChoice = (PopupChoiceView)findViewById(R.id.render_fit);
        fitChoice.setTitle("Image Fit");
        fitChoice.setChoices(IMAGE_FITS);
        fitChoice.setSelectedChangedListener(selectedChangedListener);

        dataEdit = (EditText)findViewById(R.id.render_data);
        dataEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputChanged();
            }
        });

        borderColor = (ColorPicker)findViewById(R.id.render_border_color);
        backgroundColor = (ColorPicker)findViewById(R.id.render_background_color);
        maskColor = (ColorPicker)findViewById(R.id.render_mask_color);
        overlayColor = (ColorPicker)findViewById(R.id.render_overlay_color);
        dataColor = (ColorPicker)findViewById(R.id.render_data_color);
        borderColor.setColor(0xff000000);
        backgroundColor.setColor(0xff2196F3);
        maskColor.setColor(0xff2196F3);
        overlayColor.setColor(0xffffffff);
        dataColor.setColor(0xffffffff);

        for (ColorPicker p : new ColorPicker[] { borderColor, backgroundColor, maskColor, overlayColor, dataColor }) {
            p.setFragmentManager(getSupportFragmentManager());
            p.setColorChangedListener(colorChangedListener);
        }

    }

    private void inputChanged() {
        String template = "template0001style2";//TEMPLATES[templateChoice.getSelected()];

        String dataString = dataEdit.getText().toString();
        BigInteger data = TextUtils.isEmpty(dataString) ? BigInteger.ZERO : new BigInteger(dataString);

        Skin skin = new Skin();
        skin.backgroundColor = backgroundColor.getColorHtmlHex();
        skin.borderColor = borderColor.getColorHtmlHex();
        skin.maskColor = maskColor.getColorHtmlHex();
        skin.overlayColor = overlayColor.getColorHtmlHex();
        skin.dataColor = dataColor.getColorHtmlHex();
        try {
            skin.imageUrl = readAndBase64EncodeFromAssets(this, "grid.jpg");
            skin.logoUrl = readAndBase64EncodeFromAssets(this, "quikkly.png");
        } catch (IOException e) {
            Log.e(Quikkly.TAG, "Cannot read image files from assets", e);
        }
        // TODO: consider separate image fit inputs.
        skin.imageFit = fitChoice.getSelected();
        skin.logoFit = fitChoice.getSelected();

        renderView.setAll(template, data, skin);
    }

    private PopupChoiceView.SelectedChangedListener selectedChangedListener = new PopupChoiceView.SelectedChangedListener() {
        @Override
        public void onSelectedChanged(int position) {
            inputChanged();
        }
    };

    private ColorPicker.ColorChangedListener colorChangedListener = new ColorPicker.ColorChangedListener() {
        @Override
        public void onColorChanged(ColorPicker picker, int color) {
            inputChanged();
        }
    };

    private String readAndBase64EncodeFromAssets(Context context, String assetsFile) throws IOException {
        String mime;
        if (assetsFile.toLowerCase().endsWith(".png"))
            mime = "image/png";
        else if (assetsFile.toLowerCase().endsWith(".jpg") || assetsFile.toLowerCase().endsWith(".jpeg"))
            mime = "image/jpeg";
        else
            throw new IllegalArgumentException("Unknown image file extension, cannot determine mime type: " + assetsFile);

        AssetManager am = context.getAssets();
        InputStream stream = am.open(assetsFile, AssetManager.ACCESS_STREAMING);
        try {
            byte[] bytes = IOUtils.toByteArray(stream);
            String dataUri = "data:" + mime + ";base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
            return dataUri;
        } finally {
            stream.close();
        }
    }
}
