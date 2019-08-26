package com.example.socialmedia;

public class SpinnerObject {
    public String  label;
    public int iconNum;
    public int storeValue; // This is the value we store in the user profile.

    //The int to image mapping

    //1.) USA
    //2.) CAN
    //3.) INDIA

    //4.) MALE
    //5.) FEMALE
    //6.) OTHER

    public SpinnerObject(String name, int imageNum, int storeValue){
        label = name;
        iconNum = imageNum;
        this.storeValue = storeValue;
    }


}
