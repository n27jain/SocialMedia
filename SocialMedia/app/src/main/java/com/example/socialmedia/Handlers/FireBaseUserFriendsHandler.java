package com.example.socialmedia.Handlers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialmedia.Objects.UserAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseUserFriendsHandler {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDB;
    private DatabaseReference usersListOfFriendsDB;
    private int numberOfFriends;
    private String TAG = "FireBaseUserFriendsHandler";
    private ArrayList<String> ListOfFriends;
    private ArrayList<UserAccount> ListOfFoundUsers;
    private int counterComplete = 0;
    private FireBaseUserDataHandler dataHandler = new FireBaseUserDataHandler();

    private ArrayList<UserAccount> UserAccountsFromFriendId;

    public FireBaseUserFriendsHandler(String UserId){ // this is the userid of the current user
        ListOfFoundUsers = new ArrayList<UserAccount>() ;
        ListOfFriends = new ArrayList<String>();
        UserAccountsFromFriendId = new ArrayList<UserAccount>() ;
        numberOfFriends = 0;
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDB = firebaseDatabase.getReference().child("Users").child(UserId);
        usersListOfFriendsDB = usersDB.child("Friends");
        usersListOfFriendsDB.addChildEventListener(UsersListOfFriendsDBChildChange);

    }
    public void addFriend(String newFriendUserID){
        //TODO: Take the new friends Id and add them to friend list of the user
       usersListOfFriendsDB.setValue(newFriendUserID);

    }
    public void removeFriend(String friendIDToRemove){
        //TODO: When user clicks remove friend we take that users Id and erase it from the friends list.

    }
    //TODO: get list of friends

    public void getListOfUsersByUserId(String searchValue){
        //TODO: find users by userID
        // For now lets just show all of the users


    }
    public void  getListOfFriendsUserID(){
        usersListOfFriendsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Successful in reading list of friends");
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    ListOfFriends.add(i.getValue().toString());
                    numberOfFriends++;
                }
                try {
                    ListOfFriendsInitialized();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void ListOfFriendsInitialized() throws InterruptedException {
       //Now we need to initialize the user accounts associated


        for(String i : ListOfFriends){
            dataHandler.GetUserDataByUI(i, new FireBaseUserDataHandler.DataStatus() {
                @Override
                public void DataIsLoaded(UserAccount foundUser) {
                    UserAccountsFromFriendId.add(foundUser);
                    counterComplete++;
                }

                @Override
                public void Error() {

                }
            });

        }
        while( counterComplete != ListOfFriends.size()){
            Thread.sleep(1000);
        }
        AllUserAccountsInitialized();// Once all the users are stored we are good
        counterComplete = 0;
    }
    public void AllUserAccountsInitialized(){
        // We will override this in the activity to allow us to know that the friends have been initialized

    }

    private ChildEventListener UsersListOfFriendsDBChildChange = new ChildEventListener(){
        //TODO: OPTIMIZE THIS
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ArrayList<String> getListOfFriends() {
        return ListOfFriends;
    }

    public void setListOfFriends(ArrayList<String> ListOfFriends) {
        ListOfFriends = ListOfFriends;
    }


    public ArrayList<UserAccount> getUserAccountsFromFriendId() {
        return UserAccountsFromFriendId;
    }

    public void setUserAccountsFromFriendId(ArrayList<UserAccount> userAccountsFromFriendId) {
        UserAccountsFromFriendId = userAccountsFromFriendId;
    }

}
