package com.example.androiddave;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GameWorld {
    private Context context;
    private Bitmap textures;
    private Bitmap background;
    private Player player;
    private GameWorld world;
    private LevelLoader loader;

    // Список игровых объектов
    private ArrayList<GameObject> objects = new ArrayList<>();

    public GameWorld(Context context) {
        this.context = context;
        this.textures = BitmapFactory.decodeResource(context.getResources(), R.drawable.egatiles);
        this.background = Bitmap.createBitmap(textures,
                Utils.pxFromDp(context, 0),
                Utils.pxFromDp(context, 179),
                Utils.pxFromDp(context, 16),
                Utils.pxFromDp(context, 16));
    }

    /**
     * Метод добавления объектов в игровой мир.
     * @param object -- Объект для добавления.
     */
    public void addObject(GameObject object) {
        if (object instanceof Player) {
            this.player = (Player) object;
            this.objects.add(0, player);
        } else {
            this.objects.add(object);
        }
    }

    public void removeObject(GameObject object) {
        this.objects.remove(object);
    }

    public Player getPlayer() {
        return this.player;
    }

    public ArrayList<GameObject> getCollisions(GameObject object) {
        ArrayList<GameObject> result = new ArrayList<>();
        for (GameObject obj : objects) {
            if ((object != obj) && Rect.intersects(object.getHitbox(), obj.getHitbox())) {
                result.add(obj);
            }
        }
        return result;
    }

    private final int GRAVITY = 20;

    public void update() {
        int win = 0;
        int x = player.getX();
        int y = player.getY();
        player.setPosition(x, y + GRAVITY + player.getJumpForce());
        ArrayList<GameObject> collisions = getCollisions(player);
        if (collisions.size() > 0) {
            GameObject floor = null;
            for (GameObject obj : collisions) {
                if (obj instanceof TrueRubin){
                    Intent i2 = new Intent(context.getApplicationContext(), MainActivity3.class);
                    i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(i2);
                    removeObject(obj);
                }

                if (obj instanceof Knife){
                   Intent i = new Intent(context.getApplicationContext(), MainActivity2.class);
                   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(i);
                    removeObject(obj);

                }else if ((obj instanceof Wall) || (obj instanceof Floor) || (obj instanceof Stairs)) {
                    if (obj.getHitbox().top > player.getHitbox().top) {
                        floor = obj;
                    }
                }
            }
            if (floor != null) {
                player.setPosition(x, y);
                player.setPosition(x, floor.getHitbox().top - Utils.pxFromDp(context, 33));
                player.move(0, 0);
            }
        } else {
            player.setPosition(x, y);
            player.move(0, GRAVITY + player.getJumpForce());
        }
        player.update();
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        for (int y = 0; y < canvas.getHeight(); y += Utils.pxFromDp(context, 16)) {
            for (int x = 0; x < canvas.getWidth(); x += Utils.pxFromDp(context, 16)) {
                canvas.drawBitmap(this.background, x, y, p);
            }
        }

        Paint hitBoxPaint = new Paint();
        hitBoxPaint.setStyle(Paint.Style.STROKE);
        hitBoxPaint.setColor(Color.GREEN);
        for (int idx = 1; idx < objects.size(); idx++) {
            GameObject obj = objects.get(idx);
            canvas.drawBitmap(obj.getImage(), obj.getX(), obj.getY(), p);
            canvas.drawRect(obj.getHitbox(), hitBoxPaint);
        }
        canvas.drawBitmap(player.getImage(), player.getX(), player.getY(), p);
        canvas.drawRect(player.getHitbox(), hitBoxPaint);
    }
}
