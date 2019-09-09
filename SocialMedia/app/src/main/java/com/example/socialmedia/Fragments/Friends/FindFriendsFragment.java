package com.example.socialmedia.Fragments.Friends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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


public class FindFriendsFragment extends Fragment {

    private String TAG = "FindFriendFragment";
    private ProgressDialog loadingBar;
    private EditText findFriendEditText;
    private FireBaseUserFriendsHandler friendsHandler;
    private String lastSearch;


    private RecyclerView allFriendsRecyclerView;
    private ArrayList<UserAccount> ListOfFriends;
    private FriendsAdapter friendsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //postButton
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_friends, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsHandler = new FireBaseUserFriendsHandler(FirebaseAuth.getInstance().getUid()){
            @Override public void onCompleteSearchForFriendsByUserName(ArrayList<UserAccount> foundFriendsAccounts) {
                // to be accessed and overwritten in the friends activity.
                initListOfFriendsAdapter();

            }
        };
        allFriendsRecyclerView = view.findViewById(R.id.friendsListRecyclerView);
        ListOfFriends = new ArrayList<UserAccount>();


        loadingBar = new ProgressDialog(getActivity());
        findFriendEditText = view.findViewById(R.id.findFriendEditText);
        findFriendEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        findFriendEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String search = findFriendEditText.getText().toString();
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        if(lastSearch != search ) {
                            friendsHandler.SearchForFriendsByUserName(findFriendEditText.getText().toString());
                        }
                        return true;
                    case EditorInfo.IME_ACTION_NEXT:
                    case EditorInfo.IME_ACTION_PREVIOUS:
                        return true;
                }
                return false;
            }
        });
    }

    private void initListOfFriendsAdapter() {
        Log.d(TAG, "Tried to insert feed");
        loadingBar.setTitle("Loading Posts...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false); // prevent user from touching the outside to close it*/
        ListOfFriends = friendsHandler.getListOfFoundUsers();
        friendsAdapter = new FriendsAdapter(getContext(),ListOfFriends, Constants.FRIEND_BUTTON_ADD);
        allFriendsRecyclerView.setAdapter(friendsAdapter);
        allFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadingBar.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
