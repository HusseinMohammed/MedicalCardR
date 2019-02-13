package com.hyper.design.medicalcard.TabHost;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hyper.design.medicalcard.Fragments.TabDoctorProfile;
import com.hyper.design.medicalcard.Fragments.TabReviews;

import static com.hyper.design.medicalcard.Activities.DoctorProfile.int_items;

/**
 * Created by dell on 29/12/2017.
 */

public class TabHostAdapter extends FragmentPagerAdapter {

    public TabHostAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TabDoctorProfile();
            case 1:
                return new TabReviews();
        }
        return null;
    }

    @Override
    public int getCount() {
        return int_items;
    }

    /**
     * This method returns the title of the tab according to the position.
     */
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                String profile = "Profile";
                return profile;
            case 1:
                String reviews = "Reviews";
                return reviews;
        }
        return null;
    }
}
