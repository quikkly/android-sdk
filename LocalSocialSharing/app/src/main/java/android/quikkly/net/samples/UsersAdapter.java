package android.quikkly.net.samples;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {

    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);
        }
        TextView tvUsername = (TextView) convertView.findViewById(R.id.username);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        tvName.setText(user.getName());
        tvUsername.setText(user.getUsername());

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.profile_picture);
        if(ivProfileImage != null) {
            Picasso.with(convertView.getContext().getApplicationContext())
                    .load(user.getProfilePic())
                    .into(ivProfileImage);
        }

        return convertView;
    }
}
