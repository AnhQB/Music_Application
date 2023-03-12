package com.example.musicapplication;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicapplication.databinding.ActivityAddMusicBinding;
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

public class AddMusic extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

    }

    @Override
    public boolean onSupportNavigateUp() {

        return false;
    }
}