package android.quikkly.net.samples;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import net.quikkly.android.scanning.scanner.CameraStateListener;
import net.quikkly.android.scanning.scanner.ScanResultListener;
import net.quikkly.java.scan.ScanResult;
import net.quikkly.java.scan.Symbol;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ListView contactsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfileHelper.getInstance().init(this);

        setContentView(R.layout.contacts_activity);
        contactsListView = (ListView) findViewById(R.id.contacts_list);

        ArrayList<User> arrayOfUsers = ProfileHelper.getInstance().getUsersList();
        UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);
        contactsListView.setAdapter(adapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                User itemValue = (User) contactsListView.getItemAtPosition(position);
                Intent intent = new Intent(ContactsActivity.this, ProfileActivity.class);
                intent.putExtra("userNum", itemValue.getId());
                startActivity(intent);
            }
        });

        FloatingActionButton addContactButton = (FloatingActionButton) super.findViewById(R.id.add_contact_button);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, SocialScannerActivity.class);
                ContactsActivity.this.startActivity(intent);
            }
        });
    }
}
