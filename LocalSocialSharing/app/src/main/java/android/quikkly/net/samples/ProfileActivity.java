package android.quikkly.net.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.quikkly.android.render.ScannableImageView;
import net.quikkly.android.render.Skin;
import net.quikkly.java.scan.Scanner;

/**
 * Created by fisher on 15/12/2016.
 */

public class ProfileActivity extends AppCompatActivity {

    private ScannableImageView mScannableImageView;
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
            viewBox.setX(65.0D);
            viewBox.setY(65.0D);

            Skin skin = new Skin();
            skin.setCodePattern(codePattern);
            skin.setLayout(codeLayout.ordinal() + 1);
            skin.setVersion(3);
            skin.setBorderColour("#FFFFFF");
            skin.setCodeDotColor("#FFFFFF");
            skin.setBackgroundColor("#000000");
            skin.setFooter("");
            skin.setShowFooter(false);
            skin.setLogoWidth(300.0D);
            skin.setLogoHeight(300.0D);
            skin.setLogoScaleFactor(0.75D);
            skin.setLogoViewBox(viewBox);
            skin.setTextColor("#000000");
            skin.setLogoUri(user.getProfilePic());

            ScannableImageView scannableImageView = (ScannableImageView) super.findViewById(R.id.scannable_view);
            scannableImageView.render(skin, true, R.drawable.default_user); // Provide a default image to show in place of the URI whilst the actual image is genrated from the cache, disk or via download.

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
