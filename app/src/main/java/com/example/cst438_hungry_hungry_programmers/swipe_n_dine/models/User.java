package com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models;

import com.google.firebase.database.DataSnapshot;

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

    public static User parseSnapshot(DataSnapshot ds){
        User result = new User();
        for (DataSnapshot post: ds.getChildren()) {
            if(post.getKey().equals("friends")){
                for(DataSnapshot friend : post.getChildren()){
                    result.friends.add((String) friend.getValue());
                }
            }
            else if(post.getKey().equals("favorites")){
                for(DataSnapshot fav : post.getChildren()){
                    result.favorites.add((String) fav.getValue());
                }
            }
            else if(post.getKey().equals("groups")){
                for(DataSnapshot group : post.getChildren()){
                    result.groups.add((String) group.getValue());
                }
            }
            else if(post.getKey().equals("uid")){
                result.uid = (String) post.getValue();
            }
        }
        return result;
    }
}
