package com.yinkin.yinelderservice.ui.login.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class registerFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer addressError;

    private boolean isDataValid;

    registerFormState(@Nullable Integer usernameError, @Nullable Integer passwordError,
            @Nullable Integer addressError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.addressError = addressError;
        this.isDataValid = false;
    }

    registerFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.addressError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getAddressError() {
        return addressError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}