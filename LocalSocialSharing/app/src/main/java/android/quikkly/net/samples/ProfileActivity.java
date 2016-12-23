package android.quikkly.net.samples;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.quikkly.android.render.ScannableImageView;
import net.quikkly.android.render.Skin;
import net.quikkly.java.scan.Scanner;

/**
 * Created by Gregg Fisher on 15/12/2016.
 */

public class ProfileActivity extends AppCompatActivity {

    private ScannableImageView mScannableImageView;
    private Skin mSkin;
    private TextView tvUsername;
    private TextView tvName;
    private TextView tvGender;
    private TextView tvDob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        setTitle(R.string.profile_title);

        tvUsername = (TextView) findViewById(R.id.username);
        tvName = (TextView) findViewById(R.id.name);
        tvGender = (TextView) findViewById(R.id.gender);
        tvDob = (TextView) findViewById(R.id.dob);

        Intent intent = getIntent();
        Long userNum = intent.getLongExtra("userNum", -1);

        User user = ProfileHelper.getInstance().getUser(userNum);
        if(user != null) {

            Scanner mScanner = new Scanner();
            Scanner.CodeLayout codeLayout = Scanner.CodeLayout.Horizontal;
            String codePattern = mScanner.generateV3CodePattern(codeLayout, userNum);

            Skin.ViewBox viewBox = new Skin.ViewBox();
            viewBox.setWidth(358.0D);
            viewBox.setHeight(358.0D);
            viewBox.setX(80.0D);
            viewBox.setY(65.0D);

            mSkin = new Skin();
            mSkin.setCodePattern(codePattern);
            mSkin.setLayout(codeLayout.ordinal() + 1);
            mSkin.setVersion(3);
            mSkin.setFooter("");
            mSkin.setShowFooter(false);
            mSkin.setBorderColour("#FFFFFF");
            mSkin.setCodeDotColor("#FFFFFF");
            mSkin.setBackgroundColor("#5a47ec");
            mSkin.setLogoWidth(200.0D);
            mSkin.setLogoHeight(200.0D);
            mSkin.setLogoScaleFactor(1D);
            mSkin.setLogoViewBox(viewBox);
            mSkin.setTextColor("#000000");
            mSkin.setLogoUri(user.getProfilePic());

            mScannableImageView = (ScannableImageView) super.findViewById(R.id.scannable_view);
            mScannableImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        // noinspection deprecation
                        mScannableImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    else {
                        mScannableImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    mScannableImageView.render(mSkin, true, R.drawable.default_user); // Provide a default image to show in place of the URI whilst the actual image is genrated from the cache, disk or via download.
                }
            });

            if(tvUsername != null)
                tvUsername.setText(user.getUsername());

            if(tvName != null)
                tvName.setText(user.getName());

            if(tvGender != null)
                tvGender.setText(user.getGender());

            if(tvDob != null)
                tvDob.setText(user.getDob());
        }

    }

}
