package com.example.socialmedia;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileAndSettings extends AppCompatActivity {

    private Button logOut;
    private FirebaseAuth mAuth;
    private FireBaseUserDataHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_and_settings);
        logOut = findViewById(R.id.logOutButton);
        handler = new FireBaseUserDataHandler(); // TODO: Potential bug? We might need the constructor?

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                Log.d("FIX", mAuth.toString());
                mAuth.signOut();
               // Log.d("FIX", mAuth.getCurrentUser().getEmail());
                Intent goToMainActivity = new Intent(ProfileAndSettings.this, MainActivity.class);
                startActivity(goToMainActivity);
                finish();
            }
        });

    }
    private void SetUpDataFromDB() {
        //TODO: By default we should be on the feed section in this app.
        // create a nice loading screen while it updates the users feed
        //UserAccount account = handle.GetUserDataByUI(handle.getUI());

    }





}
