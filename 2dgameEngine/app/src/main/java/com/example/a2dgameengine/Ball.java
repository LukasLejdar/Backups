package com.example.a2dgameengine;

import android.util.Log;

import java.util.Random;

public class Ball {
    float x,y;
    float vx, vy;
    float ax, ay;
    float r;
    float drag = 0;
    float mass;
    int id;

    Random random = new Random();
    boolean collision = false;

    Ball(float x,float y, float r, int id, float mass) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.mass = r;
        this.id = id;

        vx = random.nextInt(1);
        vy = random.nextInt(1);
        ax = 0;
        ay = 0;
    }

    public void setPos(float touchX, float touchY) {
        x = touchX;
        y = touchY;
        vx = 0;
        vy = 0;
    }

    public void move() {
        vx += ax-vx*drag;
        vy += ay-vy*drag;
        x += vx;
        y += vy;

        if ((vx*vx + vy*vy) < 0.001) {
            vx = 0;
            vy = 0;
        }
    }

    public float nextVelX() {
        return vx += ax-vx*drag;
    }

    public float nextVelY() {
        return vy += ay-vy*drag;
    }

    public void subPos(float mx, float my) {
        x -= mx;
        y -= my;
    }

    public void addPos(float mx, float my) {
        x += mx;
        y += my;
    }
}
