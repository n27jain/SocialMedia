package com.example.socialmedia.Fragments.Friends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Handlers.FireBaseUserDataHandler;
import com.example.socialmedia.Handlers.FireBaseUserFriendsHandler;
import com.example.socialmedia.Objects.UserAccount;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class MyFriendsFragment extends Fragment {

    private String resultUri = null;
    private String TAG = "FeedFragment";
    private ProgressDialog loadingBar;
    private RecyclerView friendsListRecyclerView;
    private FireBaseUserFriendsHandler friendsHandler;
    private ArrayList<String> ListOfFriends;
    private ArrayList<UserAccount> UserAccountsFromFriendId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //postButton
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsListRecyclerView = view.findViewById(R.id.friendsListRecyclerView);
        friendsHandler = new FireBaseUserFriendsHandler(FirebaseAuth.getInstance().getUid()){
            @Override
            public void AllUserAccountsInitialized(){
                initAdapter();
            }
        };
        friendsHandler.getListOfFriendsUserID();
        loadingBar = new ProgressDialog(getActivity());
        initAdapter();
    }

    private void initAdapter() {
        //TODO: Create a list of users based on friends list
        // Send in the User accounts to the adapter to be processed into view.
        // What we need to display is DP, username, real name, #about, gender, country
        FireBaseUserDataHandler dataHandler = new FireBaseUserDataHandler();
        ListOfFriends= friendsHandler.getListOfFriends();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
