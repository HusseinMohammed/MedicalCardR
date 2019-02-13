package com.hyper.design.medicalcard.loginRequest;

import com.hyper.design.medicalcard.User.User;

/**
 * Created by Hyper Design Hussien on 1/16/2018.
 */

interface GetUserCallback {

    public abstract void done(User returnedUser);
}
