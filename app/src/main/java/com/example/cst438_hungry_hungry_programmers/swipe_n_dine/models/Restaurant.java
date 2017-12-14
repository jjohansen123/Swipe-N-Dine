package com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models;

/**
 * Created by coleman on 12/14/17.
 */

public class Restaurant {
    public String name;
    public String url;

    public Restaurant(){
        name = "";
        url = "";
    }

    public Restaurant(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
