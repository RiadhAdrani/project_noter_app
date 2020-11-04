package com.example.noter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NoteFragmentPager extends FragmentPagerAdapter {

    private int size;
    private FragmentSelect fragmentSelect;

    public void FragmentSelect(FragmentSelect fragmentSelect){
        this.fragmentSelect = fragmentSelect;
    }

    public NoteFragmentPager(@NonNull FragmentManager fm, int size) {
        super(fm);

        this.size = size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentSelect.Selector(position);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentSelect.TitleSetter(position);
    }

    interface FragmentSelect{
        Fragment Selector(int position);
        CharSequence TitleSetter(int position);
    }
}
