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

        Skin skin = new Skin();
        mScannableImageView = (ScannableImageView) super.findViewById(R.id.scannable_view);

        tvUsername = (TextView) findViewById(R.id.username);
        tvName = (TextView) findViewById(R.id.name);
        tvGender = (TextView) findViewById(R.id.gender);
        tvDob = (TextView) findViewById(R.id.dob);

        Intent intent = getIntent();
        Long userNum = intent.getLongExtra("userNum", -1);
        User user = ProfileHelper.getInstance().getUser(userNum);

        if(user != null) {

            mScannableImageView.render(skin, true, R.drawable.default_user);

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
