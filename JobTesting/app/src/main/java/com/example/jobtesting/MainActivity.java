package com.example.jobtesting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        LayoutInflater factory = LayoutInflater.from(this);
        viewFlipper.addView(factory.inflate(R.layout.layout0, null));
        viewFlipper.addView(factory.inflate(R.layout.layout1, null));
        viewFlipper.addView(factory.inflate(R.layout.layout2, null));

        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
    }

    public  void switchLayout(int i) {
        viewFlipper.setDisplayedChild(i);
    }

    public void toLayout0(View view) { switchLayout(0); }
    public void toLayout1(View view) { switchLayout(1); }
    public void toLayout2(View view) { switchLayout(2); }

}
