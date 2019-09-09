package com.example.socialmedia.Objects;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {

    private String UserName = null , FullName = null, Email = null, Phone = null , UserID = null ;

    private String DPUrl = null;

    private int Country, Gender, Status;

    //Country
    //1.) United States of America
    //2.) Canada
    //3.) India

    //Gender
    //1.) MALE
    //2.) FEMALE
    //3.) OTHER

    //Status
    //0.) New
    //1.) REGISTERED
    //3.) BANNED
    //4.) ERROR


    public UserAccount(String userName, String fullName, String email, String phone, String dpUrl, int country, int gender, int status) {

        if(userName != null)
        UserName = userName;

        if(fullName != null)
        FullName = fullName;

        if(email != null)
        Email = email;

        if(phone != null)
        Phone = phone;

        if(dpUrl != null)
        DPUrl = dpUrl;

        Country = country;
        Gender = gender;
        Status = status;
    }

    public UserAccount() {
        // Blank Constructor
    }

    public String getUserName() {
            return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFullName() {
            return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
            return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
            return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
    public String getDPUrl() {
            return DPUrl;
    }

    public void setDPUrl(String DPUrl) {
        this.DPUrl = DPUrl;
    }

    public int getCountry() {
        return Country;
    }

    public void setCountry(int country) {
        Country = country;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put("UserID",UserID);
        userMap.put("UserName",UserName);
        userMap.put("FullName",FullName);
        if(Email!= null && !Email.isEmpty()){
            userMap.put("Email",Email);
        }
        if(DPUrl!= null && !DPUrl.isEmpty()){
            userMap.put("DPUrl", DPUrl);
        }
        if(Phone!= null && !Phone.isEmpty()){
            userMap.put("Phone No",Phone);
        }
        userMap.put("Country",Country );
        userMap.put("Gender",Gender);
        userMap.put("Status",Status);

        return userMap;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
