package com.yinkin.yinelderservice.ui.login.ui.register;

/**
 * Class exposing authenticated user details to the UI.
 */
class registeredInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    registeredInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}