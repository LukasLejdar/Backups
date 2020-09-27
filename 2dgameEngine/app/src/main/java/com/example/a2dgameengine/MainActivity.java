package com.example.a2dgameengine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.drm.DrmStore;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    SurfaceView screenView;
    GameView gameView;

    float screenX, screenY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenView = (SurfaceView) findViewById(R.id.screen);

        WindowManager w = getWindowManager();

        Point size = new Point();
        w.getDefaultDisplay().getSize(size);
        screenX = size.x;
        screenY = size.y-76;

        gameView = new GameView(screenView, screenX, screenY);

        screenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        gameView.selectBall(motionEvent.getX(), motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        gameView.moveBall(motionEvent.getX(), motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.unSelectBall();
                        break;
                }
                return true;
            }
        });
    }

    public void gravity(View view) {
        gameView.gravity();
    }
}
