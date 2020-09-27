package com.example.a3drenderoptimalized;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Functions {

    public int getColourLuminated(float lum, Rgb color) {
        int r = (int) (color.r*lum);
        int g = (int) (color.g*lum);
        int b = (int) (color.b*lum);

        return (r-255)*256*256 + (g-255)*256 + (b-256);
    }

    public int getColour(Rgb color) {
        return (color.r-255)*256*256 + (color.g-255)*256 + (color.b-256);
    }

    public Vector<Triangle> loadFromObjectFile(String name, Context context) {
        Vector<Triangle> tris = new Vector<Triangle>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(name)));
            Vector<Vec> verts = new Vector<Vec>();

            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.length() != 0) {
                    if (line.charAt(0) == 'v') {
                        String data[] = line.split(" ");
                        Vec v = new Vec(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                        verts.add(v);
                    }

                    if (line.charAt(0) == 'f') {
                        String data[] = line.split(" ");
                        for (int i = 0; i < data.length; i++) {
                            data[i] = data[i].split("/")[0];
                        }
                        if (data.length == 4) {
                            Triangle t = new Triangle(verts.get(Integer.parseInt(data[1])-1), verts.get(Integer.parseInt(data[2])-1), verts.get(Integer.parseInt(data[3])-1));
                            tris.add(t);

                        } else {
                            Triangle t = new Triangle(verts.get(Integer.parseInt(data[1])-1), verts.get(Integer.parseInt(data[2])-1), verts.get(Integer.parseInt(data[3])-1));
                            tris.add(t);
                            t = new Triangle(verts.get(Integer.parseInt(data[1])-1), verts.get(Integer.parseInt(data[3])-1), verts.get(Integer.parseInt(data[4])-1));
                            tris.add(t);

                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tris;
    }

    // Math Functions

    public Vec multiplyMatrixVec(Matrix m, Vec i) {
        Vec v = new Vec();
        v.x = i.x * m.m[0][0] + i.y * m.m[1][0] + i.z * m.m[2][0] + i.w * m.m[3][0];
        v.y = i.x * m.m[0][1] + i.y * m.m[1][1] + i.z * m.m[2][1] + i.w * m.m[3][1];
        v.z = i.x * m.m[0][2] + i.y * m.m[1][2] + i.z * m.m[2][2] + i.w * m.m[3][2];
        v.w = i.x * m.m[0][3] + i.y * m.m[1][3] + i.z * m.m[2][3] + i.w * m.m[3][3];
        return v;
    }

    public float degToRad(float deg) {
        return (float) (Math.PI/180 * deg);
    }

    public Vec addVec (Vec v1, Vec v2) {
        return new Vec(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public Vec subVec (Vec v1, Vec v2) {
        return new Vec(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public Vec mulVec (Vec p, float f) {
        return new Vec(p.x*f, p.y*f, p.z*f);
    }

    public Vec divVec (Vec v, float f) {
        return  new Vec(v.x / f, v.y / f, v.z / f);
    }

    public float dotProductVec (Vec v1, Vec v2) {
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }

    public float lengthVec(Vec p) {
        return (float) Math.sqrt(dotProductVec(p, p));
    }

    public Vec subVecs(Vec v0, Vec v1) {
        Vec v = new Vec();
        v.x = v0.x - v1.x;
        v.y = v0.y - v1.y;
        v.z = v0.z - v1.z;
        return v;
    }

    public Vec addVecs(Vec v0, Vec v1) {
        Vec v = new Vec();
        v.x = v0.x + v1.x;
        v.y = v0.y + v1.y;
        v.z = v0.z + v1.z;
        return v;
    }

    public Vec normalizeVec(Vec p) {
        float l = lengthVec(p);
        return new Vec(p.x/l, p.y/l, p.z/l);
    }

    public Vec crossProductVec (Vec p1, Vec v2) {
        Vec v = new Vec();
        v.x = p1.y*v2.z - p1.z*v2.y;
        v.y = p1.z*v2.x - p1.x*v2.z;
        v.z = p1.x*v2.y - p1.y*v2.x;
        return v;
    }

    public Matrix makeRotX(float rotX) {
        Matrix matrix = new Matrix();
        matrix.m[0][0] = 1;
        matrix.m[1][1] = (float) Math.cos(rotX);
        matrix.m[1][2] = (float) Math.sin(rotX);
        matrix.m[2][1] = (float)-Math.sin(rotX);
        matrix.m[2][2] = (float) Math.cos(rotX);
        matrix.m[3][3] = 1;
        return  matrix;
    }

    public Matrix makeRotY(float rotY) {
        Matrix matrix = new Matrix();
        matrix.m[0][0] = (float) Math.cos(rotY);
        matrix.m[1][1] = 1;
        matrix.m[2][0] = (float)-Math.sin(rotY);
        matrix.m[0][2] = (float) Math.sin(rotY);
        matrix.m[2][2] = (float) Math.cos(rotY);
        matrix.m[3][3] = 1;
        return  matrix;
    }

    public Matrix makeRotZ(float rotZ) {
        Matrix matrix = new Matrix();
        matrix.m[0][0] = (float) Math.cos(rotZ);
        matrix.m[0][1] = (float) Math.sin(rotZ);
        matrix.m[1][0] = (float)-Math.sin(rotZ);
        matrix.m[1][1] = (float) Math.cos(rotZ);
        matrix.m[2][2] = 1;
        matrix.m[3][3] = 1;
        return  matrix;
    }

    public Matrix makeTranslation(float x, float y, float z) {
        Matrix matrix = new Matrix();
        matrix.m[0][0] = 1;
        matrix.m[1][1] = 1;
        matrix.m[2][2] = 1;
        matrix.m[3][3] = 1;
        matrix.m[3][0] = x;
        matrix.m[3][1] = y;
        matrix.m[3][2] = z;
        return  matrix;
    }

    public Vec rot2d(Vec v, float rad) {
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        Vec r = new Vec(
                v.x*cos - v.y*sin,
                v.x*sin + v.y*cos,
                0
        );

        return r;
    }

    public Matrix makeProjection(float fFovRad, float fAspectRatio, float fNear, float fFar) {
        fFovRad = 1.0f / (float) Math.tan(fFovRad * 0.5f);
        Matrix matrix = new Matrix();
        matrix.m[0][0] = fAspectRatio * fFovRad;
        matrix.m[1][1] = fFovRad;
        matrix.m[2][2] = fFar / (fFar - fNear);
        matrix.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        matrix.m[2][3] = 1.0f;
        matrix.m[3][3] = 0.0f;
        return matrix;
    }

    public Matrix multiplyMatrixMatrix(Matrix m1, Matrix m2) {
        Matrix matrix = new Matrix();
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++){
                matrix.m[r][c] = m1.m[r][0] * m2.m[0][c] + m1.m[r][1] * m2.m[1][c] + m1.m[r][2] * m2.m[2][c] + m1.m[r][3] * m2.m[3][c];
            }
        }
        return matrix;
    }

    public Matrix pointAtMatrix(Vec pos, Vec target, Vec up) {
        target = normalizeVec(target);

        // Calculate new Up direction
        Vec a = mulVec(target, dotProductVec(up, target));
        Vec newUp = subVec(up, a);
        newUp = normalizeVec(newUp);

        // New Right direction is just cross product
        Vec newRight = crossProductVec(newUp, target);

        // Construct Dimensioning and Translation Matrix
        Matrix matrix = new Matrix();
        matrix.m[0][0] = newRight.x;	matrix.m[0][1] = newRight.y;	matrix.m[0][2] = newRight.z;	matrix.m[0][3] = 0.0f;
        matrix.m[1][0] = newUp.x;		matrix.m[1][1] = newUp.y;		matrix.m[1][2] = newUp.z;		matrix.m[1][3] = 0.0f;
        matrix.m[2][0] = target.x;	    matrix.m[2][1] = target.y;	    matrix.m[2][2] = target.z;	    matrix.m[2][3] = 0.0f;
        matrix.m[3][0] = pos.x;			matrix.m[3][1] = pos.y;			matrix.m[3][2] = pos.z;			matrix.m[3][3] = 1.0f;
        return matrix;
    }

    public Matrix quickInverseMatrix(Matrix m) {
        Matrix matrix = new Matrix();
        matrix.m[0][0] = m.m[0][0]; matrix.m[0][1] = m.m[1][0]; matrix.m[0][2] = m.m[2][0]; matrix.m[0][3] = 0.0f;
        matrix.m[1][0] = m.m[0][1]; matrix.m[1][1] = m.m[1][1]; matrix.m[1][2] = m.m[2][1]; matrix.m[1][3] = 0.0f;
        matrix.m[2][0] = m.m[0][2]; matrix.m[2][1] = m.m[1][2]; matrix.m[2][2] = m.m[2][2]; matrix.m[2][3] = 0.0f;
        matrix.m[3][0] = -(m.m[3][0] * matrix.m[0][0] + m.m[3][1] * matrix.m[1][0] + m.m[3][2] * matrix.m[2][0]);
        matrix.m[3][1] = -(m.m[3][0] * matrix.m[0][1] + m.m[3][1] * matrix.m[1][1] + m.m[3][2] * matrix.m[2][1]);
        matrix.m[3][2] = -(m.m[3][0] * matrix.m[0][2] + m.m[3][1] * matrix.m[1][2] + m.m[3][2] * matrix.m[2][2]);
        matrix.m[3][3] = 1.0f;
        return matrix;
    }

    public Vec intersectPlaneVec(Vec plane_p, Vec plane_n, Vec lineStart, Vec lineEnd) {
        Vec lineDir = subVec(lineEnd, lineStart);
        lineDir = normalizeVec(lineDir);
        float tilt = dotProductVec(plane_n, lineDir);

        if (Math.abs(tilt) == 0) {
            //the line lies on the plane, don't render at all
            return new Vec();
        }

        float t = (dotProductVec(plane_n, plane_p) - dotProductVec(plane_n, lineStart)) / tilt;
        return addVec(lineStart, mulVec(lineDir, t));
    }

    public boolean isTriangleVisible(Triangle tri, float screenX, float screenY) {
        if (tri.v[0].y > screenY && tri.v[1].y > screenY && tri.v[2].y > screenY) {
            return false;
        }
        if (tri.v[0].x > screenX && tri.v[1].x > screenX && tri.v[2].x > screenX) {
            return false;
        }

        if (tri.v[0].y < 0 && tri.v[1].y < 0 && tri.v[2].y < 0) {
            return false;
        }
        if (tri.v[0].x < 0 && tri.v[1].x < 0 && tri.v[2].x < 0) {
            return false;
        }

        return true;
    }

    public Vec vecToSize(Vec v, float scale) {
        Vec scaled = normalizeVec(v);
        scaled.x *= scale;
        scaled.y *= scale;
        scaled.z *= scale;
        return scaled;
    }

    public Boolean clipAgainstPlaneTRinagle(Vec plane_p, Vec plane_n, Triangle tri) {
        // Make sure plane normal is indeed normal
        plane_n = normalizeVec(plane_n);

        Vec[] inside_points = new Vec[3];  int nInsidePointCount = 0;
        Vec[] outside_points = new Vec[3]; int nOutsidePointCount = 0;

        // Get signed distance of each point in triangle to plane
        float d0 = dist(tri.v[0], plane_p, plane_n, 0);
        float d1 = dist(tri.v[1], plane_p, plane_n, 1);
        float d2 = dist(tri.v[2], plane_p, plane_n, 2);

        if (d0 > 0) {inside_points[nInsidePointCount++] = tri.v[0];}
        else {outside_points[nOutsidePointCount++] = tri.v[0];}
        if (d1 > 0) {inside_points[nInsidePointCount++] = tri.v[1];}
        else {outside_points[nOutsidePointCount++] = tri.v[1];}
        if (d2 > 0) {inside_points[nInsidePointCount++] = tri.v[2];}
        else {outside_points[nOutsidePointCount++] = tri.v[2];}

        if (nInsidePointCount == 0) {
            return false;
        }

        if (nInsidePointCount == 3) {
            return true;
        }

        if (nInsidePointCount == 1) {
            tri.v[0] = new Vec(inside_points[0]);
            tri.v[1] = intersectPlaneVec(plane_p, plane_n, new Vec(inside_points[0]), new Vec(outside_points[0]));
            tri.v[2] = intersectPlaneVec(plane_p, plane_n, new Vec(inside_points[0]), new Vec(outside_points[1]));
            return true;
        }

        if (nInsidePointCount == 2) {
            tri.v = new Vec[4];
            for(int i = 0; i < 4; i++) {tri.v[i] = new Vec();}

            tri.v[0] = new Vec(inside_points[0]);
            tri.v[1] = new Vec(inside_points[1]);
            tri.v[3] = intersectPlaneVec(plane_p, plane_n, new Vec(inside_points[0]), new Vec(outside_points[0]));
            tri.v[2] = intersectPlaneVec(plane_p, plane_n, new Vec(inside_points[1]), new Vec(outside_points[0]));

            return true;
        }

        return false;
    }

    private float dist(Vec p, Vec plane_p, Vec plane_n, int i) {
        // view plane is never rotated >> plane_n.x = 0...
        return p.z - dotProductVec(plane_n, plane_p);
        //return (plane_n.x * p.x + plane_n.y * p.y + plane_n.z * p.z - Vector_DotProduct(plane_n, plane_p));
    }


    //Generate functions

    public Vector<Triangle> generatePlanet(int size, float seeHeight, float smoothness, float scale, float moveUpBy) {
        float radius = size/2;
        Triangle[][][] vecs = new Triangle[6][size][size*2];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                float x0 = (x+0)-radius; float y0 = (y+0)-radius;
                float x1 = (x+0)-radius; float y1 = (y+1)-radius;
                float x2 = (x+1)-radius; float y2 = (y+1)-radius;
                float x3 = (x+1)-radius; float y3 = (y+0)-radius;

                //Front
                vecs[0][x][y*2+0] = new Triangle(new Vec(x0, y0, -radius), new Vec(x1, y1, -radius), new Vec(x3, y3, -radius));
                vecs[0][x][y*2+1] = new Triangle(new Vec(x1, y1, -radius), new Vec(x2, y2, -radius), new Vec(x3, y3, -radius));

                //Back
                vecs[1][x][y*2+0] = new Triangle(new Vec(x1, y1, radius), new Vec(x0, y0, radius), new Vec(x3, y3, radius));
                vecs[1][x][y*2+1] = new Triangle(new Vec(x2, y2, radius), new Vec(x1, y1, radius), new Vec(x3, y3, radius));

                //Top
                vecs[3][x][y*2+0] = new Triangle(new Vec(x0, radius, y0), new Vec(x1, radius, y1), new Vec(x3, radius, y3));
                vecs[3][x][y*2+1] = new Triangle(new Vec(x1, radius, y1), new Vec(x2, radius, y2), new Vec(x3, radius, y3));

                //Bottom
                vecs[2][x][y*2+0] = new Triangle(new Vec(x1, -radius, y1), new Vec(x0, -radius, y0), new Vec(x3, -radius, y3));
                vecs[2][x][y*2+1] = new Triangle(new Vec(x2, -radius, y2), new Vec(x1, -radius, y1), new Vec(x3, -radius, y3));

                //Right
                vecs[4][x][y*2+0] = new Triangle(new Vec(radius, x2, y2), new Vec(radius, x1, y1), new Vec(radius, x0, y0));
                vecs[4][x][y*2+1] = new Triangle(new Vec(radius, x2, y2), new Vec(radius, x0, y0), new Vec(radius, x3, y3));

                //left
                vecs[5][x][y*2+0] = new Triangle(new Vec(-radius, x1, y1), new Vec(-radius, x2, y2), new Vec(-radius, x0, y0));
                vecs[5][x][y*2+1] = new Triangle(new Vec(-radius, x0, y0), new Vec(-radius, x2, y2), new Vec(-radius, x3, y3));
            }
        }

        Vector<Triangle> triangles = new Vector<Triangle>();
        Vector<Float> heights = new Vector<>();
        Vector<Boolean> isOcean = new Vector<>();

        Boolean ocean;
        float maxHeight = 0;
        float minHeight = 10000;

        for (Triangle[][] a: vecs) {
            for (Triangle[] b: a) {
                for (Triangle c: b) {
                    ocean = true;

                    float height = seeHeight;
                    for (int i = 0; i < c.v.length; i++) {
                        //perlin noise layers
                        Vec l0 = vecToSize(c.v[i], seeHeight/smoothness);
                        Vec l1 = vecToSize(c.v[i], seeHeight/smoothness*5);
                        height = seeHeight + (float) (noise(l0.x, l0.y, l0.z)*scale) + moveUpBy + (float) (noise(l1.x, l1.y, l1.z)*scale)/5;

                        if (height >= seeHeight) { ocean = false; }
                        if (height > maxHeight) { maxHeight = height; }
                        if (height < minHeight) { minHeight = height; }

                        Vec v = vecToSize(c.v[i], Math.max(seeHeight, height));
                        c.v[i] = v;
                    }

                    if (ocean) { isOcean.add(true); } else { isOcean.add(false); }

                    heights.add(height);
                    triangles.add(c);
                }
            }
        }

        for (int i = 0; i < triangles.size(); i++) {
            Triangle t = triangles.get(i);

            //triangle normal
            Vec normal, line1, line2;
            line1 = subVec(t.v[1], t.v[0]);
            line2 = subVec(t.v[2], t.v[0]);
            normal = normalizeVec( crossProductVec(line1, line2));
            t.normal = new Vec(normal);

            if (isOcean.get(i)) {
                t.rgb = getOceanColor((-(heights.get(i) - seeHeight))/(-(minHeight-seeHeight)));
            } else {
                t.rgb = (getPlanetColor(Math.min(1, (heights.get(i) - seeHeight)/(maxHeight-seeHeight)+0.4f)));
            }
        }

        return triangles;
    }

    static public float noise(double x, double y, double z) {
        int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
                Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
                Z = (int)Math.floor(z) & 255;
        x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
        y -= Math.floor(y);                                // OF POINT IN CUBE.
        z -= Math.floor(z);
        double u = fade(x),                                // COMPUTE FADE CURVES
                v = fade(y),                                // FOR EACH OF X,Y,Z.
                w = fade(z);
        int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
                B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

        return (float) lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  // AND ADD
                grad(p[BA  ], x-1, y  , z   )), // BLENDED
                lerp(u, grad(p[AB  ], x  , y-1, z   ),  // RESULTS
                        grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
                lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  // CORNERS
                        grad(p[BA+1], x-1, y  , z-1 )), // OF CUBE
                        lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
                                grad(p[BB+1], x-1, y-1, z-1 ))));
    }
    static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
    static double lerp(double t, double a, double b) { return a + t * (b - a); }
    static double grad(int hash, double x, double y, double z) {
        int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
        double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
                v = h<4 ? y : h==12||h==14 ? x : z;
        return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
    }
    static final int p[] = new int[512], permutation[] = { 151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };
    static { for (int i=0; i < 256 ; i++) p[256+i] = p[i] = permutation[i]; }

    public Rgb getPlanetColor(float height) {
        Rgb grass = new Rgb(80, 224, 31);
        Rgb mountins = new Rgb(142, 133, 116);

        int r = (int)(grass.r + (mountins.r - grass.r)*height);
        int g = (int)(grass.g + (mountins.g - grass.g)*height);
        int b = (int)(grass.b + (mountins.b - grass.b)*height);

        return new Rgb(r, g, b);
    }

    public Rgb getOceanColor(float depth) {
        Rgb shallow = new Rgb(38, 155, 217);
        Rgb deep = new Rgb(34, 105, 221);

        int r = (int)(shallow.r + (deep.r - shallow.r)*depth);
        int g = (int)(shallow.g + (deep.g - shallow.g)*depth);
        int b = (int)(shallow.b + (deep.b - shallow.b)*depth);

        return new Rgb(r, g, b);
    }
}


