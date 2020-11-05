package com.yinkin.yinelderservice.adapter;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.model.Employer;

import java.util.ArrayList;
import java.util.List;

public class EmployerListViewModel extends ViewModel {
    private MutableLiveData<List<Employer>> employers;
    List<Employer> employerList;


    //Observer List of Employer data
    public MutableLiveData<List<Employer>> getEmployersAync() {
        if (employers == null) {
            employers = new MutableLiveData<List<Employer>>();
            loadUsers();
        }
        return employers;
    }
    public List<Employer> getLiveDataValues(){
        return employers.getValue();
    }

    //Simple getter for employer data
    public List<Employer> getEmployers(){
        //if (employers == null) {
        employerList = new ArrayList<>();
        ParseQuery<ParseObject> query= ParseQuery.getQuery("Employer");
        //query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.whereExists("username");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0 ) {
                        for (ParseObject object : objects) {
                            Log.i("",object.getObjectId()+object.getString("username")+ object.getString("address"));
                            Employer employer = new Employer( object.getObjectId(), object.getString("username"), object.getString("address"));
                            employerList.add(employer);
                        }
                    }
                    Log.i("Data"," found "+ employerList.size() );
                } else {
                    Log.i("Data","not found");
                }
            }
        });

        return employerList;
    }

    private void loadUsers( )  {
        employerList = new ArrayList<>();
        // perform an asynchronous operation to fetch users.
        ParseQuery<ParseObject> query= ParseQuery.getQuery("Employer");
        //query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.whereExists("username");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    for ( ParseObject object : objects) {
                        Employer employer = new Employer(object.getString("objectId"), object.getString("username"), object.getString("address"));
                        employerList.add(employer);
                    }
                    Log.i("Data"," found");
                } else {
                    Log.i("Data","not found");
                }
            }
        });
        Log.i("","Live Data finished loading");
    }
}
