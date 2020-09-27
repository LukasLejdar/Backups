package com.example.a3drenderoptimalized;

public class Matrix {
    float m[][] = new float[4][4];

    Matrix() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                m[x][y]  = 0;
            }
        }
    }
}
