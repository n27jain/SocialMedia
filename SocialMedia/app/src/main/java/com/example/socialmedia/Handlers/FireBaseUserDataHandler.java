package com.example.socialmedia.Handlers;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.socialmedia.Objects.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.Objects;


public  class FireBaseUserDataHandler {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDB;
    private DatabaseReference selectUserDB;
    private UserAccount foundUser; // this will be the stored user account found from our db
    private StorageReference UserProfileImageStorageReference;
    private String ImgTAG = "IMAGETAG";
    public interface DataStatus{
        void DataIsLoaded(UserAccount foundUser);
        void Error();

    }
    public interface UpdateStatus{
        void DataIsLoaded();
        void Error();

    }

    public FireBaseUserDataHandler() { // constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDB = firebaseDatabase.getReference().child("Users");
        UserProfileImageStorageReference= FirebaseStorage.getInstance().getReference();
    }

    public void GetUserDataByUI(String UI, final DataStatus dataStatus){

        if(UI != null) {
            selectUserDB = usersDB.child(UI);
            selectUserDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("DATA", "Successful in reading.");
                    UserAccount outPutUser = dataSnapshot.getValue(UserAccount.class); // works since we already filtered out the DB with our userID
                    dataStatus.DataIsLoaded(outPutUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DATA", databaseError.toException().toString());
                    dataStatus.Error();
                }
            });

        }

        else{
            Log.e("DATA", "Error! a null id was provided!");
            dataStatus.Error();
        }
    } // This method can also be used to try and find info for another fireBaseUser as well

    public Boolean CheckIfUserNameExists(String UserName){
        //TODO: Verify that no such username exists!
        return false;
    }

    public Boolean UpdateUser(UserAccount inUser, final Context context , final UpdateStatus updateStatus ){
        selectUserDB = usersDB.child(inUser.getUserID());
        Map<String,Object> mappedUser = inUser.toMap();
        Task performTask = selectUserDB.updateChildren(mappedUser).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            updateStatus.DataIsLoaded();
                            Log.d("DATABASE", "Successful in trying to gain data!"); // Find out why we need this twice!
                        }else {
                            updateStatus.Error();
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

    public void SaveProfileImage(final String UserID, Uri uri,  final UpdateStatus updateStatus){
        final StorageReference profileImageRef = UserProfileImageStorageReference.child("Users").child(UserID).child("profile_picture");

        profileImageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        Log.d(ImgTAG, "Profile Image successfully Stored!");

                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                Log.d(ImgTAG, "downloadUrl:" + downloadUrl);
                                UserAccount updateUriOnly = new UserAccount();
                                updateUriOnly.setDPUrl(downloadUrl);
                                updateUriOnly.setUserID(UserID);
                                UpdateUser(updateUriOnly, null, new UpdateStatus() {
                                            @Override
                                            public void DataIsLoaded() {
                                                updateStatus.DataIsLoaded();
                                            }

                                            @Override
                                            public void Error() {
                                                updateStatus.Error();
                                            }
                                        }
                                );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //TODO: Handle any errors
                                Log.e(ImgTAG, "this guy failed");
                            }
                        });
                    }
                    else{
                        Log.e(ImgTAG, "Profile Image unable to be Stored!");
                    }
                }

            }

        );

    }











}
