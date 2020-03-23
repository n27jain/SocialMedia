package com.example.socialmedia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmedia.Handlers.FireBaseUserDataHandler;
import com.example.socialmedia.Objects.UserAccount;
import com.example.socialmedia.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class RegisterOrSignInActivity extends AppCompatActivity {

    //TODO: Add FB authentication
    private FireBaseUserDataHandler handler;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 1;
    private EditText email , password;
    private ProgressDialog loadingBar;
    private GoogleApiClient mGoogleSignInClient;
    private static String gTag = "GOOGLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loadingBar = new ProgressDialog(this);

        // Configure Google Sign In This also authenticates it
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { // we need to make sure google IS ACCESSIBLE

                Toast.makeText(RegisterOrSignInActivity.this,"Connecting to GOOGLE failed", Toast.LENGTH_SHORT).show();
            }
        })
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

        handler = new FireBaseUserDataHandler();

    }

    public void checkLog(View view) {// check in the login info
        CreateOrVerifyAccount();
    }
    private void CreateOrVerifyAccount() {

        final String getEmail = email.getText().toString();
        final String getPassword = password.getText().toString();

        if(getEmail.isEmpty()){
            Toast.makeText(this, "Please enter the email.", Toast.LENGTH_SHORT).show();
        }
        else if(getPassword.isEmpty()){
            Toast.makeText(this, "Please enter the password.", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while we verify your account ...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true); // prevent user from touching the outside to close it
            firebaseAuth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        loadingBar.dismiss();
                        UpdateUIAndSendToNextActivity(null);
                        Toast.makeText(RegisterOrSignInActivity.this, "Successful Registration!", Toast.LENGTH_SHORT).show();
                        SendToSetUpActivity();
                        SendToSetUpActivity();// Success log in. Send to Main activity
                    }
                    else { // any error explained
                        String message = Objects.requireNonNull(task.getException()).getMessage();

                        // already registered use
                        if( message!= null && message.equals(getString(R.string.error_user_already_signed_up))) {
                            PerformSignIn(getEmail, getPassword);
                        }
                        else {

                            Log.d("FIX", Objects.requireNonNull(message));
                            Toast.makeText(RegisterOrSignInActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.dismiss();
                    }
                }
            });
        }

    }
    private void PerformSignIn(String inEmail, String inPass) {
        firebaseAuth.signInWithEmailAndPassword(inEmail, inPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIX", "signInWithEmail:success");
                           // SendToSetUpActivity();
                            UpdateUIAndSendToNextActivity(null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("FIX", "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegisterOrSignInActivity.this, "Account Exists. Invalid Password!",
                                    Toast.LENGTH_SHORT).show();
                          //  UpdateUIAndSendToNextActivity(null);
                        }
                    }
                });
    }
    private void SendToSetUpActivity() {
        Intent registerIntent = new Intent (RegisterOrSignInActivity.this, SetUpActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
        finish(); // clear the current activities visuals
    }
    private void SendToMainActivity(){
        Intent registerIntent = new Intent (RegisterOrSignInActivity.this, MainActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
        finish(); // clear the current activities visuals
    }


    //google sign in
        public void GoogleSignIn(View view) {

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient) ;
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                    Log.d(gTag, "Successful Log in");
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                }
                else{ // error found
                    Log.e(gTag, "an error occured. Request code : " + requestCode);
                }
            }
        }

        private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
            if(acct != null) {
                Log.d(gTag, "firebaseAuthWithGoogle:" + acct.getId());
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(gTag, "signInWithCredential:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    UpdateUIAndSendToNextActivity(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.e(gTag, "signInWithCredential:failure", task.getException());
                                    Toast.makeText(RegisterOrSignInActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{
                Log.e(gTag, "Account provided was null" );
            }
        }

    private void UpdateUIAndSendToNextActivity(FirebaseUser user) {
                if(user != null){
                    handler.GetUserDataByUI(user.getUid(), new FireBaseUserDataHandler.DataStatus() {
                        @Override
                        public void DataIsLoaded(UserAccount foundUser) {
                            if(foundUser != null) {
                                int status = foundUser.getStatus();
                                switch (status) {
                                    case 0: // the account is new
                                        SendToSetUpActivity();
                                        finish();
                                        break;
                                    case 1:// user has already signed up!
                                        SendToMainActivity();
                                        finish();
                                        break;
                                    case 2:// user is banned
                                        Toast.makeText(RegisterOrSignInActivity.this, "This account is currently banned", Toast.LENGTH_SHORT).show(); // TODO: create solution for banned account
                                        break;
                                    case 3:// there is an error with this account
                                        Toast.makeText(RegisterOrSignInActivity.this, "There is an error with your account. We apologize for the inconvenience. Please try again later.", Toast.LENGTH_SHORT).show(); // TODO: create crashalytics for this
                                        break;
                                }
                            }
                            else{
                                SendToSetUpActivity();
                                finish();
                            }
                        }

                        @Override
                        public void Error() {
                            Log.e("DATA", "Data was not loaded properly.");
                        }
                    });
                }
                else{
                    Log.e("DATA", "User was not a valid user");
                }
    }

}
