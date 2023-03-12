package com.example.musicapplication;

import android.app.MediaRouteActionProvider;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.storage.StorageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class AddMusicFragment extends Fragment {
    TextView textViewImage;
    ProgressBar proogressBar;
    Uri audioUri;
    StorageReference mStorageRef;
    StorageTask mStorageTask;
    DatabaseReference databaseReference;
    String songCategory;
    MediaMetadataRetriever mediaMetadataRetriever;
    byte [] art;
    String title1,artist1,album_art1 = "", durations1;
    TextView title, artist, durations;
    ImageView album_art;

}
