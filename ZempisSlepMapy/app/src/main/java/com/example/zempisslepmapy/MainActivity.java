package com.example.zempisslepmapy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    TabLayout tabLayout;
    WindowManager windowManager;

    private static int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        windowManager = getWindowManager();
        
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager, sectionsPageAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.randomizer_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.editor_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.camera_icon);
    }

    public void setupViewPager(ViewPager viewPager, final SectionsPageAdapter adapter) {
        adapter.addFragmet(new RandomizerClass(this), getString(R.string.tab_text_1));
        adapter.addFragmet(new EditorClass(this), getString(R.string.tab_text_2));
        adapter.addFragmet(new ScannerClass(this, windowManager), getString(R.string.tab_text_3));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private void requestPermission() {
        while (ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {CAMERA}, REQUEST_CAMERA);
        }
    }
}
