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

import com.example.socialmedia.R;


public class FriendsFragment extends Fragment {
    private String resultUri = null;

    private String TAG = "FeedFragment";
    private ProgressDialog loadingBar;

    private Button findFriendsButton;
    private Button myFriendsButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //postButton
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userId = " "; // TODO: pass in the userID with the intent.
        loadingBar = new ProgressDialog(getActivity());
        findFriendsButton = view.findViewById(R.id.findFriendsButton);
        myFriendsButton = view.findViewById(R.id.myFriendsButton);
        findFriendsButton.setOnClickListener(findFriendsButtonOnClickListener);
        myFriendsButton.setOnClickListener(myFriendsButtonOnClickListener);

    }

    private OnClickListener myFriendsButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_friends_container,new MyFriendsFragment()).commit();
        }
    };
    private OnClickListener findFriendsButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_friends_container,new FindFriendsFragment() ).commit();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
