package com.example.zempisslepmapy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class EditorClass extends Fragment {

    Context context;
    EditorClass (Context context) {
        this.context = context;
    }

    HashSet<String> nullHashset = new HashSet<>(Arrays.asList("text"));
    String[] titles;
    String[][] allTerms;
    ScrollView scrollView;
    LinearLayout parentLayout;
    ImageButton addView;
    Button sync;
    Button save;

    LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    LinearLayout.LayoutParams imageButtonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor_layout, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        parentLayout = (LinearLayout) view.findViewById(R.id.parent_layout);

        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestFocusFromTouch();
                return false;
            }
        });

        sync = (Button) view.findViewById(R.id.synchronize);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    synchronize();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                Log.d("action", "synchronize");
            }
        });

        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                Log.d("action", "save");
            }
        });

        editTextParams.weight = 1.0f;
        imageButtonParams.gravity = Gravity.RIGHT;

        try {
            synchronize();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void getNames() {
        SharedPreferences sharedpreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        Set<String> hashSet;

        hashSet = sharedpreferences.getStringSet("names", nullHashset);
        titles = new String[hashSet.size()];
        hashSet.toArray(titles);

        allTerms = new String[titles.length][];
        int i = 0;
        for (String title: titles) {
            String[] terms;

            hashSet = sharedpreferences.getStringSet(title, nullHashset);
            terms = new String[hashSet.size()];
            hashSet.toArray(terms);

            allTerms[i] = terms;
            i++;
        }
    }

    public void synchronize() throws NoSuchFieldException {
        parentLayout.removeAllViews();

        getNames();
        if (titles != null) {
            int i = 0;
            for (String title: titles) {
                final LinearLayout mainLayout = new LinearLayout(context);
                mainLayout.setLayoutParams(mainLayoutParams);
                mainLayout.setPadding(25,10,10,10);
                mainLayout.setBackgroundResource(R.drawable.layout_border);
                mainLayout.setOrientation(LinearLayout.VERTICAL);

                TextView titleView = new TextView(context);
                titleView.setLayoutParams(titleParams);
                titleView.setText(title);
                titleView.setTextColor(getResources().getColor(R.color.textTitle));
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                mainLayout.addView(titleView);

                for (String s: allTerms[i]) {
                    final LinearLayout componentLayout = new LinearLayout(context);
                    componentLayout.setLayoutParams(layoutParams);
                    componentLayout.setOrientation(LinearLayout.HORIZONTAL);

                    EditText editText = new EditText(context);
                    editText.setLayoutParams(editTextParams);
                    editText.setText(s, EditText.BufferType.NORMAL);
                    editText.setTextColor(Color.BLACK);
                    editText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));

                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    try { f.set(editText, R.drawable.cursor); } catch (Exception e) {}

                    final ImageButton imageButton = new ImageButton(context);
                    imageButton.setLayoutParams(imageButtonParams);
                    imageButton.setImageResource(R.drawable.delete);
                    imageButton.setBackgroundColor(Color.TRANSPARENT);

                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mainLayout.removeView(componentLayout);
                        }
                    });

                    componentLayout.addView(editText);
                    componentLayout.addView(imageButton);

                    mainLayout.addView(componentLayout);
                }

                parentLayout.addView(mainLayout);
                i++;
            }
        }
    }

    private void save() {

    }
}
