package com.yinkin.yinelderservice.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.data.LoginRepository;
import com.yinkin.yinelderservice.data.Result;
import com.yinkin.yinelderservice.data.model.LoggedInUser;
import com.yinkin.yinelderservice.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    ParseUser parseUser;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LoginViewModel(

    ){}

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
       // Result<ParseUser> result = loginRepository.login(username, password);

//        if (result instanceof Result.Success) {
//            ParseUser data = ((Result.Success<ParseUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUsername())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }

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
                        loginResult.setValue(new LoginResult(new LoggedInUserView(ParseUser.getCurrentUser().getUsername())));
                    }

                } else {
                    Log.i("Info", "Logged fail");
                    loginResult.setValue(new LoginResult(R.string.login_failed));

                }
            }
        });
    }


    public void logout(){
        ParseUser.logOut();
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

}