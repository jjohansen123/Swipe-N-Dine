package com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleman on 12/6/17.
 */

public class User {
    public String uid;
    public String name;
    public List<Friend> friends;
    public List<Restaurant> favorites;
    public List<String> groups;

    public User(){
        uid = "";
        name = "";
        friends = new ArrayList<>();
        favorites = new ArrayList<>();
        groups = new ArrayList<>();
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

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<Restaurant> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Restaurant> favorites) {
        this.favorites = favorites;
    }

    public static User parseSnapshot(DataSnapshot ds){
        User result = new User();

        if(ds == null){
            return result;
        }

        for (DataSnapshot post: ds.getChildren()) {
            if(post.getKey().equals("friends")){
                for(DataSnapshot friend : post.getChildren()){
                    result.friends.add(friend.getValue(Friend.class));
                }
            }
            else if(post.getKey().equals("favorites")){
                for(DataSnapshot fav : post.getChildren()){
                    result.favorites.add(fav.getValue(Restaurant.class));
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
            else if(post.getKey().equals("name")){
                result.name = (String) post.getValue();
            }
        }
        return result;
    }

    public boolean equals(Object other){
        if(other instanceof User){
            User u = (User) other;

            return(u.name.equals(this.name) && u.uid.equals(this.uid));
        }
        return false;
    }
}
