package com.example.ace.ace.ModelAndAdapters;

import java.io.Serializable;

/**
 * Created by ace on 9/14/18.
 */

public class UserDataModel implements Serializable {

    String phone,level,name,pass,userid;



    public UserDataModel() {
    }

    public UserDataModel(String email, String level, String name, String pass, String userid) {
        this.phone = email;
        this.level = level;
        this.name = name;
        this.pass = pass;
        this.userid = userid;
    }

    public String getEmail() {
        return phone;
    }

    public void setEmail(String email) {
        this.phone = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
