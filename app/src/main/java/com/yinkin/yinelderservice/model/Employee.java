package com.yinkin.yinelderservice.model;

public class Employee {

    public final String objectId;
    public final String username;
    public final String address;

    public Employee(String objectid, String username, String address){
        this.objectId = objectid;
        this.username = username;
        this.address = address;
    }

    @Override
    public String toString() {
        return username +" "+ address;
    }
}
