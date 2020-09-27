package com.example.a3drenderoptimalized;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    float screenX, screenY;
    GameView gameView;

    ImageButton[] arrows = new ImageButton[4];
    ImageButton vertical;

    private float verticalYA;
    private float touchY;
    private float touchX = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        WindowManager w = getWindowManager();
        Point size = new Point();
        w.getDefaultDisplay().getSize(size);

        screenX = size.x;
        screenY = size.y;

        gameView = new GameView(this, surfaceView, screenX, screenY);

        arrows[0] = findViewById(R.id.a0);
        arrows[1] = findViewById(R.id.a1);
        arrows[2] = findViewById(R.id.a2);
        arrows[3] = findViewById(R.id.a3);


        for (int i = 0; i < arrows.length; i++) {
            ImageButton a = arrows[i];
            a.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            gameView.arrowsPress(Integer.parseInt(view.getTag().toString()));
                            return true;
                        case MotionEvent.ACTION_UP: gameView.arrowsRelease(Integer.parseInt(view.getTag().toString()));
                            return true;
                    }

                    return false;
                }
            });
        }

        vertical = (ImageButton) findViewById(R.id.vertical);
        vertical.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        verticalYA = motionEvent.getY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (motionEvent.getY() > verticalYA) {
                            gameView.arrowsPress(4);
                        } else {
                            gameView.arrowsPress(5);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        gameView.arrowsRelease(4);
                        gameView.arrowsRelease(5);
                        return true;
                }
                return false;
            }
        });

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = motionEvent.getX();
                        touchY = motionEvent.getY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (touchX != -1) {
                            gameView.camera.turnCamera(touchX-motionEvent.getX(), touchY-motionEvent.getY());
                            gameView.arrows[6] = true;
                            return true;
                        }

                    case MotionEvent.ACTION_UP:
                        gameView.camera.setCamera();
                        touchX = -1;
                        gameView.arrows[6] = false;
                        return true;
                }
                return false;
            }
        });
    }

    public void pause(View view) {
        switch(gameView.pause.toString()) {
            case "true": gameView.pause = false; break;
            case "false": gameView.pause = true; break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause(new View(this));
    }
}