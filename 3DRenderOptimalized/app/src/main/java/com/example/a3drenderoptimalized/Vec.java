package com.example.a3drenderoptimalized;

public class Vec {
    public float x = 0;
    public float y = 0;
    public float z = 0;
    public float w = 1;

    Vec() {

    }
    Vec(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    Vec(Vec v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void setVec(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}