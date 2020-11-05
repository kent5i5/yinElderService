package com.yinkin.yinelderservice.model;

import androidx.lifecycle.ViewModel;

public class Employer {

    public final String objectId;
    public final String username;
    public final String address;

    public Employer(String objectid, String username, String address){
        this.objectId = objectid;
        this.username = username;
        this.address = address;
    }

    @Override
    public String toString() {
        return username +" "+ address;
    }
}
