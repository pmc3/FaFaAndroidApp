package com.example.pmakerson.fafaapplication.manager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by P Makerson on 24/03/2016.
 */
public class FragmentActionManager {

    private FragmentManager fragmentManager;

    public FragmentActionManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }


    public void addFragmentToContainer(Fragment fragment, int idContainer) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(idContainer, fragment);
            fragmentTransaction.commit();
        }
    }


    public void addAndBackStackFragmentToContainer(Fragment fragment, int idContainer, String fragmentTag) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(idContainer, fragment, fragmentTag);
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
        }
    }

}
