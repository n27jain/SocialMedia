package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email , password;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loadingBar = new ProgressDialog(this);
    }

    public void checkLog(View view) {// check in the login info
        CreateOrVerifyAccount();
    }

    private void CreateOrVerifyAccount() {

        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();

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
            loadingBar.setCanceledOnTouchOutside(true); // prevent user from touching the outside to close it.

            mAuth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                    }
                    else { // any error explained
                        String message = Objects.requireNonNull(task.getException()).getMessage();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this,  message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
