package com.yinkin.yinelderservice.ui.login.ui.register;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.yinkin.yinelderservice.ProfileActivity;
import com.yinkin.yinelderservice.R;

import java.io.IOException;
import java.util.List;
//import com.yinkin.yinelderservice.ui.login.R;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private registerViewModel registerViewModel;

    public ParseGeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        ParseGeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new ParseGeoPoint((double) (location.getLatitude() ),
                    (double) (location.getLongitude() ));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this, new registerViewModelFactory())
                .get(registerViewModel.class);

        final EditText usernameEditText = view.findViewById(R.id.registerUsername);
        final EditText passwordEditText = view.findViewById(R.id.registerPassword);
        final EditText addressEditText = view.findViewById(R.id.registerAddress);
        final CheckBox isEmployerCheck = view.findViewById(R.id.isEmployerCheckBox);
        final Button registerButton = view.findViewById(R.id.register);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        final ParseGeoPoint newUserGeoPoint = getLocationFromAddress(addressEditText.getText().toString());

        registerViewModel.getRegisterFormState().observe(getViewLifecycleOwner(), new Observer<registerFormState>() {
            @Override
            public void onChanged(@Nullable registerFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }

                if (loginFormState.getPasswordError() != null) {
                    addressEditText.setError(getString(loginFormState.getAddressError()));
                }
            }
        });

        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<registerResult>() {
            @Override
            public void onChanged(@Nullable registerResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), newUserGeoPoint , isEmployerCheck.isChecked());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.register(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), newUserGeoPoint, isEmployerCheck.isChecked());
                //NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.loginFragment);

            }
        });

        ConstraintLayout backgroundLayout = (ConstraintLayout) view.findViewById(R.id.registerContainer);
        backgroundLayout.setOnClickListener(this);
    }

    private void updateUiWithUser(registeredInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerContainer) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

        }
    }
}