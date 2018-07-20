package me.caelumterrae.fbunewsapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);

        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if (position != 0) {
            return fragments.get(position - 1);
        }else{
            return fragments.get(fragments.size()-1);
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
