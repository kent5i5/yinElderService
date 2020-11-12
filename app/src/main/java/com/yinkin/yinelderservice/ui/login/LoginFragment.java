package com.yinkin.yinelderservice.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yinkin.yinelderservice.EmployerListActivity;
import com.yinkin.yinelderservice.ProfileActivity;
import com.yinkin.yinelderservice.R;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = view.findViewById(R.id.registerUsername);
        final EditText passwordEditText = view.findViewById(R.id.registerPassword);
        final Button loginButton = view.findViewById(R.id.login);
        final Button gotoRegisterButton = view.findViewById(R.id.gotoregister);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        if (isCashedUserInSQLlite()){
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        }

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    storeDataInSQLlite(usernameEditText.getText().toString(), passwordEditText.getText().toString() );
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        gotoRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
           // NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_jobFragment2);

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
    public boolean isCashedUserInSQLlite(){
        try {
            SQLiteDatabase yinDatabase = getActivity().openOrCreateDatabase("Users", MODE_PRIVATE, null);
            Cursor sqlcursor = yinDatabase.rawQuery("SELECT * FROM users", null);

            int nameIndex = sqlcursor.getColumnIndex("username");
            //int passwordIndex = sqlcursor.getColumnIndex("password");
            sqlcursor.moveToFirst();
            while (!sqlcursor.isAfterLast()) {
                Log.i("username", sqlcursor.getString(nameIndex));
                //Log.i("password", sqlcursor.getString(passwordIndex));
                sqlcursor.moveToNext();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void storeDataInSQLlite(String loggedinUsername, String LoggedInPassword){
        try {
            SQLiteDatabase yinDatabase = getActivity().openOrCreateDatabase("Users", MODE_PRIVATE, null);
            yinDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (username VARCHAR, password VARCHAR)");
            yinDatabase.execSQL("INSERT OR REPLACE INTO users (username, password) VALUES ('"
                    + loggedinUsername
                    + "', '" + LoggedInPassword + "')");


            Cursor sqlcursor = yinDatabase.rawQuery("SELECT * FROM users", null);

            int nameIndex = sqlcursor.getColumnIndex("username");
            //int passwordIndex= sqlcursor.getColumnIndex("password");
            sqlcursor.moveToFirst();
            while (!sqlcursor.isAfterLast()) {
                Log.i("username", sqlcursor.getString(nameIndex));
                //Log.i("password",sqlcursor.getString(passwordIndex));
                sqlcursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}