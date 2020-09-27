package com.example.a2dgameengine;

import android.util.Log;

public class Collisions {

    float friction = 1;
    float screenX, screenY;
    float moveX, moveY;

    Collisions(float screenX, float screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public boolean circlePointCol(float x, float y, float cx, float cy, float r) {
        boolean col = false;
        float distance = (float) (Math.pow((x-cx), 2) + Math.pow((y-cy), 2));
        if (distance < Math.pow(r, 2)) {
            col = true;
        }
        return col;
    }

    public boolean ballBorderCol(Ball ball) {
        float x = ball.x;
        float y = ball.y;
        float r = ball.r;

        boolean col = false;
        float borderX1 = r;
        float borderX2 = screenX - r;
        float borderY1 = r;
        float borderY2 = screenY - r;

        if (x < borderX1) {
            ball.x = borderX1;
            ball.vx = ball.vx*-1;
            col = true;
        } else  if (x > borderX2) {
            ball.x = borderX2;
            ball.vx = ball.vx*-1;
            col = true;
        }

        if (y < borderY1) {
            ball.y = borderY1;
            ball.vy = ball.vy*-1;
            col = true;
        } else  if (y > borderY2) {
            ball.y = borderY2;
            ball.vy = ball.vy*-1;
            col = true;
        }

        return col;
    }

    public void circleCircleDyn(Ball ball1, Ball ball2) {

        float dx = ball1.x - ball2.x;
        float dy = ball1.y - ball2.y;
        float ballRs = ball1.r + ball2.r;
        float distance = dx * dx + dy * dy;

        if(distance < ballRs * ballRs){

            float angle = (float) Math.atan2(dy, dx);
            float sin = (float) Math.sin(angle);
            float cos = (float) Math.cos(angle);

            float vx1 = cos*ball1.vx + sin*ball1.vy;
            float vy1 = -sin*ball1.vx + cos*ball1.vy;
            float vx2 = cos*ball2.vx + sin*ball2.vy;
            float vy2 = -sin*ball2.vx + cos*ball2.vy;

            float m1 = ball1.mass;
            float m2 = ball2.mass;
            float vx1final = ((m1-m2)*vx1 + (2*m2)*vx2) / (m1 + m2);
            float vx2final = ((m2-m1)*vx2 + (2*m1)*vx1) / (m1 + m2);

            ball1.vx = cos*vx1final - sin*vy1;
            ball1.vy = sin*vx1final + cos*vy1;
            ball2.vx = cos*vx2final - sin*vy2;
            ball2.vy = sin*vx2final + cos*vy2;

            Log.d("collision", "vx1" + ball1.vx);
            Log.d("collision", "vy1" + ball1.vy);
            Log.d("collision", "vx2" + ball2.vx);
            Log.d("collision", "vy2" + ball2.vy);
            ball1.collision = true;
            ball2.collision = true;

            distance = (float) Math.sqrt(distance);
            float overLap = 0.5f * (distance - ballRs);

            moveX = overLap * (ball1.x - ball2.x)/distance;
            moveY = overLap * (ball1.y - ball2.y)/distance;

            ball1.subPos(moveX, moveY);
            ball2.addPos(moveX, moveY);

            Log.d("Collision", "circle");
        }
    }

    public void lineBallDyn(Ball ball, Line line) {
        if (pointBallDyn(line.x1, line.y1, ball) == false) {
            if (pointBallDyn(line.x2, line.y2, ball) == false) {

                float velx = ball.nextVelX();
                float vely = ball.nextVelY();

                float dx = ball.x + velx -line.gx;
                float dy = ball.y + vely -line.gy;

                float sin = line.sin;
                float cos = line.cos;

                float dx_ =  cos*dx + sin*dy;
                float dy_ = -sin*dx + cos*dy;
                float velx_ =  cos*velx + sin*vely;
                float vely_ = -sin*velx + cos*vely;

                if (Math.abs(dx_) < line.len/2+ball.r-40 && (dy_ > -ball.r && ball.r > dy_ )) {
                    dx = ball.x -line.gx;
                    dy = ball.y -line.gy;
                    dx_ =  cos*dx + sin*dy;
                    dy_ = -sin*dx + cos*dy;

                    if (dy_ < 0) {
                        dy_ = -ball.r;
                        //vely_ = Math.abs(vely_)*-1;
                    } else {
                        dy_ = ball.r;
                        //vely_ = Math.abs(vely_);
                    }
                    vely_ *= -friction;

                    ball.vx = cos*velx_ - sin*vely_;
                    ball.vy = sin*velx_ + cos*vely_;
                    dx = cos*dx_ - sin*dy_;
                    dy = sin*dx_ + cos*dy_;

                    ball.x = line.gx + dx;
                    ball.y = line.gy + dy;
                    Log.d("Collision", "line");
                }
            }
        }
    }

    public boolean pointBallDyn(float x, float y, Ball ball) {
        float renderD = ball.r + 70;
        if (x-renderD < ball.x  && ball.x  < x+renderD ) {
            if (y-renderD < ball.y  && ball.y  < y+renderD ) {
                float dx = ball.x - x;
                float dy = ball.y - y;
                float distance = (float) Math.sqrt(dx*dx + dy*dy);

                if (distance <= ball.r) {
                    float overLap = distance - ball.r;

                    ball.subPos(overLap * (dx)/distance, overLap * (dy)/distance);

                    float angle = (float) Math.atan2(dy, dx);
                    float sin = (float) Math.sin(angle);
                    float cos = (float) Math.cos(angle);

                    float vx1 = cos*ball.vx + sin*ball.vy;
                    float vy1 = -sin*ball.vx + cos*ball.vy;
                    vx1 *= -1;

                    ball.vx = cos*vx1 - sin*vy1;
                    ball.vy = sin*vx1 + cos*vy1;
                    Log.d("Collision", "point");
                    return true;
                }
            }
        }
        return false;
    }
}
