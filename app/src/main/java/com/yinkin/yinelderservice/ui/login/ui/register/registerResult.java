package com.yinkin.yinelderservice.ui.login.ui.register;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class registerResult {
    @Nullable
    private registeredInUserView success;
    @Nullable
    private Integer error;

    registerResult(@Nullable Integer error) {
        this.error = error;
    }

    registerResult(@Nullable registeredInUserView success) {
        this.success = success;
    }

    @Nullable
    registeredInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}