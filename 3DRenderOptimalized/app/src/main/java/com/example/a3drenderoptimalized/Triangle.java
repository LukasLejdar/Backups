package com.example.a3drenderoptimalized;

import java.util.Arrays;

public class Triangle {
    public Vec[] v = new Vec[3];
    Rgb rgb = new Rgb();
    int lumcolor = 0;
    Vec normal = new Vec();
    float height = 0;

    Triangle() {
        v[0] = new Vec();
        v[1] = new Vec();
        v[2] = new Vec();
    }
    Triangle(Vec v0, Vec v1, Vec v2) {
        v[0] = v0;
        v[1] = v1;
        v[2] = v2;
    }
    Triangle(Triangle t) {
        v[0] = new Vec(t.v[0]);
        v[1] = new Vec(t.v[1]);
        v[2] = new Vec(t.v[2]);
        normal = t.normal;
        lumcolor = t.lumcolor;
        rgb.r = t.rgb.r;
        rgb.g = t.rgb.g;
        rgb.b = t.rgb.b;
    }
}
