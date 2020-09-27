package com.example.a3drenderoptimalized;

import java.util.Vector;

public class Mesh {
    Functions f = new Functions();
    public Vector<Triangle> triangles = new Vector<Triangle>();
    public Vector<Triangle> transformed = new Vector<Triangle>();
    Vec pos = new Vec(0,0,0);

    public void addMesh(Vector<Triangle> newTriangles) {
        for (Triangle t: newTriangles){
            transformed.add(t);
        }
    }

    public void colorTriangles(int r, int g, int b) {
        for (Triangle t: triangles) {
            t.rgb.setRGB(r,g,b);
        }
    }

    public void scale(int s) {
        for (Triangle t: triangles) {
            t.v[0] = f.vecToSize(t.v[0], s);
            t.v[1] = f.vecToSize(t.v[1], s);
            t.v[2] = f.vecToSize(t.v[2], s);
        }
    }
}
