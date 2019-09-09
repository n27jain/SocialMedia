package com.example.socialmedia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmedia.Fragments.FeedFragment;
import com.example.socialmedia.Fragments.Friends.FriendsFragment;
import com.example.socialmedia.Handlers.FireBaseUserDataHandler;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    //TODO: On back button pressed return to the last fragment

    private FirebaseAuth mAuth;
    private FireBaseUserDataHandler handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

    }

    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent (MainActivity.this, RegisterOrSignInActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
        finish(); // clear the current activities visuals


    }

    public void goToSettings(View v){ // shortcut for onClick
        Intent intent = new Intent(MainActivity.this, ProfileAndSettings.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            SetUpDataFromDB();
        }

        if (currentUser == null) {
            SendUserToRegisterActivity();
        }
    }

    private void SetUpDataFromDB() {
        //TODO: By default we should be on the feed section in this app.
        // create a nice loading screen while it updates the users feed
        //UserAccount account = handle.GetUserDataByUI(handle.getUID());

    }


    public void insertFeedView(View view) {
        Log.d("FIX", "Tried to insert feed");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container,new FeedFragment() ).commit();
    }
    public void insertFriendsView(View view) {
        Log.d("FIX", "Tried to insert Friend screen");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container,new FriendsFragment() ).commit();
    }
}
