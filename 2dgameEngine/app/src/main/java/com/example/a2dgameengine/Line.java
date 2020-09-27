package com.example.a2dgameengine;

import android.util.Log;

public class Line {
    float x1, y1;
    float x2, y2;
    boolean col;
    float[] line;

    float gx, gy;
    float dx, dy;
    float len, rot;
    float sin, cos;

    Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        line = new float[] {x1, y1, x2, y2};

        gx = x1+(x2-x1)/2;
        gy = y1+(y2-y1)/2;

        dx = Math.abs(x1-x2);
        dy = Math.abs(y1-y2);
        len = (float) Math.sqrt(dx*dx + dy*dy);
        if (y1-y2 > 0) {
            rot = -(float) Math.atan2(dy, dx);
        } else {
            rot = (float) Math.atan2(dy, dx);
        }
        sin = (float) Math.sin(rot);
        cos = (float) Math.cos(rot);

        Log.d("collision", "len: " + len);
        Log.d("collision", "rot: " + rot);
        Log.d("collision", "sin: " + sin);
        Log.d("collision", "cos: " + cos);
    }

}
