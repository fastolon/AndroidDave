package com.example.androiddave;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class GameObject {
    protected int x;
    protected int y;
    private Bitmap[] images;
    protected int imageIndex = 0;
    protected Rect hitbox;

    public GameObject(Bitmap[] images, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.images = images;
        this.hitbox = new Rect(x, y, x + width, y + height);
    }

    public GameObject(Bitmap image, int x, int y, int width, int height) {
        this(new Bitmap[]{ image }, x, y, width, height);
    }

    /**
     * Метод получения области столкновения объекта.
     * @return Область столкновения как экземпляр класса Rect.
     */
    public Rect getHitbox() {
        return this.hitbox;
    }

    /**
     * Метод получения изображения для отрисовки.
     * @return изображение для отрисовки.
     */
    public Bitmap getImage() {
        return this.images[imageIndex];
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Смещение объекта на указанные дельты X, Y
     * @param dx
     * @param dy
     */
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
        this.hitbox.offset(dx, dy);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.hitbox.offsetTo(x, y);
    }
}
