package com.yinkin.yinelderservice.data;

import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.MainActivity;
import com.yinkin.yinelderservice.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<ParseUser> login(String username, String password) {
        Log.i("user and password",username+" "+password);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null) {
                    Log.i("Info", "Logged in " + user.get("level"));
                    if(user.containsKey("level")){
                        //                        if (user.get("level").equals("Employee") && isEmployee){
                        //                            goToEmployeeListActivity();
                        //                        } else if(user.get("level").equals("Employer") && isEmployer) {
                        //                            goToUserListActivity();
                        //                        } else {
                        Log.i("Info", "Logged fail with incorrect level");
                        //Toast.makeText(MainActivity.this, "Please switch to " + user.get("level").toString()+ " mode", Toast.LENGTH_SHORT).show();
                        //ParseUser.logOut();
                        //}
                    }

                } else {
                    Log.i("Info", "Logged fail");
                }
            }
        });


        try {
            ParseUser parseUser = ParseUser.getCurrentUser();


                if (parseUser.getUsername().isEmpty()) {
                    throw new Exception();
                }
                // TODO: handle loggedInUser authentication

            return new Result.Success<>(parseUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        ParseUser.logOut();
    }
}