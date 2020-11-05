package com.yinkin.yinelderservice.ui.login.data;

import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.yinkin.yinelderservice.MainActivity;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class registerRepository {

    private static volatile registerRepository instance;

    private registerDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private ParseUser user;

    // private constructor : singleton access
    private registerRepository(registerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static registerRepository getInstance(registerDataSource dataSource) {
        if (instance == null) {
            instance = new registerRepository(dataSource);
        }
        return instance;
    }

    public boolean isregisteredIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setRegisteredInUser(ParseUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<ParseUser> login(String username, String password) {
        // handle register
       // Result<ParseUser> result = dataSource.login(username, password);
//        if (result instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<ParseUser>) result).getData());
//        }
        return null;
    }

    public Result<ParseUser> register(final String username, String password, final String address, boolean isEmployer){
        user = new ParseUser();
        Result<ParseUser> result = dataSource.register(username, password, address, isEmployer);
        if (result instanceof  Result.Success){
            setRegisteredInUser(((Result.Success<ParseUser>) result).getData());
        }
        return result;
    }
}