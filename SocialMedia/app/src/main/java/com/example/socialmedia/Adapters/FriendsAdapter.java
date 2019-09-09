package com.example.socialmedia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Constants;
import com.example.socialmedia.Handlers.FireBaseUserDataHandler;
import com.example.socialmedia.Handlers.FireBaseUserFriendsHandler;
import com.example.socialmedia.Objects.PostObject;
import com.example.socialmedia.Objects.UserAccount;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder>{

    private int typeOfButton;
    private Context context;
    private ArrayList<UserAccount> ListOfFriends;

    public FriendsAdapter(Context context, ArrayList<UserAccount> ListOfPosts, int typeOfButton) {
        this.context = context;
        this.ListOfFriends = ListOfPosts;
        this.typeOfButton = typeOfButton;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate
                (R.layout.friend_layout ,parent,false);

        MyViewHolder holder = new MyViewHolder(convertView);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) { // here we can assign the value of the given object
        // we cannot make async task in this method. This is pretty much a hack solution
        final UserAccount object = ListOfFriends.get(position);
        final String userName = object.getUserName();
        String dpUrl = object.getDPUrl();
        int countryFlagNum = object.getCountry();
        int genderNum = object.getGender();


        if(dpUrl!= null && !dpUrl.isEmpty() ){
            Picasso.get().load(dpUrl).into(holder.circleProfileImageView);
        }
        if(userName!= null && !userName.isEmpty()) {
            holder.userName.setText(userName);
        }
        else{
            holder.userName.setText("UNKNOWN");
        }
        switch(countryFlagNum){
            case Constants.USA:
                holder.countryFlag.setImageResource(R.drawable.usa);
                break;
            case Constants.CAN:
                holder.countryFlag.setImageResource(R.drawable.canada);
                break;
            case Constants.INDIA:
                holder.countryFlag.setImageResource(R.drawable.india);
                break;
            default:
                holder.countryFlag.setVisibility(View.GONE);
                break;
        }
        switch (genderNum){
            case Constants.GEN_MALE:
                holder.gender.setImageResource(R.drawable.male);
                break;
            case Constants.GEN_FEMALE:
                holder.gender.setImageResource(R.drawable.female);
                break;
            case Constants.GEN_OTHER:
                holder.gender.setImageResource(R.drawable.other);
                break;
        }
        if(typeOfButton == Constants.FRIEND_BUTTON_ADD) {
            holder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.userFriendsHandler.addFriend(object.getUserID());
                    ListOfFriends.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        else if(typeOfButton == Constants.FRIEND_BUTTON_REMOVE){
            holder.addFriend.setText("REMOVE");
            holder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {/*
                    //TODO: REMOVE FRIEND
                    holder..addFriend(object.getUserID());
                    ListOfFriends.remove(position);
                    notifyDataSetChanged();*/
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return ListOfFriends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private  CircleImageView circleProfileImageView;
        private TextView userName ;
        private ImageView countryFlag;
        private ImageView gender;
        private Button addFriend;
        private FireBaseUserFriendsHandler userFriendsHandler;

        public MyViewHolder(View itemView) { // prepare the view. This is different from an array adapter
            super(itemView);
            userFriendsHandler = new FireBaseUserFriendsHandler(FirebaseAuth.getInstance().getUid().toString());
            userName = itemView.findViewById(R.id.userNameTextView);
            circleProfileImageView = itemView.findViewById(R.id.circleProfileImageView);
            countryFlag = itemView.findViewById(R.id.countryFlag);
            gender = itemView.findViewById(R.id.gender);
            addFriend = itemView.findViewById(R.id.addFriend);
        }
    }
}








