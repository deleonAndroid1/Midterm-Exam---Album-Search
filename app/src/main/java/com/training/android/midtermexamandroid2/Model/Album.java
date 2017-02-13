package com.training.android.midtermexamandroid2.Model;


public class Album {

    public String Album;
    public String Artist;
    public String imgUrl;

    public Album(String album, String artist,String imageUrl) {
        Album = album;
        Artist = artist;
        imgUrl = imageUrl;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
