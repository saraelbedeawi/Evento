package com.sf.evento.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter  extends FragmentStatePagerAdapter
{
    private final List<Fragment> fragmentList= new ArrayList<>();
    private final List<String> fragmentListTitles= new ArrayList<>();

    public ViewPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }

    @Override
    public int getCount() {
        return fragmentListTitles.size();

    }
    public void AddFragment(Fragment fragment , String title)
    {
        fragmentList.add(fragment);
        fragmentListTitles.add(title);
    }
}
