package com.kuo.kuoresume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.kuo.kuoresume.common.adapter.ViewPagerAdapter;
import com.kuo.kuoresume.common.view.SlidingTabLayout;
import com.kuo.kuoresume.fragments.AboutFragment;
import com.kuo.kuoresume.fragments.ExperienceFragment;
import com.kuo.kuoresume.fragments.SkillFragment;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/30.
 */
public class ResumeActivity extends AppCompatActivity {

    private static final String[] title = {"About", "Skill", "Works"};

    private ViewPagerAdapter viewPagerAdapter;

    private SlidingTabLayout slidingTabLayout;

    private ViewPager viewPager;

    ArrayList<String> titles = new ArrayList<>();

    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        createTitles();
        createFragments();

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(titles, fragments, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    private void createTitles() {

        for(int i = 0 ; i < title.length ; i++)
            titles.add(title[i]);

    }

    private void createFragments() {

        fragments.add(new AboutFragment());
        fragments.add(new SkillFragment());
        fragments.add(new ExperienceFragment());

    }

}
