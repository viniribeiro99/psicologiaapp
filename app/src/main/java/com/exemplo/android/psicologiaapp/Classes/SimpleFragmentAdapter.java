package com.exemplo.android.psicologiaapp.Classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.exemplo.android.psicologiaapp.Fragments.ChatFragment;
import com.exemplo.android.psicologiaonline.Fragments.PsicologoProfileFragment;

public class SimpleFragmentAdapter extends FragmentPagerAdapter {
    public SimpleFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    private String tabTitles[] = new String[]{"Chat", "Perfil"};

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ChatFragment();
                break;

            case 1:
                fragment = new PsicologoProfileFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
