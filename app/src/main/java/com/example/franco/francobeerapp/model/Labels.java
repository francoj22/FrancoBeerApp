package com.example.franco.francobeerapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Franco on 29/05/2017.
 */

public class Labels {

    @SerializedName("medium")
    @Expose
    private String medium;

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        if (medium == null) {
            this. medium = "no medium";
        } else {
            this.medium = medium;
        }
    }

}
