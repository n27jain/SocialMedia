package com.example.socialmedia.Handlers;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialmedia.Adapters.PostsAdapter;
import com.example.socialmedia.Objects.PostObject;
import com.example.socialmedia.Objects.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class FireBasePostHandler {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference postsDB;
    private DatabaseReference selectUsersPostsDB;
    private FireBaseUserFriendsHandler userFriendsHandler;
    final String TAG = "PostHandler";
    private long maxId = 0;
    private StorageReference UserPostImageStorageReference;
    String PostTAG = "POSTTAG";

    private  ArrayList<PostObject> currentUserAndFriendsPosts;
    private UserAccount CurrentUserAccount;
    private int countDownBeforeInitialize = 0;

    private ArrayList<UserAccount> allFoundFriendsAccounts;

    public long getMaxId() {
        return maxId;
    }

    public interface UpdateStatus{
        void DataIsLoaded();
        void Error();
    }

    public FireBasePostHandler(String UserId) {
        UserPostImageStorageReference = FirebaseStorage.getInstance().getReference();
        currentUserAndFriendsPosts = new ArrayList<PostObject>();
        CurrentUserAccount = new UserAccount();
        FireBaseUserDataHandler fireBaseUserDataHandler = new FireBaseUserDataHandler();
        fireBaseUserDataHandler.GetUserDataByUI(UserId, new FireBaseUserDataHandler.DataStatus() {
            @Override
            public void DataIsLoaded(UserAccount foundUser) {
                CurrentUserAccount = foundUser; // store the current users account here.
            }

            @Override
            public void Error() {
                Log.e(TAG, "CRITICAL ERROR INVESTIGATE THIS! LIKELY CAUSE USER WAS A FRIEND THAT IS NOW DELETED");
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        postsDB = firebaseDatabase.getReference().child("Posts");
        selectUsersPostsDB = postsDB.child(UserId); // this database contains all of the posts of the current user.
        selectUsersPostsDB.addValueEventListener(valueEventListenerForCurrentUser); // this should retrieve every post from the user.

        userFriendsHandler = new FireBaseUserFriendsHandler(UserId){
            @Override
            public void onCompleteFriendsUserAccountsStored(ArrayList<UserAccount> foundFriendsAccounts) {

                allFoundFriendsAccounts = foundFriendsAccounts;
                int finishCounting = allFoundFriendsAccounts.size();
                countDownBeforeInitialize = 0 ;
                for(UserAccount i : allFoundFriendsAccounts){
                    DatabaseReference newUsersPostsDB = postsDB.child(i.getUserID());
                    newUsersPostsDB.addValueEventListener(valueEventListener);
                }
               // UpdatePostsAdapter();
            }
        };
        userFriendsHandler.getListOfFriendsUserID(); // trigger the getUserListOfFriends. j
    }


    private ValueEventListener valueEventListenerForCurrentUser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            final int lastSize = currentUserAndFriendsPosts.size();
            if (dataSnapshot.exists()) {

                maxId = dataSnapshot.getChildrenCount() + 1;
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    PostObject object = i.getValue(PostObject.class);
                    boolean alreadyExists = false;
                    for(PostObject k : currentUserAndFriendsPosts){
                        if(object.getPostID() == k.getPostID()){
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        //TODO: CHECK HERE
                        object.setOptionalAccountLink(CurrentUserAccount);
                        currentUserAndFriendsPosts.add(object);

                    }
                }
            }
            else {
                maxId = 1;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    PostObject object = i.getValue(PostObject.class);
                    if (!currentUserAndFriendsPosts.contains(object)) {
                        //TODO: CHECK HERE
                        for(UserAccount j: allFoundFriendsAccounts){ //TODO: Warning this is inefficient
                            if(j.getUserID() .equals( object.getUserID())){
                                object.setOptionalAccountLink(j);
                                break;
                            }
                        }
                        currentUserAndFriendsPosts.add(object);
                    }
                }
                countDownBeforeInitialize++;
                if(countDownBeforeInitialize >= allFoundFriendsAccounts.size()){ // we have processed the last friend
                    UpdatePostsAdapter();
                    countDownBeforeInitialize = 0;
                }
            }
            else {
                //TODO: Handle the empty data
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void CreatePost(final PostObject post, final Uri uri, final UpdateStatus updateStatus ) {

        final long setPostID = post.getPostID();

        if (uri != null) { // we have an image to save
            SavePostImage(post, uri);
        }
        else{
            UpdatePost(post);
        }
        updateStatus.DataIsLoaded();
    }

    public void SavePostImage(final PostObject postObject, Uri uri){

        final StorageReference postImageRef = UserPostImageStorageReference.child("Posts").child(""+ postObject.getUserID()).child(""+ postObject.getPostID()).child("post_image");

        postImageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(PostTAG, "Profile Image successfully Stored!");
                    postImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            PostObject newPostObject = postObject;
                            final String downloadUrl = uri.toString();
                            Log.d(PostTAG, "downloadUrl:" + downloadUrl);
                            newPostObject.setImgUrl(downloadUrl);
                            UpdatePost(newPostObject);
                        }
                    });
                }
            }
        });

    }

    public void UpdatePost(PostObject postObject){
        Map<String,Object> mappedUser = postObject.toMap();
        Task performTask = postsDB.child(postObject.getUserID()).child(""+ postObject.getPostID()).updateChildren(mappedUser).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("DATABASE", "Successful in trying to gain data!"); // Find out why we need this twice!

                            maxId++;
                        }else {
                            Log.e("DATABASE", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage())); // Find out why we need this twice!
                        }
                    }
                }
        );

    }

    public ArrayList<PostObject> getCurrentUserAndFriendsPosts(){
        return currentUserAndFriendsPosts;
    }


    public void UpdatePostsAdapter(){
        // we will override this in the fragment. What this helps us achieve is concurrency
    }
}
