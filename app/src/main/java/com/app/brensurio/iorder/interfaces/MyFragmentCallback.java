package com.app.brensurio.iorder.interfaces;

import com.app.brensurio.iorder.models.User;

public interface MyFragmentCallback {

    void signUp(String email, String password, User user);
    void signIn(String email, String password);
    void loading();
    void unload();
}
