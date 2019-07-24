package com.example.usedmarket;

import android.graphics.Bitmap;

public class PhotoBean {
    private Bitmap photo;

    public PhotoBean(Bitmap photo) {
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
