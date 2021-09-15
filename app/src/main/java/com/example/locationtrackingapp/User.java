package com.example.locationtrackingapp;

import java.util.Date;

public class User {
    public String fullName,Email,GroupName,Longitude,Latitude;
    public Date Time;
    public User(){
    }
    public User(String full_name,String email_id,String group,String latitude,String longitude,Date time){
        this.Email = email_id;
        this.fullName= full_name;
        this.GroupName = group;
        this.Latitude= latitude;
        this.Longitude= longitude;
        this.Time = time;

    }
//    public void UserSetLatitude(String latitude){
//        this.Latitude= latitude;
//    }
//    public void UserSetLongitude(String longitude){
//        this.Longitude= longitude;
//    }
//    public void UserTime(Date time){
//        this.Time = time;
//    }
}

