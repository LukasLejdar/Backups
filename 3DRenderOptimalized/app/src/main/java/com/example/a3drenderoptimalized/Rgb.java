package com.example.a3drenderoptimalized;

public class Rgb {
    int r = 255;
    int g = 255;
    int b = 255;

    Rgb() {

    }
    Rgb(int r, int g, int b) {
        // r, g, b cannot be over 255 and under 0
        r = Math.max(0, r); r = Math.min(255, r);
        g = Math.max(0, g); g = Math.min(255, g);
        b = Math.max(0, b); b = Math.min(255, b);

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setRGB (int r, int g, int b) {
        // r, g, b cannot be over 255 and under 0
        r = Math.max(0, r); r = Math.min(255, r);
        g = Math.max(0, g); g = Math.min(255, g);
        b = Math.max(0, b); b = Math.min(255, b);

        this.r = r;
        this.g = g;
        this.b = b;
    }
}
