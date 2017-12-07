package com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleman on 12/6/17.
 */

public class User {
    public String uid;
    public List<String> friends;
    public List<String> favorites;
    public List<String> groups;

    public User(){
        uid = "";
        friends = new ArrayList<>();
        favorites = new ArrayList<>();
        groups = new ArrayList<>();
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }
}
