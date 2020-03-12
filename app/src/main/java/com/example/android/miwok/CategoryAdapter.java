package com.example.android.miwok;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CategoryAdapter extends FragmentPagerAdapter {

    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new NumbersFragment();

            case 1:
                return new FamilyFragment();

            case 2:
                return new ColorsFragment();

            case 3:
                return new PhrasesFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Numbers";

            case 1:
                return "Family";

            case 2:
                return "Colors";

            case 3:
                return "Phrases";

            default:
                return " ";
        }
    }
}
