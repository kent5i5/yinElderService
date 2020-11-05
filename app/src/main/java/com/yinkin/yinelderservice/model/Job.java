package com.yinkin.yinelderservice.model;

import java.util.Date;

public class Job {
    public final String jobid;
    public final Date created_date;
    public final String creator;
    public final String address;
    public final String title;

    public Job(String objectid, Date created_date, String username, String address, String title){
        this.jobid = objectid;
        this.created_date = created_date;
        this.creator = username;
        this.address = address;
        this.title = title;
    }

    public String getAddress(){
        return address;
    }

    public String getCreatorName(){
        return creator;
    }
    public String getJobId(){
        return jobid;
    }

    @Override
    public String toString() {
        return creator +" "+ address;
    }
}
