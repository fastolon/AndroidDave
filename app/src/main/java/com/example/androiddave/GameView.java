package com.example.androiddave;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;

public class GameView extends View implements View.OnTouchListener {
    private GameWorld world;
    private LevelLoader loader;
    private Context context;
    private Bitmap textures;



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        context = view.getContext();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int win = 0;
        Log.d(GameView.class.getName(), "TOUCH X=" + x + "; Y=" + y);
        Player p = world.getPlayer();
        int originX = p.getX();
        int originY = p.getY();
        if (y < (view.getHeight() / 3)) {

            if (p.getJumpForce() == 0) {
                p.move(0, Player.JUMP_POWER);
            }
        } else if (y > (view.getHeight() - (view.getHeight() / 3))) {
            p.setPosition(originX, originY + 10);
            ArrayList<GameObject> collisions = world.getCollisions(p);
            p.setPosition(originX, originY);
            boolean canMove = false;
            for (GameObject obj : collisions) {

                if (obj instanceof Stairs) {
                    canMove = true;
                }
                if ((obj instanceof Floor) || (obj instanceof Wall)) {
                    canMove = false;
                    break;
                }


            }
            if (canMove) {
                p.move(0, 16);
            }
        } else if (x < view.getWidth() / 3) {
            p.setPosition(originX - 10, originY);
            ArrayList<GameObject> collisions = world.getCollisions(p);
            p.setPosition(originX, originY);
            boolean canMove = true;
            for (GameObject obj : collisions) {
                if ((obj instanceof Wall) || (obj instanceof Floor)) {
                    canMove = false;
                }
                if (obj instanceof Nothing){
                    world.addObject(new TrueRubin(loader.getTrueRubyImage(), 60, 670,
                            Utils.pxFromDp(context, 8),
                            Utils.pxFromDp(context, 8)));
                }
                if (obj instanceof Ruby) {
                    int ox = obj.getX();
                    int oy = obj.getY();
                    world.removeObject(obj);
                    world.addObject(new Knife(loader.getKnifeImage(), ox - 70, oy, Utils.pxFromDp(context, 16), Utils.pxFromDp(context, 3)));
                }
                if (obj instanceof Door){
                    win = win +20;
                    if (win > 41){
                        world.addObject(new TrueRubin(loader.getTrueRubyImage(), 60, 670,
                                Utils.pxFromDp(context, 8),
                                Utils.pxFromDp(context, 8)));
                    }
                }
            }
            if (canMove) {
                p.move(-10, 0);
            }
        } else if (x > (view.getWidth() - (view.getWidth() / 3))) {
            p.setPosition(originX + 10, originY);
            ArrayList<GameObject> collisions = world.getCollisions(p);
            p.setPosition(originX, originY);
            boolean canMove = true;
            for (GameObject obj : collisions) {
                if ((obj instanceof Wall) || (obj instanceof Floor)) {
                    canMove = false;
                }
                if (obj instanceof Nothing){
                    world.addObject(new TrueRubin(loader.getTrueRubyImage(), 60, 670,
                            Utils.pxFromDp(context, 8),
                            Utils.pxFromDp(context, 8)));
                }
                if (obj instanceof Ruby) {
                    int ox = obj.getX();
                    int oy = obj.getY();
                    world.removeObject(obj);
                    world.addObject(new Knife(loader.getKnifeImage(), ox + 70, oy, Utils.pxFromDp(context, 16), Utils.pxFromDp(context, 3)));
                }
                if (obj instanceof Door){
                    win = win + 20;
                    if (win > 41){
                        world.addObject(new TrueRubin(loader.getTrueRubyImage(), 60, 600,
                                Utils.pxFromDp(context, 8),
                               Utils.pxFromDp(context, 8)));
                    }
                }
            }
            if (canMove) {
                p.move(10, 0);
            }
        }
        this.invalidate();
        return true;
    }

    public class Task extends java.util.TimerTask {
        private Context context;

        public Task(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    world.update();
                    GameView.this.invalidate();
                }
            });

        }
    }

    private Timer timer = new Timer();

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.textures = BitmapFactory.decodeResource(context.getResources(), R.drawable.egatiles);
        String[] level1 = {
                "====================================================",
                "#                                                   #",
                "#             P                                     #",
                "#                                                   #",
                "#                                                   #",
                "#         R                                  D    T #",
                "#        ===========================================#",
                "#                                                   #",
                "#                                                   #",
                "#                                                   #",
                "#                                                   #",
                "#                                                   #",
                "#    D      R                                       #",
                "#=======================                            #",
                "#                                                   #",
                "#                                                   #",
                "#                                                   #",
                "#                                                   #",
                "#                                                   #",
                "#          R   D                                    #",
                "#======{_}======                                ====#",
                "#      {_}                                          #",
                "#      {_}                                          #",
                "#      {_}                                          #",
                "#      {_}                                          #",
                "#      {_}                                           ",
                "#      {_}                                           ",
                "#      {_}   K                                R      ",
                "=============================================================",
        };

        loader = new LevelLoader(context);
        this.world = loader.load(level1);

        Task myTask = new Task(context);
        timer.scheduleAtFixedRate(myTask, 1, 33);

        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        world.draw(canvas);
        int px = world.getPlayer().getX();
        int py = world.getPlayer().getY();
        Paint textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(50);
        canvas.drawText("x: " + px + ", y: " + py, 50, 100, textPaint);
    }
}
