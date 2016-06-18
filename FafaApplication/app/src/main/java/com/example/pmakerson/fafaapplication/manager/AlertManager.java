package com.example.pmakerson.fafaapplication.manager;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

/**
 * Created by P Makerson on 23/03/2016.
 */
public class AlertManager {

    private CoordinatorLayout coordinatorLayout;

    public AlertManager(CoordinatorLayout coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }

    public void showSnacbarMessage(String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
