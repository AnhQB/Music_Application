package com.example.musicapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListMusicFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String[] mParam1;
    //private String mParam2;
    private String[] items;
    private ListView listView;

    private LinkedList<File> mySongs;

    public ListMusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
    //* @param param2 Parameter 2.
     * @return A new instance of fragment ListMusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListMusicFragment newInstance(String[] param1, LinkedList<File> param2) {
        ListMusicFragment fragment = new ListMusicFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        listView = getView().findViewById(R.id.listMusicLocal);
//        displaySong();
//        return inflater.inflate(R.layout.fragment_list_music, container, false);

        View view = inflater.inflate(R.layout.fragment_list_music, container, false);
        listView = view.findViewById(R.id.listMusicLocal);
        if (getArguments() != null) {
            items = getArguments().getStringArray(ARG_PARAM1);
            if (items != null && items.length > 0) {
                displaySong();
            }
            mySongs = (LinkedList<File>) getArguments().getSerializable(ARG_PARAM2);
        }
        return view;
    }

    public void displaySong(){
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName = (String) items[position];
                 //MusicPlayFragment musicPlayFragment = new MusicPlayFragment();
                 //musicPlayFragment = MusicPlayFragment.newInstance(songName,mySongs);

//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.view_paper, musicPlayFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                ArrayList<File> songs = new ArrayList<>();
                songs.addAll(mySongs);
                startActivity(new Intent(getContext().getApplicationContext(), MusicPlayFragment.class)
                        .putExtra("song", songs)
                        .putExtra("songName",songName)
                        .putExtra("position", position));

            }
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView txtSong = view.findViewById(R.id.txtSong);
            txtSong.setSelected(true);
            txtSong.setText(items[position]);
            return view;
        }
    }
}