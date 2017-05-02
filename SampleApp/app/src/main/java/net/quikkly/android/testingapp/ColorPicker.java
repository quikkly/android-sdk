package net.quikkly.android.testingapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;

import com.thebluealliance.spectrum.SpectrumDialog;
import com.thebluealliance.spectrum.internal.ColorUtil;


public class ColorPicker extends View {

    public interface ColorChangedListener {
        void onColorChanged(ColorPicker picker, int color);
    }

    private FragmentManager fragmentManager;
    private ColorChangedListener listener;
    private @ColorInt int mColor = 0xff000000;
    private int mOutlineWidth = 2;

    public ColorPicker(Context context) {
        super(context);
        init();
    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        updateDrawables();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager != null) {
                    new SpectrumDialog.Builder(getContext())
                            .setColors(R.array.render_colors)
                            .setDismissOnColorSelected(true)
                            .setOutlineWidth(mOutlineWidth)
                            .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                                @Override
                                public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                    if (positiveResult) {
                                        setColor(color);
                                    }
                                }
                            }).build().show(fragmentManager, "color_dialog");
                }
            }
        });
    }

    private void updateDrawables() {
        setBackground(createBackgroundDrawable());
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setColorChangedListener(ColorChangedListener listener) {
        this.listener = listener;
    }

    public void setOutlineWidth(int width) {
        mOutlineWidth = width;
        updateDrawables();
    }

    public void setColor(int color) {
        if (mColor != color) {
            mColor = color;
            updateDrawables();
            if (listener != null) {
                listener.onColorChanged(this, color);
            }
        }
    }

    public int getColor() {
        return mColor;
    }

    public String getColorHtmlHex() {
        String hex = Integer.toHexString(mColor & 0x00FFFFFF).toUpperCase();
        return '#' + "000000".substring(0, 6 - hex.length()) + hex;
    }

    private Drawable createBackgroundDrawable() {
        GradientDrawable mask = new GradientDrawable();
        mask.setShape(GradientDrawable.OVAL);
        if (mOutlineWidth != 0) {
            mask.setStroke(mOutlineWidth, ColorUtil.isColorDark(mColor) ? Color.WHITE : Color.BLACK);
        }
        mask.setColor(mColor);
        return mask;
    }
}
