package com.example.socialmedia;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;


public  class FireBaseUserDataHandler {

    private UserAccount foundUser;

    private FirebaseAuth mAuth ;
    private FirebaseUser user  ;
    private String UI ;
    //TODO: Potential bug when provided invalid user, or user DNE.
    private DatabaseReference completeDB ;
    private DatabaseReference dB ;

    public FireBaseUserDataHandler(){

        foundUser = new UserAccount();
        mAuth = FirebaseAuth.getInstance();
        user  = mAuth.getCurrentUser();
        if(user!= null) {
            UI = Objects.requireNonNull(user).getUid();
        }
        completeDB = FirebaseDatabase.getInstance().getReference().child("Users"); // we create this database structure from our desired userid!;
        if(user!=null) {
            dB = completeDB.child((user).getUid()); // we create this database structure from our desired userid!;
        }
    } // Main constructor. By defualt it takes current user

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseUser getUser(){
        return this.user;
    } // returns the most up to date stored user

    public String getUI(){
        return this.UI;
    }// returns current User Id

    public Boolean UpdateUser(UserAccount inUser, final Context context){

        Map<String,Object> mappedUser = inUser.toMap();
        Task performTask = dB.updateChildren(mappedUser).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Log.d("DATABASE", "Successful in logging in!"); // Find out why we need this twice!
                        }else {
                            Log.e("DATABASE", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage())); // Find out why we need this twice!

                        }
                    }
                }
        );
        //TODO: Perform tests to make sure this is working perfectly
        if(performTask.isCanceled()){
            return false;
        }
        else{
            return true;
        }
    }// after passing in the context for the Toast message and the User Account with the correctly stored data we can update it!

    public UserAccount GetUserDataByUI(String UI){
        //TODO: parse db object into UserAccount object
        if(UI != null) {
            UserAccount account = new UserAccount();
            this.UI = UI;
            completeDB.addValueEventListener(dataListener);
            return foundUser;
        }
        else{
            Log.e("DATA", "Error! a null id was provided!");
            return null;
        }
    } // This method can also be used to try and find info for another user as well

    public Boolean CheckIfUserNameExists(String UserName){
        //TODO: Verify that no such username exists!
        return false;
    } //TODO: Make a query to find out if such a user already exists

    private ValueEventListener dataListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("DATA", "Successful in reading.");;
            foundUser = dataSnapshot.getValue(UserAccount.class); // works since we already filtered out the DB with our userID
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("DATA", databaseError.toException().toString());
        }
    }; // This is the on data change listner. It is very important for updated information.


}
