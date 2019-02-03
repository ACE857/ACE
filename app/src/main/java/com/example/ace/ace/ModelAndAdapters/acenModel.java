package com.example.ace.ace.ModelAndAdapters;

import java.io.Serializable;

public class acenModel implements Serializable {
    public String userid,username,acenid,title,whathappned,story,img,finalthought;

    public acenModel(String userid, String username, String acenid, String title, String whathappned, String story, String img, String finalthought) {
        this.userid = userid;
        this.username = username;
        this.acenid = acenid;
        this.title = title;
        this.whathappned = whathappned;
        this.story = story;
        this.img = img;
        this.finalthought = finalthought;
    }

    public acenModel() {
    }
}
