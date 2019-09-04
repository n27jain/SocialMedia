package com.example.socialmedia.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.socialmedia.Adapters.SpinnerAdapter;
import com.example.socialmedia.Constants;
import com.example.socialmedia.Handlers.FireBaseUserDataHandler;
import com.example.socialmedia.Objects.SpinnerObject;
import com.example.socialmedia.Objects.UserAccount;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpActivity extends AppCompatActivity {


    //TODO: Make this screen sexier
    //TODO: Change gender and country to selectable.
    //TODO: Add B-Days

    private FirebaseAuth firebaseAuth;
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
    private ProgressDialog loadingBar;
    //CircleView
    CircleImageView editDP;
    final static int GALLERY_CODE_PICK = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        handler = new FireBaseUserDataHandler();// object for handling DB

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullName);

        //Spinners
        genderSpinner = findViewById(R.id.gender);
        countrySpinner =findViewById(R.id.country);
        editDP = findViewById(R.id.editDP);

        initAdapter();// set up our adapters

        SetUpFromExistingInfo();

    }


    private void initAdapter() {
        genderList = new ArrayList<>();
        genderList.add(new SpinnerObject("Male",R.drawable.male, Constants.GEN_MALE  ));
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
        //TODO: Work with the image
        infoFromRegister = firebaseAuth.getCurrentUser();
        handler.GetUserDataByUI(infoFromRegister.getUid(), new FireBaseUserDataHandler.DataStatus() {
            @Override
            public void DataIsLoaded(UserAccount foundUser) {
                if(foundUser != null) {
                    if (foundUser.getDPUrl() != null) {
                        Picasso.get().load(foundUser.getDPUrl()).into(editDP);
                    }
                }
            }

            @Override
            public void Error() {

            }
        });
        String name = infoFromRegister.getDisplayName();
        //String dpUrl = infoFromRegister.getPhotoUrl().toString();
        if(name != null){
            fullname.setText(name);
        }


    }

    public void UpdateProfile(View view) {

        String tUsername = username.getText().toString();
        String tFullname = fullname.getText().toString();

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

    private void MakeAndSaveProfile(final String tUsername , final String tFullname, final int tCountry, final int tGender  ) {

        infoFromRegister = firebaseAuth.getCurrentUser();
        handler.GetUserDataByUI(infoFromRegister.getUid(), new FireBaseUserDataHandler.DataStatus() {
            @Override
            public void DataIsLoaded(UserAccount foundUser) {
                String email = infoFromRegister.getEmail();
                if(email != null)
                foundUser.setEmail(email);
                foundUser.setPhone(infoFromRegister.getPhoneNumber());
                foundUser.setCountry(tCountry);
                foundUser.setGender(tGender);
                foundUser.setUserName(tUsername);
                foundUser.setFullName(tFullname);
                foundUser.setStatus(Constants.STAT_REGISTERED);
                foundUser.setUserID(infoFromRegister.getUid());
                handler.UpdateUser(foundUser, SetUpActivity.this, new FireBaseUserDataHandler.UpdateStatus() {
                    @Override
                    public void DataIsLoaded() {
                        SendToMainActivity();
                    }

                    @Override
                    public void Error() {
                        //TODO: Should we have a toast message here?
                    }
                });

            }

            @Override
            public void Error() {

            }
        });
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
        //TODO: DP edit
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            loadingBar.setTitle("Loading Image...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true); // prevent user from touching the outside to close it
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                editDP.setImageURI(resultUri);
                String [] directoryFolder = {"Users"};
                handler.SaveProfileImage(firebaseAuth.getCurrentUser().getUid(), resultUri, new FireBaseUserDataHandler.UpdateStatus() {
                    @Override
                    public void DataIsLoaded() {
                        handler.GetUserDataByUI(firebaseAuth.getCurrentUser().getUid(), new FireBaseUserDataHandler.DataStatus() {
                            @Override
                            public void DataIsLoaded(UserAccount foundUser) {

                            }

                            @Override
                            public void Error() {

                            }
                        });
                    }

                    @Override
                    public void Error() {
                        // the error is deep within and expressed using a logcat
                    }
                });
                loadingBar.dismiss();
            }
        }
    }

    private void SendToMainActivity(){
        Intent registerIntent = new Intent (SetUpActivity.this, MainActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
        finish(); // clear the current activities visuals
    }
}
