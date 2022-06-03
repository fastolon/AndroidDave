package com.example.androiddave;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LevelLoader {
    private Bitmap nothingImage;
    private Context context;
    private Bitmap textures;
    private Bitmap wallImage;
    private Bitmap floorImage;
    private Bitmap doorImage;
    private Bitmap playerImage;
    private Bitmap playerImagePart;
    private Bitmap[] rubyImages;
    private Bitmap rubyImage;

    private Bitmap stairsLeft;
    private Bitmap stairsRight;
    private Bitmap stairsMiddle;

    private Bitmap knifeImage;

    public Bitmap getKnifeImage(){
        return knifeImage;
    }
    public Bitmap[] getTrueRubyImage(){
        return rubyImages;
    }

    public LevelLoader(Context context) {
        this.context = context;
        this.textures = BitmapFactory.decodeResource(context.getResources(), R.drawable.egatiles);
        this.wallImage = Bitmap.createBitmap(
                textures,
                Utils.pxFromDp(context, 144),
                Utils.pxFromDp(context, 80),
                Utils.pxFromDp(context, 16),
                Utils.pxFromDp(context, 16));
        this.floorImage = Bitmap.createBitmap(
                textures,
                Utils.pxFromDp(context, 189),
                Utils.pxFromDp(context, 48),
                Utils.pxFromDp(context, 16),
                Utils.pxFromDp(context, 16));
        this.doorImage = Bitmap.createBitmap(
                textures,
                Utils.pxFromDp(context, 64),
                Utils.pxFromDp(context, 304),
                Utils.pxFromDp(context, 32),
                Utils.pxFromDp(context, 48));
        this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.s_dave_fixed1);

        this.rubyImages = new Bitmap[2];
        this.rubyImages[0] = Bitmap.createBitmap(playerImage,
                Utils.pxFromDp(context, 1339),
                Utils.pxFromDp(context, 7),
                Utils.pxFromDp(context, 9),
                Utils.pxFromDp(context, 8));
        this.rubyImages[1] = Bitmap.createBitmap(playerImage,
                Utils.pxFromDp(context, 1355),
                Utils.pxFromDp(context, 7),
                Utils.pxFromDp(context, 9),
                Utils.pxFromDp(context, 8));
        this.stairsLeft = Bitmap.createBitmap(textures,
                Utils.pxFromDp(context,48),
                Utils.pxFromDp(context,176),
                Utils.pxFromDp(context,16),
                Utils.pxFromDp(context,16));
        this.stairsRight = Bitmap.createBitmap(textures,
                Utils.pxFromDp(context,64),
                Utils.pxFromDp(context,176),
                Utils.pxFromDp(context,16),
                Utils.pxFromDp(context,16));
        this.stairsMiddle = Bitmap.createBitmap(textures,
                Utils.pxFromDp(context,56),
                Utils.pxFromDp(context,176),
                Utils.pxFromDp(context,16),
                Utils.pxFromDp(context,16));
        this.knifeImage = Bitmap.createBitmap(textures,
                Utils.pxFromDp(context,96),
                Utils.pxFromDp(context,156),
                Utils.pxFromDp(context,16),
                Utils.pxFromDp(context,3));
        this.nothingImage = Bitmap.createBitmap(textures,
                Utils.pxFromDp(context,1),
                Utils.pxFromDp(context,1),
                Utils.pxFromDp(context,1),
                Utils.pxFromDp(context,1));

    }

    private Player createPlayer(int x, int y) {
        Bitmap[] images = new Bitmap[18];
        for (int idx = 0; idx < 18; idx++) {
            images[idx] = Bitmap.createBitmap(
                    playerImage,
                    Utils.pxFromDp(context, idx * 32),
                    Utils.pxFromDp(context, 32),
                    Utils.pxFromDp(context, 32),
                    Utils.pxFromDp(context, 32));
        }
        return new Player(
                images,
                x,
                y - Utils.pxFromDp(context, 16),
                Utils.pxFromDp(context,32),
                Utils.pxFromDp(context, 32));
    }

    private void loadObject(GameWorld world, char ch, int x, int y) {
        switch (ch) {
            case 'T':

                world.addObject(new Nothing(nothingImage, 950, 166,
                        Utils.pxFromDp(context, 1),
                        Utils.pxFromDp(context, 1)));
                break;
            case 'R':
                world.addObject(new Ruby(rubyImages, x, y,
                        Utils.pxFromDp(context, 8),
                        Utils.pxFromDp(context, 8)));
                break;
            case '#':
                world.addObject(
                        new Wall(
                            wallImage,
                                x,
                                y,
                                Utils.pxFromDp(context, 16),
                                Utils.pxFromDp(context, 16)));
                break;
            case '=':
                world.addObject(new Floor(floorImage, x, y,
                        Utils.pxFromDp(context, 16), Utils.pxFromDp(context, 16)));
                break;
            case 'D':
                world.addObject(new Door(doorImage, x, y - 86,
                        Utils.pxFromDp(context, 32), Utils.pxFromDp(context, 48)));
                break;
            case 'P':
                world.addObject(createPlayer(x, y));
                break;
            case '{':
                world.addObject(new StairsLeft(stairsLeft, x,y, Utils.pxFromDp(context,16), Utils.pxFromDp(context,16)));
                break;
            case '_':
                world.addObject(new StairsMiddle(stairsMiddle, x,y, Utils.pxFromDp(context,16), Utils.pxFromDp(context,16)));
                break;
            case '}':
                world.addObject(new StairsRight(stairsRight, x,y, Utils.pxFromDp(context,16), Utils.pxFromDp(context,16)));
                break;
            case 'K':
                world.addObject(new Knife(knifeImage,x,y,Utils.pxFromDp(context, 16), Utils.pxFromDp(context,3)));
                break;
        }
    }

    public GameWorld load(String[] level) {

        Bitmap candleImage = Bitmap.createBitmap(textures,
                Utils.pxFromDp(context, 176),
                Utils.pxFromDp(context, 192),
                Utils.pxFromDp(context, 16),
                Utils.pxFromDp(context, 16));
        GameWorld world = new GameWorld(context);
        for (int y = 0; y < level.length; y++) {
            String row = level[y];
            for (int x = 0; x < row.length(); x++) {
                loadObject(world, row.charAt(x),
                        Utils.pxFromDp(context, x * 16),
                        Utils.pxFromDp(context, y * 16));
            }
        }

        return world;
    }
}
