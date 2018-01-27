package com.siva.needred;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by MANIKANDAN on 13-08-2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    switch (position)
    {

        case 0: DonorListFragment donorListFragment=new DonorListFragment();
            return donorListFragment;
        case 1: NeedHelpFragment needHelpFragment=new NeedHelpFragment();
            return needHelpFragment;

        default:return  null;
    }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "DONORS";
            case 1:
                return "NEED HELP";
            default: return null;

        }

    }
}
