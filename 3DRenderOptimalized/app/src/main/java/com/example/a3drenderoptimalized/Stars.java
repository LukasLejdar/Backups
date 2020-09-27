package com.example.a3drenderoptimalized;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Stars {
    Map<Float, Vec> s = new HashMap<>();
    Random r = new Random();
    Functions f = new Functions();

    Stars(int n) {
        Vec[][] cube = new Vec[6][n];
        for (int i = 0; i < n; i++) {
            cube[0][i] = new Vec(r.nextFloat(), r.nextFloat(), 0.5f);
            cube[1][i] = new Vec(r.nextFloat(), r.nextFloat(), -0.5f);
            cube[2][i] = new Vec(r.nextFloat(), 0.5f, r.nextFloat());
            cube[3][i] = new Vec(r.nextFloat(), -0.5f, r.nextFloat());
            cube[4][i] = new Vec(0.5f, r.nextFloat(), r.nextFloat());
            cube[5][i] = new Vec(-0.5f, r.nextFloat(), r.nextFloat());
        }

        for (int i = 0; i < cube.length; i++) {
            for (int o = 0; o < cube[0].length; o++) {
                s.put(r.nextFloat()*3, f.normalizeVec(cube[i][o]));
            }
        }
    }
}
