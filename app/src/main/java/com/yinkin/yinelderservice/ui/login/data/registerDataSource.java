package com.yinkin.yinelderservice.ui.login.data;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.yinkin.yinelderservice.ui.login.data.model.User;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class registerDataSource {
    ParseUser user = new ParseUser();
    public Result<ParseUser> register(final String username, final String password, final ParseGeoPoint address, final Boolean isEmployer) {
        Log.i("Parse tweet", username + password + isEmployer );
        user.setUsername(username);
        user.setPassword(password);
        try {
            // TODO: handle loggedInUser authentication
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Info", "Signed up");

                        if (isEmployer) {
                            ParseObject newEmployer = new ParseObject("Employer");
//                            newEmployee.put("username", username);
//                            newEmployee.put("password", password);
                            newEmployer.put("address", address);
                            newEmployer.put("level", "Employee");

                            newEmployer.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Parse employee signup", "successful");
                                    } else {
                                        Log.i("Parse tweet", "fail");
                                    }
                                }
                            });

                        } else if (isEmployer == false) {
                            ParseObject newEmployer = new ParseObject("Employee");
                            //newEmployer.put("username", username);
                            newEmployer.put("address", address);
                            newEmployer.put("level", "Employer");
                            newEmployer.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Parse employer signup", "successful");
                                    } else {
                                        Log.i("Parse tweet", "fail");
                                    }
                                }
                            });

                        }

                    } else {
                        Log.i("Info/", e.getMessage());
                    }

                }
            });

            user.logInInBackground(username, password);
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        ParseUser.logOut();
    }
}