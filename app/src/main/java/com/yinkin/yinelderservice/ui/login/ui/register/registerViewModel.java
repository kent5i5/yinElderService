package com.yinkin.yinelderservice.ui.login.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.R;
import com.yinkin.yinelderservice.ui.login.data.registerRepository;
import com.yinkin.yinelderservice.ui.login.data.Result;
import com.yinkin.yinelderservice.ui.login.data.model.User;
//import com.yinkin.yinelderservice.ui.login.R;

public class registerViewModel extends ViewModel {

    private MutableLiveData<registerFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<registerResult> registerResult = new MutableLiveData<>();
    private registerRepository registerRepository;

    registerViewModel(registerRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<registerFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<registerResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String username, String password, ParseGeoPoint address, boolean isEmployer) {
        // can be launched in a separate asynchronous job
        Result<ParseUser> result = registerRepository.register(username, password, address, isEmployer);

        if (result instanceof Result.Success) {
            ParseUser data = ((Result.Success<ParseUser>) result).getData();
            registerResult.setValue(new registerResult(new registeredInUserView(data.getUsername())));
        } else {
            //loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void registerDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new registerFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new registerFormState(null, R.string.invalid_password, null));
        } else {
            registerFormState.setValue(new registerFormState(true));
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