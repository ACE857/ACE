package com.example.ace.ace.ModelAndAdapters;

import java.io.Serializable;

public class sosEmergencyModel implements Serializable {
    public String userid,username,conname1,connum1,conname2,connum2;

    public sosEmergencyModel() {
    }

    public sosEmergencyModel(String userid, String username, String conname1, String connum1, String conname2, String connum2) {
        this.userid = userid;
        this.username = username;
        this.conname1 = conname1;
        this.connum1 = connum1;
        this.conname2 = conname2;
        this.connum2 = connum2;
    }
}
