package com.example.a3drenderoptimalized;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import androidx.annotation.RequiresApi;

import static java.util.stream.Collectors.toMap;

public class GameView implements Runnable {

    Context context;
    Thread thread = new Thread(this);
    SurfaceView surfaceView;
    SurfaceHolder holder;
    Canvas canvas;

    //variables
    private static float screenX, screenY;
    private static float centerX, centerY;

    //Paints
    Paint black = new Paint();
    Paint blue = new Paint();
    Paint red = new Paint();
    Paint starPaint = new Paint();
    private float stroke = 5;

    //Controls
    Boolean pause = true;
    Boolean[] arrows = new Boolean[7];
    Camera camera;

    //world
    Mesh mesh = new Mesh();
    Stars stars = new Stars(20);
    Sun sun;

    //time measuring
    long lastTimeMeasuring = 0l;
    int ticks = 0;
    int[][] timeData = new int[3][4];

    //others
    Functions f = new Functions();

    GameView(Context context, SurfaceView surfaceView, float screenX, float screenY) {
        this.context = context;
        this.surfaceView = surfaceView;
        holder = surfaceView.getHolder();

        //variables
        this.screenX = screenX;
        this.screenY = screenY;
        centerX = screenX/2;
        centerY = screenY/2;

        //paints
        black.setColor(Color.BLACK); black.setStrokeWidth(stroke);
        blue.setColor(Color.BLUE); blue.setStrokeWidth(stroke);
        red.setColor(Color.RED); red.setStrokeWidth(stroke);
        starPaint.setColor(Color.WHITE); starPaint.setStyle(Paint.Style.FILL);

        //prepare
        camera = new Camera(0,1, 0, 0, 0, 1, f.degToRad(70), screenY/screenX, 0.1f, 1000.0f, screenX, screenY);
        camera.vCamera = new Vec(-41, 0, 48);
        camera.turnY = -0.145f;
        camera.move();

        sun = new Sun(context, 30, -200, -50, 600);
        mesh.triangles = f.generatePlanet(20, 20, 20, 3, 0f);
        mesh.pos = new Vec(0,0,70);
        worldTransform(mesh, 0, 0);
        Log.d("Triangles", "" + mesh.transformed.size());

        Arrays.fill(arrows, false);
        for (int[] i: timeData) {
            Arrays.fill(i, 0);
        }

        //mesh.triangles = loadFromObjectFile("sandal.txt");
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        while (true) {
            if (pause) {
                measureTime(0);
                update();
                measureTime(1);
                draw();
                measureTime(2);
            }

            String log = "";
            int performance = 0;
            for (int i = 0; i < timeData.length; i++) {
                int[] section = timeData[i];
                int sum = 0;

                for (int t: section) {
                    sum += t;
                }

                log += "s"+i+": " + sum/timeData[0].length +" ";
                performance += sum/timeData[0].length;
            }
            if (ticks%20 == 0) {
                Log.d("Performance", performance + " " + log);
            }

            ticks++;
            lastTimeMeasuring = System.currentTimeMillis();
        }
    }

    private void measureTime(int i) {
        Long currentTime = System.currentTimeMillis();
        timeData[i][ticks%timeData[0].length] = (int) (currentTime - lastTimeMeasuring);
        lastTimeMeasuring = currentTime;
    }

