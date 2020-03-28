package com.example.socialmedia.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Adapters.PostsAdapter;
import com.example.socialmedia.Handlers.FireBasePostHandler;
import com.example.socialmedia.Objects.PostObject;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;

import static android.app.Activity.RESULT_OK;


public class FeedFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //TODO: Create a loading wheel right now our info is misleading.
    private Button postButton;
    private Button insertImageButton;
    private EditText postMessage;
    private ImageView uploaded_image;
    private String resultUri = null;
    private String TAG = "FeedFragment";
    private ProgressDialog loadingBar;
    private FireBasePostHandler postHandler;
    private RecyclerView allPostsRecyclerView;
    private ArrayList<PostObject> ListOfPosts;
    private PostsAdapter postsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //postButton
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userId = FirebaseAuth.getInstance().getUid();
        allPostsRecyclerView = view.findViewById(R.id.allPostsRecyclerView);
        if(userId!=null) {
            postHandler = new FireBasePostHandler(userId) {
                @Override
                public void UpdatePostsAdapter() {
                    initAdapter();
                }
            };
        }
        postMessage = view.findViewById(R.id.newPostMessage);
        uploaded_image = view.findViewById(R.id.uploaded_image);

        postButton = view.findViewById(R.id.postButton);
        postButton.setImeOptions(EditorInfo.IME_ACTION_DONE);
        insertImageButton = view.findViewById(R.id.newPostInsertImage);
        postButton.setOnClickListener(postButtonOnClickListener);
        insertImageButton.setOnClickListener(insertImageButtonOnClickListener);
        loadingBar = new ProgressDialog(getActivity());

        initAdapter();
    }

    private void initAdapter(){
         ListOfPosts = postHandler.getCurrentUserAndFriendsPosts();
         postsAdapter = new PostsAdapter(getContext(),ListOfPosts);
         allPostsRecyclerView.setAdapter(postsAdapter);
         allPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,true));
    }

    private OnClickListener postButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            String message = postMessage.getText().toString();
            final PostObject postObject = new PostObject();
            Uri inputUri = null;
            if(resultUri!=null) {
                postObject.setImgUrl(resultUri);
                inputUri = Uri.parse(resultUri);
            }
            if(resultUri != null || !message.isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("yyy-MM-dd-");
                postObject.setMessage(message);
                postObject.setUserID(FirebaseAuth.getInstance().getUid());
                postObject.setPostID(postHandler.getMaxId());
                postObject.setTimeCreated(System.currentTimeMillis()/1000);

                postHandler.CreatePost(postObject, inputUri, new FireBasePostHandler.UpdateStatus() {
                    @Override
                    public void DataIsLoaded() {
                        loadingBar.dismiss();
                        Log.d(TAG, "Data successfully loaded");

                        //android:layout_height="0dp"
                        postMessage.setText(null);
                        uploaded_image.invalidate();
                        uploaded_image.setImageBitmap(null);
                        uploaded_image.setImageURI(null);
                        ViewGroup.LayoutParams params = uploaded_image.getLayoutParams();
                        params.height = 0;
                        uploaded_image.setLayoutParams(params);
                        insertImageButton.setText("Insert Image");
                        postHandler = new FireBasePostHandler(postObject.getUserID());
                    }

                    @Override
                    public void Error() {
                        loadingBar.dismiss();
                        Log.e(TAG, "Unable to load the post");

                    }

                });
            }
            else{
                loadingBar.dismiss();
                Toast.makeText(getActivity(), "Please write a message or upload an image", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private OnClickListener insertImageButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // Make the ability for user to upload image. We need to get context of our activity
            CropImage.activity()
                    .start((Activity) getContext(), FeedFragment.this);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri setImage =result.getUri();
                resultUri = setImage.toString();
                ViewGroup.LayoutParams layoutParams = uploaded_image.getLayoutParams();
                final float scale = getContext().getResources().getDisplayMetrics().density;
                int pixels = (int) (250 * scale + 0.5f);// convert pixels to dp by display size
                layoutParams.height = pixels;
                uploaded_image.setLayoutParams(layoutParams);
                uploaded_image.setImageURI(setImage);
                insertImageButton.setText("Change Image");
            }
        }
    }



}
