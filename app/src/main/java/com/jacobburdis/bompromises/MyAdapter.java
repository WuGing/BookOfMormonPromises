package com.jacobburdis.bompromises;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

/**
 * Created by joshua on 5/2/14.
 */
class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    public MyAdapter(Fragment fragment){
        super(fragment.getChildFragmentManager());
    }

    @Override
    public ListFragment getItem(int arg0)
    {
        final String CATEGORY_STRING = "category_string";
        final String CATEGORY_INT = "category_int";
        ListFragment fragment = null;
        Bundle args = null;

        if ( arg0 == 0 )
        {
            fragment = new FragmentCategoryList("favorite", arg0);
        }
        if (arg0 == 1)
        {
            fragment = new FragmentCategoryList("category", arg0);
        }
        if (arg0 == 2)
        {
            fragment = new FragmentCategoryList("blessings", arg0);
        }
        if (arg0 == 3)
        {
            fragment = new FragmentCategoryList("chronological", arg0);
        }
        return fragment;
    }

    @Override
    public int getCount(){
        return 4;
    }
}