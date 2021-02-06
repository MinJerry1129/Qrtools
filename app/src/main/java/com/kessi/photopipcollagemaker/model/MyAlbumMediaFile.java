package com.kessi.photopipcollagemaker.model;


import java.io.File;

public class MyAlbumMediaFile {
    private File mediaUri;

    public MyAlbumMediaFile(File mediaUri) {
        this.mediaUri = mediaUri;
    }

    public MyAlbumMediaFile(){

    }


    public File getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(File mediaUri) {
        this.mediaUri = mediaUri;
    }

}
