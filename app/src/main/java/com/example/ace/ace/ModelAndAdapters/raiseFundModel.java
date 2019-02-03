package com.example.ace.ace.ModelAndAdapters;

import java.io.Serializable;

public class raiseFundModel implements Serializable {

    public String userID,userName,requestNum,story,titles,upiID,contact;


    public raiseFundModel() {
    }

    public raiseFundModel(String userID, String userName, String requestNum, String story, String titles, String upiID, String contact) {
        this.userID = userID;
        this.userName = userName;
        this.requestNum = requestNum;
        this.story = story;
        this.titles = titles;
        this.upiID = upiID;
        this.contact = contact;
    }
}
