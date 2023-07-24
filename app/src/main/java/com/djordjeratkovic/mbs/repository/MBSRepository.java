package com.djordjeratkovic.mbs.repository;

import android.app.Application;

public class MBSRepository {

    private static MBSRepository instance = null;
    private Application application = null;

    public static MBSRepository getInstance() {
        if (instance == null) {
            instance = new MBSRepository();
        }
        return instance;
    }

    public void setApplication(Application application) {
        if (this.application == null) {
            this.application = application;
        }
    }
}
