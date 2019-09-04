package com.example.socialmedia.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.socialmedia.Handlers.FireBaseUserDataHandler;
import com.example.socialmedia.Objects.UserAccount;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAndSettings extends AppCompatActivity {

    //TODO: Merge the profile pic interface into one interface we can repeatedly call over and over again!
    private Button logOut;
    private FirebaseAuth firebaseAuth;
    private FireBaseUserDataHandler handle;
    private TextView userNameView;
    private CircleImageView circleProfileImageView;

    String sTag = "SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_and_settings);
        circleProfileImageView = findViewById(R.id.profileSettingsImageViewButton);
        circleProfileImageView.setOnClickListener(circleViewOnclickListener);
        firebaseAuth = FirebaseAuth.getInstance();

        userNameView = findViewById(R.id.username_profile_header);
        logOut = findViewById(R.id.logOutButton);
        handle = new FireBaseUserDataHandler();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                Log.d("FIX", firebaseAuth.toString());
                firebaseAuth.signOut();
                // Log.d("FIX", mAuth.getCurrentUser().getEmail());
                Intent goToMainActivity = new Intent(ProfileAndSettings.this, MainActivity.class);
                startActivity(goToMainActivity);
                finish();
            }
        });
        SetUpDataFromDB();

    }

    private void SetUpDataFromDB() {

        if (firebaseAuth.getCurrentUser() != null) {
            handle.GetUserDataByUI(firebaseAuth.getCurrentUser().getUid(), new FireBaseUserDataHandler.DataStatus() {
                @Override
                public void DataIsLoaded(UserAccount foundUser) {
                    if (foundUser != null) {
                        Log.d(sTag, foundUser.getUserName());
                        userNameView.setText(foundUser.getUserName());
                        String url = foundUser.getDPUrl();
                        Log.d(sTag, url);
                        if(url!= null){
                            Picasso.get().load(url).into(circleProfileImageView);
                        }
                    }
                }


                @Override
                public void Error() {
                    Log.e("DATA", "Data was not loaded properly.");
                }
            });
        } else {
            Log.e("FIX", "USER DATA WAS NOT FOUND!!!");
        }

    }

    View.OnClickListener circleViewOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

        }
    };
}