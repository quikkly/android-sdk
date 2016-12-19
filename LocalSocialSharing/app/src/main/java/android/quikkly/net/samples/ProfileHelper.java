package android.quikkly.net.samples;

import android.app.Application;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by fisher on 13/12/2016.
 */
public class ProfileHelper {

    private static ProfileHelper sharedInstance = new ProfileHelper();

    public static ProfileHelper getInstance() {
        return sharedInstance;
    }

    private Context context;

    ArrayList<User> usersList = new ArrayList<>();

    private ProfileHelper() {

    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        loadJSONFromAsset();
    }

    public void loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.context.getAssets().open("users.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONArray jArry = new JSONArray(json);

            for (int i = 0; i < jArry.length(); i++) {
                JSONObject jObj = jArry.getJSONObject(i);
                User user = new User();
                user.setId(jObj.optLong("id",-1));
                user.setDob(jObj.optString("dob","n/a"));
                user.setUsername(jObj.optString("username","n/a"));
                user.setName(jObj.optString("name","n/a"));
                user.setGender(jObj.optString("gender","n/a"));
                user.setProfilePic(jObj.optString("profilePic","n/a"));
                usersList.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getUsersList(){ return usersList; }

    public User getUser(long userId) {
        for (User user : usersList) {
            if(user.getId() == userId) {
                return user;
            }
        }
        return null;
    }
}
