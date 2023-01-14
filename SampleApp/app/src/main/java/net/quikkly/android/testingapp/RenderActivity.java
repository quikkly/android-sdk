package net.quikkly.android.testingapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.quikkly.android.Quikkly;
import net.quikkly.android.render.AndroidSkinBuilder;
import net.quikkly.android.ui.RenderTagView;
import net.quikkly.core.Skin;

import java.io.IOException;
import java.math.BigInteger;


/**
 * An example use of the Quikkly.generateSvg() / Quikkly.generateBitmap() calls and RenderView.
 *
 * In your own app, you probably want to use these calls or views directly on a "share" or "profile" screen.
 */
public class RenderActivity extends AppCompatActivity {

    public static final int PHOTO_REQUEST = 19128;

    private static String[] TEMPLATES = new String[] {
            "template0001style1",  // Replaced with blueprint
    };

    private static String[] IMAGE_FITS = new String[]{
            "Image Fit: Default",
            "Image Fit: Stretch",
            "Image Fit: Meet",
            "Image Fit: Slice",
    };

    private static String[] JOINS = new String[]{
            "Join: Default",
            "Join: None",
            "Join: Horizontal",
            "Join: Vertical",
            "Join: Right",
            "Join: Left",
    };

    private static int[] JOIN_VALUES = new int[]{
            Skin.JOIN_DEFAULT,
            Skin.JOIN_NONE,
            Skin.JOIN_HORIZONTAL,
            Skin.JOIN_VERTICAL,
            Skin.JOIN_DIAGONAL_RIGHT,
            Skin.JOIN_DIAGONAL_LEFT,
    };

    public static void launch(Context context) {
        context.startActivity(new Intent(context, RenderActivity.class));
    }

    RenderTagView renderView;
    EditText dataEdit;
    PopupChoiceView templateChoice;
    PopupChoiceView fitChoice;
    PopupChoiceView joinChoice;
    ColorPicker colorBorder;
    ColorPicker colorBackground;
    ColorPicker colorMask;
    ColorPicker colorOverlay;
    ColorPicker colorData;
    View photoPicker;
    Bitmap photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.render_activity);

        TEMPLATES = Quikkly.getInstance().getTemplateIdentifiers();

        setupViews();
        inputChanged();
    }

    private void setupViews() {
        renderView = (RenderTagView)findViewById(R.id.render_tag);

        renderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataString = dataEdit.getText().toString();
                BigInteger data = TextUtils.isEmpty(dataString) ? BigInteger.ZERO : new BigInteger(dataString);
                data = data.add(BigInteger.ONE);
                dataEdit.setText("" + data);
            }
        });

        templateChoice = (PopupChoiceView)findViewById(R.id.render_template);
        templateChoice.setTitle("Template");
        templateChoice.setChoices(TEMPLATES);
        templateChoice.setSelectedChangedListener(selectedChangedListener);

        fitChoice = (PopupChoiceView)findViewById(R.id.render_fit);
        fitChoice.setTitle("Image Fit");
        fitChoice.setChoices(IMAGE_FITS);
        fitChoice.setSelectedChangedListener(selectedChangedListener);

        joinChoice = (PopupChoiceView)findViewById(R.id.render_join);
        joinChoice.setTitle("Data Dot Join");
        joinChoice.setChoices(JOINS);
        joinChoice.setSelectedChangedListener(selectedChangedListener);

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

        colorBorder = (ColorPicker)findViewById(R.id.render_color_border);
        colorBackground = (ColorPicker)findViewById(R.id.render_color_background);
        colorMask = (ColorPicker)findViewById(R.id.render_color_mask);
        colorOverlay = (ColorPicker)findViewById(R.id.render_color_overlay);
        colorData = (ColorPicker)findViewById(R.id.render_color_data);

        colorBorder.setColor(0xff000000);
        colorBackground.setColor(0xff999999);
        colorMask.setColor(0xffffffff);
        colorOverlay.setColor(0xff000000);
        colorData.setColor(0xff000000);

        for (ColorPicker p : new ColorPicker[] { colorBorder, colorBackground, colorMask, colorOverlay, colorData }) {
            p.setFragmentManager(getSupportFragmentManager());
            p.setColorChangedListener(colorChangedListener);
        }

        photoPicker = findViewById(R.id.render_image);

        photoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PHOTO_REQUEST);
            }
        });
    }

    private void inputChanged() {
        String template = TEMPLATES[templateChoice.getSelected()];

        String dataString = dataEdit.getText().toString();
        BigInteger data = TextUtils.isEmpty(dataString) ? BigInteger.ZERO : new BigInteger(dataString);

        try {
            AndroidSkinBuilder sb = new AndroidSkinBuilder()
                    .setBorderColor(colorBorder.getColorHtmlHex())
                    .setBackgroundColor(colorBackground.getColorHtmlHex())
                    .setOverlayColor(colorOverlay.getColorHtmlHex())
                    .setMaskColor(colorMask.getColorHtmlHex())
                    .setImageFit(fitChoice.getSelected())
                    .setLogoFit(fitChoice.getSelected())
                    //.setAssetsLogo(this, "q.png")
                    .setDotJoin(JOIN_VALUES[joinChoice.getSelected()]);
            if (photo == null) {
                if (!template.startsWith("template0077")) {

                    sb.setAssetsImage(this, "quikkly.png");
                }
            } else {
                sb.setImage(photo);
                //sb.setAssetsImage(this, "quikkly.png");
            }

            if (template.startsWith("template0079")) {
                sb.setDataColors(new String[]{
                        "#ffffff",
                        "#333333",
                });
            } else if (template.startsWith("template0098")) {
                sb.setDataColors(new String[]{
                        "#000000",
                        "#aaaaaa",
                });
            } else if (!template.startsWith("template0080")) {
                sb.setDataColors(new String[]{
                        colorData.getColorHtmlHex(),
                });
            }

            renderView.setAll(template, data, sb.build());
        } catch (IOException e) {
            Log.e(Quikkly.TAG, "Cannot read image file", e);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST) {
            photo = null;
            if (resultCode == RESULT_OK && data != null) {
                Uri contentURI = data.getData();
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                } catch (IOException e) {
                }
            }
            inputChanged();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.render, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export_svg:
                String svg = renderView.getGeneratedSvg();
                if (!TextUtils.isEmpty(svg)) {
                    Export.exportSvg(RenderActivity.this, dataEdit.getText().toString(), svg);
                } else {
                    Toast.makeText(RenderActivity.this, "No generated SVG", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_export_png:
                Bitmap bitmap = renderView.getGeneratedBitmap();
                if (bitmap != null) {
                    Export.exportPng(RenderActivity.this, dataEdit.getText().toString(), bitmap);
                } else {
                    Toast.makeText(RenderActivity.this, "No generated bitmap", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
