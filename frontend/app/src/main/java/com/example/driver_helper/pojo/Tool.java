package com.example.driver_helper.pojo;

import android.graphics.drawable.Drawable;

public class Tool {
    private String name;
    private int logo;


    public Tool(String name, int logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
