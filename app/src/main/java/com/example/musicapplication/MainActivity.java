package com.example.musicapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ListMusicFragment listMusicFragment;
    private MusicPlayFragment musicPlayFragment;
    private PlayListFragment playListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_paper);
        tabLayout = findViewById(R.id.tab_Layout);

        listMusicFragment = new ListMusicFragment();
        musicPlayFragment = new MusicPlayFragment();
        playListFragment = new PlayListFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPaperAdapter viewPaperAdapter = new ViewPaperAdapter(getSupportFragmentManager(), 0);

        viewPaperAdapter.addFragment(listMusicFragment, "");
        viewPaperAdapter.addFragment(musicPlayFragment, "");
        viewPaperAdapter.addFragment(playListFragment, "");

        viewPager.setAdapter(viewPaperAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_music);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_play);
        BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        badgeDrawable.setVisible(true);
        //badgeDrawable.setNumber(5);


    }

    private class ViewPaperAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        public ViewPaperAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}