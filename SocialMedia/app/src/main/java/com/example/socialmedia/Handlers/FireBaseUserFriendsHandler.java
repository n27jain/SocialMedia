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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseUserFriendsHandler {
    private String TAG = "FireBaseUserFriendsHandler";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDB;
    private DatabaseReference usersListOfFriendsDB;
    private DatabaseReference allUsersDB;
    private ArrayList<String> ListOfFriends;
    private ArrayList<UserAccount> ListOfFoundUsers;
    private ArrayList<UserAccount> UserAccountsFromFriendId;
    private FireBaseUserDataHandler dataHandler = new FireBaseUserDataHandler();
    private UserAccount newFriend;


    public FireBaseUserFriendsHandler(String UserId) { // this is the userid of the current user
        ListOfFoundUsers = new ArrayList<UserAccount>();
        UserAccountsFromFriendId = new ArrayList<UserAccount>();
        ListOfFriends = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        allUsersDB = firebaseDatabase.getReference().child("Users");
        usersDB = allUsersDB.child(UserId);
        usersListOfFriendsDB = usersDB.child("Friends");

        //usersListOfFriendsDB.addChildEventListener(UsersListOfFriendsDBChildChange);
    }

    public void addFriend(String newFriendUserID) {
        //Take the new friends Id and add them to friend list of the user
        newFriend = new UserAccount();
        newFriend.setUserID(newFriendUserID);
        dataHandler.GetUserDataByUI(newFriendUserID, new FireBaseUserDataHandler.DataStatus() {
            @Override
            public void DataIsLoaded(UserAccount foundUser) {
                if (foundUser == null) {
                    //TODO: put in error message
                } else {
                    newFriend.setUserName(foundUser.getUserName());
                    newFriend.setDPUrl(foundUser.getDPUrl());
                    newFriend.setGender(foundUser.getGender());
                    newFriend.setCountry(foundUser.getCountry());

                    usersListOfFriendsDB.push().updateChildren(newFriend.toMap());
                }

            }

            @Override
            public void Error() {

            }
        });
    }

    public void removeFriend(String friendIDToRemove) {
        // When user clicks remove friend we take that users Id and erase it from the friends list.
        //TODO: Test
        Query findDataContainingFriendId = usersListOfFriendsDB.orderByChild("UserID").startAt(friendIDToRemove).endAt(friendIDToRemove + "\uf8ff");
        findDataContainingFriendId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    i.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void SearchForFriendsByUserName(String searchValue) {
        // find users by userID
        Query searchForFriend = allUsersDB.orderByChild("UserName").startAt(searchValue).endAt(searchValue + "\uf8ff");
        ListOfFoundUsers = new ArrayList<UserAccount>(); // reset all found users array
        searchForFriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    UserAccount checkAccount = i.getValue(UserAccount.class);
                    if (!ListOfFriends.contains(checkAccount.getUserID()))
                        ListOfFoundUsers.add(checkAccount);
                }

                if (ListOfFoundUsers.size() > 0) {
                    onCompleteSearchForFriendsByUserName(ListOfFoundUsers);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onCompleteSearchForFriendsByUserName(ArrayList<UserAccount> foundFriendsAccounts) {
        // to be accessed and overwritten in the friends activity.
    }

    public void getListOfFriendsUserID() {
        usersListOfFriendsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d(TAG, "Successful in reading list of friends");
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Successful in reading list of friends");
                    UserAccount check = i.getValue(UserAccount.class);
                    if (!UserAccountsFromFriendId.contains(check)) {
                        UserAccountsFromFriendId.add(check);
                    }
                }
                onCompleteFriendsUserAccountsStored(UserAccountsFromFriendId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onCompleteFriendsUserAccountsStored(ArrayList<UserAccount> foundFriendsAccounts) {
        // We will override this in the activity to allow us to know that the friends have been initialized
    }

    private ChildEventListener UsersListOfFriendsDBChildChange = new ChildEventListener() {
        //TODO: OPTIMIZE THIS
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ListOfFriends.add(dataSnapshot.getValue().toString());

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

    public ArrayList<String> getStringListOfFriends() {
        return ListOfFriends;
    }

    public ArrayList<UserAccount> getListOfFoundUsers() {
        return ListOfFoundUsers;
    }

    public ArrayList<UserAccount> getUserAccountsFromFriendId() {
        return UserAccountsFromFriendId;
    }

}
