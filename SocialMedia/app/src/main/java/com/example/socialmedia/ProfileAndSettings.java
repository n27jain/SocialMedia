package com.example.socialmedia;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileAndSettings extends AppCompatActivity {

    private Button logOut;
    private FirebaseAuth firebaseAuth;
    private FireBaseUserDataHandler handle;
    private TextView userNameView;
    String sTag = "SETTINGS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_and_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        userNameView = findViewById(R.id.username_profile_header);
        logOut = findViewById(R.id.logOutButton);
        handle = new FireBaseUserDataHandler(); // TODO: Potential bug? We might need the constructor?

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

        if(firebaseAuth.getCurrentUser() != null){
            handle.GetUserDataByUI(firebaseAuth.getCurrentUser().getUid(), new FireBaseUserDataHandler.DataStatus() {
                @Override
                public void DataIsLoaded(UserAccount foundUser) {
                    if(foundUser != null) {
                        Log.d(sTag, foundUser.getUserName());
                        userNameView.setText(foundUser.getUserName());
                        TextView displayAll = findViewById(R.id.displayAllInfo);
                        displayAll.setText(foundUser.getUserID()+ ";  "+foundUser.getFullName()+ ";  "+foundUser.getEmail()+ ";  "+foundUser.getGender());
                    }
                }


                @Override
                public void Error() {
                    Log.e("DATA", "Data was not loaded properly.");
                }
            });
        }
        else{
            Log.e("FIX", "USER DATA WAS NOT FOUND!!!");
        }

    }





}
