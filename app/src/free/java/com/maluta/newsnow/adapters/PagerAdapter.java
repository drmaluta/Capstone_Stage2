package com.maluta.newsnow.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maluta.newsnow.fragments.AllNewsFragmentFree;
import com.maluta.newsnow.fragments.FavoritesFragment;
import com.maluta.newsnow.fragments.TopNewsFragmentFree;


/**
 * Created by admin on 9/10/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    int numberOfTabs;


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TopNewsFragmentFree();
            case 1:
                return new AllNewsFragmentFree();
            case 2:
                return new FavoritesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
