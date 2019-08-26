package com.example.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SetUpActivity extends AppCompatActivity {


    //TODO: Make this screen sexier
    //TODO: Change gender and country to selectable.
    //TODO: Add B-Days


    private EditText username, fullname, country, gender;
    private FireBaseUserDataHandler handler;

    //Adapter Vars
    private ArrayList<SpinnerObject> genderList;
    private ArrayList<SpinnerObject> countryList;
    private SpinnerAdapter genderSpinnerAdapter;
    private SpinnerAdapter countrySpinnerAdapter;
    private Spinner genderSpinner;
    private Spinner countrySpinner;
    private FirebaseUser infoFromRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        SetUpFromExistingInfo();

        handler = new FireBaseUserDataHandler();// object for handling DB


        //Spinners
        genderSpinner = findViewById(R.id.gender);
        countrySpinner =findViewById(R.id.country);

        initAdapter();// set up our adapters




    }


    private void initAdapter() {
        genderList = new ArrayList<>();
        genderList.add(new SpinnerObject("Male",R.drawable.male,Constants.GEN_MALE  ));
        genderList.add(new SpinnerObject("Female",R.drawable.female,Constants.GEN_FEMALE ));
        genderList.add(new SpinnerObject("Other",R.drawable.other,Constants.GEN_OTHER ));


        countryList = new ArrayList<>();
        countryList.add(new SpinnerObject("USA",R.drawable.usa,Constants.USA ));
        countryList.add(new SpinnerObject("Canada",R.drawable.canada,Constants.CAN));
        countryList.add(new SpinnerObject("India",R.drawable.india,Constants.INDIA ));

        genderSpinnerAdapter = new SpinnerAdapter(this, genderList);
        countrySpinnerAdapter = new SpinnerAdapter(this, countryList);

        genderSpinner.setAdapter(genderSpinnerAdapter);
        countrySpinner.setAdapter(countrySpinnerAdapter);

    }

    private void SetUpFromExistingInfo() { // Here we will take whatever info we could have gotten from our log in and test it up here.

        infoFromRegister = handler.getUser();

        String name = infoFromRegister.getDisplayName();
        String oldPhotoUrl = infoFromRegister.getPhotoUrl().toString();
        if(name != null){
            fullname.setText(name);
        }
        //TODO: Work with the image

    }

    public void UpdateProfile(View view) {

        username = findViewById(R.id.username); String tUsername = username.getText().toString();
        fullname = findViewById(R.id.fullName); String tFullname = fullname.getText().toString();

        int tGender = ((SpinnerObject) genderSpinner.getSelectedItem()).storeValue;
        int tCountry= ((SpinnerObject) countrySpinner.getSelectedItem()).storeValue;


        if(!isValidUserName(tUsername)){
            //Toast.makeText(SetUpActivity.this,"Invalid Username", Toast.LENGTH_SHORT).show();
        }
        else if(!isValidFullName(tFullname)){
            Toast.makeText(SetUpActivity.this,"Unaccepted Full Name", Toast.LENGTH_SHORT).show();
        }

        else{
            //We got the info we need to update!
            MakeAndSaveProfile(tUsername,tFullname,tCountry,tGender);
        }




    }

    private void MakeAndSaveProfile(String tUsername , String tFullname, int tCountry, int tGender  ) {

        String email = infoFromRegister.getEmail();
        String phone = infoFromRegister.getPhoneNumber();
        String dpUrl = " ";
        //TODO: Work with image

        int country = tCountry;
        int gender = tGender;

        UserAccount account = new UserAccount(tUsername, tFullname, email, phone, null, country, gender, 1);

        if (handler.UpdateUser(account, this)) {
            SendToMainActivity();
        }
    }



    private boolean isValidFullName(String in) {
        if(in == null || in.isEmpty() || in.length() > 50 || ! in.matches("^[ A-Za-z]+$")) {

            return false;
        }
        else{
            return true;
        }

    }

    private Boolean isValidUserName(String in){



        if(in == null || in.isEmpty() || in.length() > 50 || ! in.matches("[a-zA-Z0-9]*")) {

            Toast.makeText(SetUpActivity.this,"Invalid Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (handler.CheckIfUserNameExists(in) ){
            Toast.makeText(SetUpActivity.this,"Username Already Exists", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

    public void EditDp(View view) {

    }

    private void SendToMainActivity(){
        Intent registerIntent = new Intent (SetUpActivity.this, MainActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
        finish(); // clear the current activities visuals
    }
}
