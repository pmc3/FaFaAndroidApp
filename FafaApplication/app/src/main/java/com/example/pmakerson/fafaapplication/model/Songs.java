package com.example.pmakerson.fafaapplication.model;

/**
 * Created by P Makerson on 12/04/2016.
 */
public class Songs {
    private String songTitle;
    private String songArtistName;
    private String songImageUrl;

    private Songs() {
    }


    public Songs(String songTitle, String songArtistName, String songImageUrl) {
        setSongTitle(songTitle);
        setSongArtistName(songArtistName);
        setSongImageUrl(songImageUrl);
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtistName() {
        return songArtistName;
    }

    public void setSongArtistName(String songArtistName) {
        this.songArtistName = songArtistName;
    }

    public String getSongImageUrl() {
        return songImageUrl;
    }

    public void setSongImageUrl(String songImageUrl) {
        this.songImageUrl = songImageUrl;
    }
}
