package com.example.a2dgameengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.Vector;

public class GameView implements Runnable {

    SurfaceView screenView;
    Canvas canvas;
    SurfaceHolder holder;
    Thread thread;
    Ground ground;
    Collisions collisions;
    Random random = new Random();
    Ball sBall;

    Paint black = new Paint();
    Paint blue = new Paint();
    Paint red = new Paint();

    float screenX, screenY;
    float centerX, centerY;
    float baldStroke = 5;
    float thinStroke = 5;
    float ballR = 70;
    float density = 1;
    float speed = 0.1f;
    Boolean gravity = false;

    Vector<Line> Lines;
    Vector<Point> Points;
    Vector<Ball> balls = new Vector<>();

    public GameView(SurfaceView screenView, float screenX, float screenY) {
        this.screenView = screenView;
        holder = screenView.getHolder();
        canvas = new Canvas();
        collisions = new Collisions(screenX, screenY);

        black.setColor(Color.BLACK);
        black.setStrokeWidth(baldStroke);
        blue.setColor(Color.BLUE);
        blue.setStrokeWidth(baldStroke);
        blue.setStyle(Paint.Style.STROKE);
        red.setColor(Color.RED);
        red.setStrokeWidth(thinStroke);
        red.setStyle(Paint.Style.STROKE);

        this.screenX = screenX;
        this.screenY = screenY;
        centerX = screenX/2;
        centerY = screenY/2;
        ground = setGround();
        Lines = ground.Lines;
        Points = ground.Points;

        Ball ball;
        float x;
        float y;
        float ballR;
        for (int i = 0; i < 8;i++) {
            x = random.nextInt((int) screenX);
            y = random.nextInt((int) screenY);
            ballR = random.nextInt((int) 90) + 40;
            //ballR = 90;
            ball = new Ball(x, y, ballR,i, ballR*density);
            balls.add(ball);
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            update();
            draw();
        }
    }

    private void update() {
        for (Ball ball: balls) {
            ball.move();
            ball.collision = false;
        }

        Ball ball2;
        for (Ball ball1: balls) {
            for (int i = ball1.id + 1; i < balls.size(); i++) {
                ball2 = balls.get(i);
                if (ball1.id != ball2.id) {
                    collisions.circleCircleDyn(ball1, ball2);
                }
            }
        }

        for (Ball ball: balls) {
            collisions.ballBorderCol(ball);
            for (Line line: Lines) {
                collisions.lineBallDyn(ball, line);
            }
        }
    }


    private void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);

            for (Line line: Lines) {
                canvas.drawLines(line.line, black);
                //canvas.drawCircle(line.gx, line.gy,5, blue);
            }
            for (Ball ball: balls) {
                canvas.drawCircle(ball.x, ball.y, ball.r, blue);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private Ground setGround() {
        Ground ground;
        float[][] groundF = new float[][] {
                {ballR + 300, centerX, screenX -(ballR + 300)},
                {centerY-100,centerY+100, centerY-100},
        };
        ground = new Ground(groundF);
        return ground;
    }

    public void selectBall(float x, float y) {
        for (Ball ball: balls) {
            if (collisions.circlePointCol(x, y, ball.x, ball.y, ball.r)) {
                sBall = ball;
                sBall.x = x;
                sBall.y = y;
                moveBall(x,y);
                return;
            }
        }
    }

    public void unSelectBall() {
        sBall = null;
    }

    public void moveBall(float x, float y) {
        if (sBall != null) {
            sBall.vx = (x-sBall.x) * speed;
            sBall.vy = (y-sBall.y) * speed;

        }
    }

    public void gravity() {
        if (gravity) {
            for (Ball ball: balls) {
                ball.ay = 0;
                ball.drag = 0;
                collisions.friction = 1;
            }
            gravity = false;
        } else {
            for (Ball ball: balls) {
                ball.ay = 0.4f;
                ball.drag = 0.01f;
                collisions.friction = 0.4f;

            }
            gravity = true;
        }
    }

}
