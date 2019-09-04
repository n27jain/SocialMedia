package com.example.socialmedia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Handlers.FireBaseUserDataHandler;
import com.example.socialmedia.Objects.PostObject;
import com.example.socialmedia.Objects.UserAccount;
import com.example.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<PostObject> ListOfPosts;
/*
    private ImageView postImage;
    private TextView userName ;
    private TextView message ;
    private TextView dateAndTime ;
    private FireBaseUserDataHandler dataHandler = new FireBaseUserDataHandler();
    private CircleImageView profileImage;
    private String url ;
    private String userNameOfUser ;*/

    public PostsAdapter(Context context, ArrayList<PostObject> ListOfPosts) {
        this.context = context;
        this.ListOfPosts = ListOfPosts;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate
                (R.layout.posts_layout,parent,false);

        MyViewHolder holder = new MyViewHolder(convertView);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) { // here we can assign the value of the given object
        // we cannot make async task in this method. This is pretty much a hack solution
        PostObject object = ListOfPosts.get(position);
        UserAccount account = object.getOptionalAccountLink();
        String userName = account.getUserName();
        String dpUrl = account.getDPUrl();

        if(dpUrl!= null && !dpUrl.isEmpty() ){
            Picasso.get().load(dpUrl).into( holder.profileImage);
        }
        if(userName!= null && !userName.isEmpty()) {
            holder.userName.setText(userName);
        }

        if (object != null) {
            String messageByPost = object.getMessage();
            String url = object.getImgUrl();
            if(messageByPost != null && !messageByPost.isEmpty() ){
                holder.message.setText(messageByPost);
            }
            else{
                holder.message.setVisibility(View.GONE);
            }
            if( url != null && !url.isEmpty()){
                Picasso.get().load(url).into( holder.postImage);
            }
            else{
                holder.postImage.setVisibility(View.GONE);
            }

        }
        //TODO: Make sure the get user stuff is in async with the return.
        // we dont want it to return an object without image.
    }

    @Override
    public int getItemCount() {
        return ListOfPosts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView postImage;
        private TextView userName ;
        private TextView message ;
        private TextView dateAndTime ;
        private FireBaseUserDataHandler dataHandler = new FireBaseUserDataHandler();
        private CircleImageView profileImage;
        private String url = null ;
        private String userNameOfUser = null;

        public MyViewHolder(View itemView) { // prepare the view. This is different from an array adapter
            super(itemView);
            userName = itemView.findViewById(R.id.userNameTextView);
            message = itemView.findViewById(R.id.messageTextView);
            dateAndTime =itemView.findViewById(R.id.dateTime);
            postImage = itemView.findViewById(R.id.postOptionalImage);
            profileImage = itemView.findViewById(R.id.circleProfileImageView);

        }
    }
}








    /*private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.posts_layout,parent,false);
        }
        userName = convertView.findViewById(R.id.userNameTextView);
        message = convertView.findViewById(R.id.messageTextView);
        dateAndTime = convertView.findViewById(R.id.dateTime);
        postImage = convertView.findViewById(R.id.postOptionalImage);
        profileImage = convertView.findViewById(R.id.circleProfileImageView);

        PostObject object = getItem(position);
        dataHandler.GetUserDataByUI(object.getUserID(), new FireBaseUserDataHandler.DataStatus() {
            @Override
            public void DataIsLoaded(UserAccount foundUser) {
                url = foundUser.getDPUrl();
                userNameOfUser = foundUser.getUserName();
                userName.setText(userNameOfUser);
                if(url!= null){
                    Picasso.get().load(url).into(profileImage);
                }
            }
            @Override
            public void Error() {

            }
        });

        if (object != null) {
            String messageByPost = object.getMessage();
            String url = object.getImgUrl();
            if(! messageByPost.isEmpty() ){
                message.setText(messageByPost);
            }
            else{
                message.setVisibility(View.GONE);
            }
            if(! url.isEmpty()){
                Picasso.get().load(url).into(postImage);
            }
            else{
                postImage.setVisibility(View.GONE);
            }

        }
        //TODO: Make sure the get user stuff is in async with the return.
        // we dont want it to return an object without image.
        return convertView;

    }*/

