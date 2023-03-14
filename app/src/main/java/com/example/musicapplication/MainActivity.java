package com.example.musicapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.musicapplication.Class.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ListMusicFragment listMusicFragment;
    private MusicPlayFragment musicPlayFragment;
    private PlayListFragment playListFragment;
    private MenuFragment menuFragment;

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private ValueEventListener valueEventListener;
    private List<Song> mupload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mupload = new ArrayList<>();

        runtimePermission();
        getSongsOnline();





        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.view_paper);
        tabLayout = findViewById(R.id.tab_Layout);
        menuFragment = new MenuFragment();
        //listMusicFragment = new ListMusicFragment();
        //musicPlayFragment = new MusicPlayFragment();
        playListFragment = new PlayListFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPaperAdapter viewPaperAdapter = new ViewPaperAdapter(getSupportFragmentManager(), 0);


        viewPaperAdapter.addFragment(listMusicFragment, "");
        //viewPaperAdapter.addFragment(musicPlayFragment, "");
        viewPaperAdapter.addFragment(playListFragment, "");
        viewPaperAdapter.addFragment(menuFragment, "");

        viewPager.setAdapter(viewPaperAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_music);

        tabLayout.getTabAt(2).setIcon(R.drawable.icon_play);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_menu_24);

        //tabLayout.getTabAt(2).setIcon(R.drawable.icon_play);

        BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        badgeDrawable.setVisible(true);
        //badgeDrawable.setNumber(5);


    }

    private void getSongsOnline() {
        db =  FirebaseDatabase.getInstance();
        ref = db.getReference("songs");
        valueEventListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mupload.clear();
                for(DataSnapshot dss : snapshot.getChildren()){
                    Song song = dss.getValue(Song.class);
                    //song.setmKey(dss.getKey());
                    mupload.add(song);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySong();
                        //Log.d("abc","a");
                        //LinkedList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
//                        FindMusicTask task = new FindMusicTask();
//                        task.execute();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public LinkedList<File> findSong(File file){
        LinkedList<File> arrayList = new LinkedList<>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(findSong(singleFile));
                } else if (singleFile.isFile() && !singleFile.isHidden()) {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        arrayList.add(singleFile);
                    }
                }
            }
        }

        return arrayList;
    }

    public void displaySong(){
        File file = Environment.getExternalStorageDirectory();
//        if (file.exists()) {
//            LinkedList<File> songsList = findSong(file);
//        }
        final LinkedList<File> mySongs = findSong(file);
        String[] items = new String[mySongs.size()];
        for(int i = 0; i < mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "")
                    .replace(".wav", "");
        }
        listMusicFragment = ListMusicFragment.newInstance(items, mySongs);
        getSupportFragmentManager().beginTransaction()
                .commit();
    }
}