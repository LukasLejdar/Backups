package com.example.a2dgameengine;

import android.graphics.Rect;
import android.util.Log;

import java.util.Vector;

public class Ground {

    Vector<Line> Lines = new Vector();
    Vector<Point> Points = new Vector();

    Ground(float[][] ground) {

        Line line;
        Point point;
        Log.d("Collision", "leng: " + ground.length);
        for (int i = 0; i < (ground[0].length-1)*4; i+=4) {
            int p = i/4;
            line = new Line(ground[0][p], ground[1][p], ground[0][p+1], ground[1][p+1]);
            Lines.add(line);

            point = new Point(ground[0][p], ground[1][p]);
            Points.add(point);
            point = new Point(ground[0][p+1], ground[1][p+1]);
            Points.add(point);
        }
    }

    public Vector getLines() {
        return Lines;
    }
    public Vector getPoints() {
        return Points;
    }
}
