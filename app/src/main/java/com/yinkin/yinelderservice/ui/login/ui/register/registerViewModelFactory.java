package com.yinkin.yinelderservice.ui.login.ui.register;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.yinkin.yinelderservice.ui.login.data.registerDataSource;
import com.yinkin.yinelderservice.ui.login.data.registerRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class registerViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(registerViewModel.class)) {
            return (T) new registerViewModel(registerRepository.getInstance(new registerDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}