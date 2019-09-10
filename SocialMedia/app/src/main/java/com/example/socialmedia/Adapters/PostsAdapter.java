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
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<PostObject> ListOfPosts;

    public PostsAdapter(Context context, ArrayList<PostObject> ListOfPosts ) {
        this.context = context;
        Collections.sort(ListOfPosts, new Comparator<PostObject>() {
            @Override public int compare(PostObject p1, PostObject p2) {
                return p2.getDate().compareTo(p1.getDate()); // Ascending
            }
        });
        this.ListOfPosts = ListOfPosts;
        //ListOfPosts
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
        if (object != null) {
            String dateToDisplay = object.getDate();
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
            holder.dateAndTime.setText(dateToDisplay);

        }
        if(account != null){
            String userName = account.getUserName();
            String dpUrl = account.getDPUrl();
            if(dpUrl!= null && !dpUrl.isEmpty() ){
                Picasso.get().load(dpUrl).into( holder.profileImage);
            }
            if(userName!= null && !userName.isEmpty()) {
                holder.userName.setText(userName);
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




