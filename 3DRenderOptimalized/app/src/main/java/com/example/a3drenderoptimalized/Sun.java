package com.example.a3drenderoptimalized;

import android.content.Context;
import android.util.Log;

import java.util.Vector;

public class Sun {
    Functions f = new Functions();
    public Vector<Triangle> triangles;
    public Vector<Triangle> transformed = new Vector<Triangle>();
    Vec pos = new Vec();
    float radius = 0;
    Context context;

    Rgb centerColor = new Rgb(255, 255, 255);
    Rgb edgeColor = new Rgb(255, 239, 64);

    int cColor;
    int eColor;
    int lightRadius = 300;

    Sun(Context context, float radius, float x, float y, float z) {
        this.context = context;
        this.radius = radius;
        triangles = f.loadFromObjectFile("sphere.txt", context);
        pos.setVec(x,y,z);

        for (Triangle t: triangles) {
            t.v[0] = f.vecToSize(t.v[0], radius);
            t.v[1] = f.vecToSize(t.v[1], radius);
            t.v[2] = f.vecToSize(t.v[2], radius);
            transformed.add(t);
        }

        transformed = sunTransform(triangles, x,y,z);

        cColor = f.getColour(centerColor);
        eColor = f.getColour(edgeColor);
    }

    public void colorSun(Vec dir) {
        dir = f.normalizeVec(dir);

        for (Triangle t: transformed) {
            float dp = Math.max(0, f.dotProductVec(t.normal, dir));

            int r = (int)(edgeColor.r + (centerColor.r - edgeColor.r)*dp);
            int g = (int)(edgeColor.g + (centerColor.g - edgeColor.g)*dp);
            int b = (int)(edgeColor.b + (centerColor.b - edgeColor.b)*dp);

            Rgb color = new Rgb(r, g, b);
            t.lumcolor = f.getColour(color);
        }
    }

    private Vector<Triangle> sunTransform(Vector<Triangle> triangles, float x, float y, float z) {
        Matrix matTrans = f.makeTranslation(x, y, z);
        Vector<Triangle> translated = new Vector<Triangle>();

        Triangle transformed;
        for (int o = 0; o < triangles.size(); o++) {
            transformed = new Triangle(triangles.get(o));
            transformed.v[0] = f.multiplyMatrixVec(matTrans, transformed.v[0]);
            transformed.v[1] = f.multiplyMatrixVec(matTrans, transformed.v[1]);
            transformed.v[2] = f.multiplyMatrixVec(matTrans, transformed.v[2]);

            //triangle normal
            Vec normal, line1, line2;
            line1 = f.subVec(transformed.v[1], transformed.v[0]);
            line2 = f.subVec(transformed.v[2], transformed.v[0]);
            normal = f.normalizeVec(f.crossProductVec(line1, line2));
            transformed.normal = new Vec(normal);

            translated.add(transformed);
        }


        Log.d("Triangles", "sun: " + translated.size());
        return translated;
    }
}
