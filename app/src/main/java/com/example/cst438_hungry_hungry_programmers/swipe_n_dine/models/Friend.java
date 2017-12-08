package com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models;

/**
 * Created by coleman on 12/7/17.
 */



public class Friend {
    public String name;
    public String uid;

    public Friend(){
        name = "";
        uid = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Friend){
            Friend otherFriend = (Friend) other;
            if(otherFriend.getUid().equals(this.getUid())){
                return true;
            }
        }
        return false;
    }

}
