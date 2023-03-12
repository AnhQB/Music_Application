package com.example.musicapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicapplication.databinding.ActivityAddMusicBinding;
import android.app.MediaRouteActionProvider;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import android.os.storage.StorageManager;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddMusic extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView textViewImage;
    ProgressBar progressBar;
    Uri audioUri;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    DatabaseReference referenceSongs;
    String songCategory;
    MediaMetadataRetriever metadataRetriever;
    byte [] art;
    String title1,artist1,album_art1 = "", durations1;
    TextView title, artist, durations, album, dataa;
    ImageView album_art;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);
        textViewImage = findViewById(R.id.textViewSongFileSelected);
        progressBar = findViewById(R.id.progressBar);
        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        durations = findViewById(R.id.duration);
        album = findViewById(R.id.album);
        dataa = findViewById(R.id.data);

        metadataRetriever = new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("songs");

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();

        categories.add("Love songs");
        categories.add("Sad songs");
        categories.add("Party songs");
        categories.add("Birthday songs");
        categories.add("God songs");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item, categories);

        dataAdapter.setDropDownViewResource(com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        songCategory = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "Select:" + songCategory, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void openAudioView (View v) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/+");
        startActivityForResult(i, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data.getData() != null) {
            audioUri = data.getData();
            String fileNames = getFileName(audioUri);
            textViewImage.setText(fileNames);
            metadataRetriever.setDataSource(String.valueOf(this.audioUri));

            art = metadataRetriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
            album_art.setImageBitmap(bitmap);

            album.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            dataa.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            durations.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            title.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

            artist1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            durations1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
         }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri){
        String result = null;

        if(uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor != null && cursor.moveToNext()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally {
                cursor.close();
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf("/");
            if (cut != -1) {
                result = result.substring(cut+1);
            }
        }

        return result;
    }

    public void uploadFileToFireBase(View v) {
        if(textViewImage.equals("No file selected")) {
            Toast.makeText(this,"Please selcet image!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(this, "Songs uploads in allready preogress!",Toast.LENGTH_SHORT).show();
            }
            else {
                uploadFiles();
            }
        }
    }

    private void uploadFiles() {
        if(audioUri != null) {
            Toast.makeText(this,"Uploads please wait!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(audioUri));
            mUploadTask = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri audioUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }
}





















