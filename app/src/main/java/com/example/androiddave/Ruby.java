package com.example.androiddave;

import android.graphics.Bitmap;

public class Ruby extends GameObject {
    private int counter = 0;
    public Ruby(Bitmap[] images, int x, int y, int width, int height) {
        super(images, x, y, width, height);
    }

    @Override
    public Bitmap getImage() {
        if (counter % 2 == 0) {
            if (imageIndex == 0) {
                imageIndex = 1;
            } else {
                imageIndex = 0;
            }
        }
        counter++;
        return super.getImage();
    }
}
