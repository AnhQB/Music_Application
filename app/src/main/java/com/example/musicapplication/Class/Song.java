package com.example.musicapplication.Class;

public class Song {
    private String artist;
    private String durations;
    private String songLink;
    private String songTitle;
    private String songsCategory;

    public Song() {
    }

    public Song(String artist, String durations, String songLink, String songTitle, String songsCategory) {
        this.artist = artist;
        this.durations = durations;
        this.songLink = songLink;
        this.songTitle = songTitle;
        this.songsCategory = songsCategory;
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

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }
}
