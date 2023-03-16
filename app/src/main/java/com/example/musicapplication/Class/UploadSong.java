package com.example.musicapplication.Class;

public class UploadSong {
    public String songsCategory, songTitle, artist, durations, songLink,mKey;

    public UploadSong(String songsCategory, String songTitle, String artist, String durations, String songLink) {

        if(songTitle.trim().equals("")) {
            songTitle = "No title";
        }

        this.songsCategory = songsCategory;
        this.songTitle = songTitle;
        this.artist = artist;
        this.durations = durations;
        this.songLink = songLink;
    }

    public UploadSong() {
    }

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDurations() {
        return durations;
    }

    public void setDurations(String durations) {
        this.durations = durations;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
