package com.app.brensurio.iorder;

/**
 * Created by Mariz L. Maas on 10/21/2016.
 */

public interface MyFragmentCallback {

    void signUp(String email, String password, String displayName);
    void signIn(String email, String password);
}
