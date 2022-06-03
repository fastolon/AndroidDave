package com.example.androiddave;

import android.graphics.Bitmap;

public class Player extends GameObject {
    /**
     * Возможные состояния игрока.
     */
    public enum State {
        STANDING_LEFT,
        STANDING_RIGHT,
        RUNNING_LEFT,
        RUNNING_RIGHT,
        FALLING_LEFT,
        FALLING_RIGHT,
        JUMPING_LEFT,
        JUMPING_RIGHT
    }

    private final static int INERTIA = 5;
    public final static int JUMP_POWER = -30;
    int runningCounter = 0;
    private int jumpForce = 0;

    /**
     * Состояние игрока.
     */
    private State state = State.STANDING_RIGHT;

    public Player(Bitmap[] images, int x, int y, int width, int height) {
        super(images, x, y, width, height);
    }

    public int getJumpForce() {
        return jumpForce;
    }

    public void update() {
        switch (state) {
            case RUNNING_LEFT:
                runningCounter--;
                if (runningCounter == 0) {
                    state = State.STANDING_LEFT;
                    imageIndex = 8;
                }
                break;

            case RUNNING_RIGHT:
                runningCounter--;
                if (runningCounter == 0) {
                    state = State.STANDING_RIGHT;
                    imageIndex = 0;
                }
                break;
        }
        if (jumpForce < 0) {
            jumpForce++;
        }
    }

    public void move(int dx, int dy) {

        switch (state) {
            case STANDING_RIGHT:
            case STANDING_LEFT:
                super.move(dx,dy);
                if (dy > 0) {
                    if (state == State.STANDING_LEFT) {
                        state = State.FALLING_LEFT;
                        imageIndex = 15;
                    } else {
                        state = State.FALLING_RIGHT;
                        imageIndex = 7;
                    }
                }
                if (dy < 0) {
                    if (state == State.STANDING_LEFT) {
                        state = State.JUMPING_LEFT;
                        imageIndex = 13;
                    } else {
                        state = State.JUMPING_RIGHT;
                        imageIndex = 5;
                    }
                    jumpForce = dy;
                }
                if (dx > 0) {
                    state = State.RUNNING_RIGHT;
                    runningCounter = INERTIA;
                    imageIndex = 1;
                }
                if (dx < 0) {
                    state = State.RUNNING_LEFT;
                    runningCounter = INERTIA;
                    imageIndex = 9;
                }
                break;

            case RUNNING_RIGHT:
                super.move(dx,dy);
                if (dy > 0) {
                    state = State.FALLING_RIGHT;
                    imageIndex = 7;
                }
                if (dy < 0) {
                    state = State.JUMPING_RIGHT;
                    imageIndex = 5;
                    jumpForce = dy;
                }
                if (dx < 0) {
                    state = State.RUNNING_LEFT;
                    imageIndex = 9;
                } else if (dx == 0) {
                    state = State.STANDING_RIGHT;
                    imageIndex = 0;
                } else {
                    imageIndex++;
                    if (imageIndex == 5) {
                        imageIndex = 1;
                    }
                }
                runningCounter = INERTIA;
                break;
            case RUNNING_LEFT:
                super.move(dx,dy);
                if (dy > 0) {
                    state = State.FALLING_LEFT;
                    imageIndex = 15;
                }
                if (dy < 0) {
                    state = State.JUMPING_LEFT;
                    imageIndex = 13;
                    jumpForce = dy;
                }
                if (dx > 0) {
                    state = State.RUNNING_RIGHT;
                    imageIndex = 1;
                } else if (dx == 0) {
                    state = State.STANDING_LEFT;
                    imageIndex = 8;
                } else {
                    imageIndex++;
                    if (imageIndex == 13) {
                        imageIndex = 9;
                    }
                }
                runningCounter = INERTIA;
                break;

            case FALLING_LEFT:
                if (dy > 0) {
                    super.move(0,dy);
                }

                imageIndex = 15;
                if ((dy == 0) && (dx == 0)) {
                    state = State.STANDING_LEFT;
                    imageIndex = 8;
                }
                break;

            case FALLING_RIGHT:
                if (dy > 0) {
                    super.move(0,dy);
                }
                imageIndex = 7;
                if ((dy == 0) && (dx == 0)) {
                    state = State.STANDING_RIGHT;
                    imageIndex = 0;
                }
                break;

            case JUMPING_LEFT:
                super.move(0,dy);
                if ((dy >= 0) && ( dx == 0)) {
                    state = State.FALLING_LEFT;
                    imageIndex = 14;
                }
                break;

            case JUMPING_RIGHT:
                super.move(0,dy);
                if ((dy >= 0) && ( dx == 0)) {
                    state = State.FALLING_RIGHT;
                    imageIndex = 6;
                }
                break;
        }
    }
}
