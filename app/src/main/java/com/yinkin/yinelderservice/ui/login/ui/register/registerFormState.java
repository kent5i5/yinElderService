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
    private Integer adddressError;
    private boolean isDataValid;

    registerFormState(@Nullable Integer usernameError, @Nullable Integer passwordError,
            @Nullable Integer addressError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.adddressError = addressError;
        this.isDataValid = false;
    }

    registerFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
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

    Integer getAdddressError() {
        return adddressError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}