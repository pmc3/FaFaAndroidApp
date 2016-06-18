package com.example.pmakerson.fafaapplication.model;

/**
 * Created by P Makerson on 31/03/2016.
 */
public class Track {

    private Long trackId;
    private Long lyricId;
    private String trackLyricId;
    private String trackName;
    private String albumName;
    private String artistName;
    private String albumCoverUrl;
    private int trackLenght;

    private String firstReleaseDate;


    public Track() {
    }


    public Track(Long trackId, Long lyricId, String trackName, String albumName, String artistName, String albumCoverUrl, int trackLenght, String firstReleaseDate) {
        setTrackId(trackId);
        setLyricId(lyricId);
        setTrackName(trackName);
        setAlbumName(albumName);
        setArtistName(artistName);
        setAlbumCoverUrl(albumCoverUrl);
        setTrackLenght(trackLenght);
        setFirstReleaseDate(firstReleaseDate);
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Long getLyricId() {
        return lyricId;
    }

    public void setLyricId(Long lyricId) {
        this.lyricId = lyricId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public int getTrackLenght() {
        return trackLenght;
    }

    public void setTrackLenght(int trackLenght) {
        this.trackLenght = trackLenght;
    }

    public String getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(String firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public String getTrackLyricId() {
        return trackLyricId;
    }

    public void setTrackLyricId(String trackLyricId) {
        this.trackLyricId = trackLyricId;
    }
}
