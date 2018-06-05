package com.example.keiji.app.objects;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.keiji.app.activities.Rules.DoctorRules;
import com.example.keiji.app.activities.Rules.GeneralRules;
import com.example.keiji.app.activities.Rules.MafiaRules;
import com.example.keiji.app.activities.Rules.VilagerRules;

public class RulesPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public RulesPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                GeneralRules tab1 = new GeneralRules();
                return tab1;
            case 1:
                DoctorRules tab2 = new DoctorRules();
                return tab2;
            case 2:
                MafiaRules tab4 = new MafiaRules();
                return tab4;
            case 3:
                VilagerRules tab5 = new VilagerRules();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}