    public void update() {
        worldTransform(mesh, ticks*0.006f, 2);
        for (int i = 0; i < arrows.length; i++) {
            //inverse: more = les speed
            int speed = 1;
            if (arrows[i] == true) {
                switch (i) {
                    case 0: camera.vCamera = f.addVec(camera.vCamera, f.divVec(camera.vStraight, speed)); break;
                    case 1: camera.vCamera = f.addVec(camera.vCamera, f.multiplyMatrixVec(f.makeRotY((float) -Math.PI/2), f.divVec(camera.vStraight, speed))); break;
                    case 2: camera.vCamera = f.subVec(camera.vCamera, f.divVec(camera.vStraight, speed)); break;
                    case 3: camera.vCamera = f.addVec(camera.vCamera, f.multiplyMatrixVec(f.makeRotY((float) +Math.PI/2), f.divVec(camera.vStraight, speed))); break;
                    case 4: camera.vCamera.y += 0.5; break;
                    case 5: camera.vCamera.y -= 0.5; break;
                }
                camera.move();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void draw() {
        if (holder.getSurface().isValid()) {
            synchronized (holder) {
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.BLACK);

                //Stars
                for (Map.Entry map : stars.s.entrySet()) {
                    Vec star = (Vec) map.getValue();
                    if (true) { //f.dotProductVec(s, camera.vLookDir) < 0
                        star = f.multiplyMatrixVec(camera.matCameraRot, star); //World Space to View Space
                        star = f.multiplyMatrixVec(camera.matProj, star); //3D to 2D
                        star = f.divVec(star, star.w); //scale into view
                        star = f.addVec(star, new Vec(1, 1, 0)); // move to the normalized center of the screen
                        star.x *= centerX; // scale to screen size
                        star.y *= centerY;
                        canvas.drawCircle(star.x, star.y, (Float) map.getKey(), starPaint);
                    }
                }

                //Paint sunLight = new Paint();
                //Shader gradient = new RadialGradient (sunCenter.x, sunCenter.y, radius, sun.eColor, Color.TRANSPARENT, Shader.TileMode.MIRROR);
                //sunLight.setShader(gradient);

                sun.colorSun(f.subVec(camera.vCamera, sun.pos));

                float sunDist = f.lengthVec(f.subVec(sun.pos, camera.vCamera));
                float meshDist = f.lengthVec(f.subVec(mesh.pos, camera.vCamera));

                if (sunDist > meshDist) {
                    drawMesh(sun.transformed, canvas);
                    drawMesh(mesh.transformed, canvas);
                } else  {
                    drawMesh(mesh.transformed, canvas);
                    drawMesh(sun.transformed, canvas);
                }

                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawMesh(Vector<Triangle> transformed, Canvas canvas) {

        //Objects
        Vec plane_p = new Vec(0.0f, 0.0f, 0.1f);
        Vec plane_n = new Vec(0.0f, 0.0f, 1.0f);
        Map<Triangle, Float> vecTrianglesToRaster = new HashMap<>();

        for (int o = 0; o < transformed.size(); o++) {
            Triangle tri = new Triangle(transformed.elementAt(o));
            Vec vCameraRay = f.subVec(tri.v[0], camera.vCamera);

            if (f.dotProductVec(tri.normal, vCameraRay) < 0) {
                //Convert World Space to View Space
                tri.v[0] = f.multiplyMatrixVec(camera.matView, tri.v[0]);
                tri.v[1] = f.multiplyMatrixVec(camera.matView, tri.v[1]);
                tri.v[2] = f.multiplyMatrixVec(camera.matView, tri.v[2]);
                Triangle clipped = new Triangle(tri);

                if (f.clipAgainstPlaneTRinagle(plane_p, plane_n, clipped)) {
                    for (int i = 0; i < clipped.v.length; i++) {
                        clipped.v[i] = f.multiplyMatrixVec(camera.matProj, clipped.v[i]); //3D to 2D
                        clipped.v[i] = f.divVec(clipped.v[i], clipped.v[i].w); //scale into view
                        clipped.v[i] = f.addVec(clipped.v[i], new Vec(1, 1, 0)); // move to the normalized center of the screen
                        clipped.v[i].x *= centerX; // scale to screen size
                        clipped.v[i].y *= centerY;
                    }

                    vecTrianglesToRaster.put(clipped, (tri.v[0].z + tri.v[1].z + tri.v[2].z) / 3);
                }
            }
        }

        // sorting by descending order
        Map<Triangle, Float> vecRaster = vecTrianglesToRaster
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        for (Map.Entry map : vecRaster.entrySet()) {
            Triangle tri = (Triangle) map.getKey();
            if (f.isTriangleVisible(tri, screenX, screenY)) {
                drawPoly(canvas, tri);
            }
        }
    }

    private void drawPoly(Canvas canvas, Triangle tri) {
        Paint polyPaint = new Paint();
        polyPaint.setColor(tri.lumcolor);
        polyPaint.setStyle(Paint.Style.FILL);

        Path polyPath = new Path();
        polyPath.setFillType(Path.FillType.EVEN_ODD);
        polyPath.moveTo(tri.v[0].x, tri.v[0].y);
        for (int i = 1; i < tri.v.length; i++) { polyPath.lineTo(tri.v[i].x, tri.v[i].y); }
        polyPath.lineTo(tri.v[0].x, tri.v[0].y);
        polyPath.close();

        canvas.drawPath(polyPath, polyPaint);
        //polyPaint.setColor(Color.BLACK);
        //polyPaint.setStyle(Paint.Style.STROKE);
        //polyPaint.setStrokeWidth(tri.stroke);
        //canvas.drawPath(polyPath, polyPaint);
    }

    private void worldTransform(Mesh mesh, float rotZ, float rotX) {
        Matrix matTrans = f.makeTranslation(mesh.pos.x, mesh.pos.y, mesh.pos.z);
        Matrix matRot = f.multiplyMatrixMatrix(f.makeRotZ(rotZ), f.makeRotX(rotX)); //(float)Math.PI/2
        Matrix transform = f.multiplyMatrixMatrix(matRot, matTrans);

        Vector<Triangle> translated = new Vector<Triangle>();
        Triangle transformed;

        for (int o = 0; o < mesh.triangles.size(); o++) {
            transformed = new Triangle(mesh.triangles.get(o));
            transformed.v[0] = f.multiplyMatrixVec(transform, transformed.v[0]);
            transformed.v[1] = f.multiplyMatrixVec(transform, transformed.v[1]);
            transformed.v[2] = f.multiplyMatrixVec(transform, transformed.v[2]);

            //triangle normal
            Vec normal, line1, line2;
            line1 = f.subVec(transformed.v[1], transformed.v[0]);
            line2 = f.subVec(transformed.v[2], transformed.v[0]);
            normal = f.normalizeVec(f.crossProductVec(line1, line2));
            transformed.normal = new Vec(normal);

            Vec light_direction = f.subVec(sun.pos, transformed.v[0]);
            light_direction = f.normalizeVec(light_direction);

            // How "aligned" are light direction and triangle surface normal?
            float dp = Math.max(0, (f.dotProductVec(light_direction, normal)));
            transformed.lumcolor = f.getColourLuminated(dp, transformed.rgb);

            translated.add(transformed);
        }

        mesh.transformed = translated;
    }


    public void arrowsPress(int i) {
        if (pause) {
            arrows[i] = true;
        }
    }

    public void arrowsRelease(int i) {
        if (pause) {
            arrows[i] = false;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}