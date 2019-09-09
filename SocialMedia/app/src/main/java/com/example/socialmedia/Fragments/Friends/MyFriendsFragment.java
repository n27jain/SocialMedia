package com.example.socialmedia.Fragments.Friends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Adapters.FriendsAdapter;
import com.example.socialmedia.Constants;
import com.example.socialmedia.Handlers.FireBaseUserFriendsHandler;
import com.example.socialmedia.Objects.UserAccount;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MyFriendsFragment extends Fragment {

    private String resultUri = null;
    private String TAG = "MyFriends";
    private ProgressDialog loadingBar;

    private RecyclerView friendsListRecyclerView;
    private FriendsAdapter friendsAdapter;
    private FireBaseUserFriendsHandler friendsHandler;
    private ArrayList<UserAccount> UserAccountsFromFriendId;
    private EditText  searchFriendEditText;
    private String lastSearch = "";

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
        friendsHandler = new FireBaseUserFriendsHandler(FirebaseAuth.getInstance().getUid()){
            @Override
            public void onCompleteFriendsUserAccountsStored(ArrayList<UserAccount> foundFriendsAccounts){
                UserAccountsFromFriendId = foundFriendsAccounts;
                initAdapter();
            }
        };

        UserAccountsFromFriendId = new ArrayList<UserAccount>();
        friendsHandler.getListOfFriendsUserID();

        loadingBar = new ProgressDialog(getActivity());


        friendsListRecyclerView = view.findViewById(R.id.friendsListRecyclerView);
        searchFriendEditText = view.findViewById(R.id.searchFriendEditText);
        searchFriendEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //TODO: search through friends
        /*searchFriendEditText .setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String search = searchFriendEditText .getText().toString();
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        if(lastSearch != search ) {
                            friendsHandler.SearchForFriendsByUserName(searchFriendEditText .getText().toString());
                        }
                        return true;
                    case EditorInfo.IME_ACTION_NEXT:
                    case EditorInfo.IME_ACTION_PREVIOUS:
                        return true;
                }
                return false;
            }
        });*/
    }

    private void initAdapter() {
       /* UserAccountsFromFriendId = friendsHandler.getUserAccountsFromFriendId();*/
        friendsAdapter = new FriendsAdapter(getContext(),UserAccountsFromFriendId, Constants.FRIEND_BUTTON_REMOVE);
        friendsListRecyclerView.setAdapter(friendsAdapter);
        friendsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadingBar.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
