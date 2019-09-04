package com.example.socialmedia.Objects;

import java.util.HashMap;
import java.util.Map;

public class PostObject {
    public long getPostID() {
        return PostID;
    }

    private long PostID = 0;
    private String UserID;
    private String ImgUrl;
    private String Message;
    private UserAccount optionalAccountLink;
    /*private int Appeal;
    private int Unappeal;*/
    //TODO: include option for sharing location, emotion, etc...
    public void setPostID(long postID){
        PostID = postID;
    }
    public PostObject(){

    }
    public PostObject(Long postId,String userId, String imgUrl, String message){

        this.PostID =  postId;
        this.UserID = userId ;
        this.ImgUrl = imgUrl;
        this.Message = message;

        /*this.Appeal = appeal;
        this.Unappeal = unappeal;*/
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.ImgUrl = imgUrl;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

   /* public int getAppeal() {
        return Appeal;
    }

    public int getUnappeal() {
        return Unappeal;
    }*/

    public Map<String, Object> toMap() {
        HashMap<String, Object> postMap = new HashMap<>();
        if(PostID != 0) {
            postMap.put("PostID", PostID);
        }
        postMap.put("UserID", UserID);
        postMap.put("ImgUrl",ImgUrl);
        postMap.put("Message",Message);

        //TODO: Determine how to store Appeals and Unappeals
        return postMap;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) { UserID = userID;
    }


    public UserAccount getOptionalAccountLink() {
        return optionalAccountLink;
    }

    public void setOptionalAccountLink(UserAccount optionalAccountLink) {
        this.optionalAccountLink = optionalAccountLink;
    }
}
