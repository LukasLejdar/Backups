package com.example.zempisslepmapy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RandomizerClass extends Fragment {

    Context context;
    RandomizerClass (Context context) {
        this.context = context;
    }

    Button next;
    TextView name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.randomizer_layout, container, false);
        name = (TextView) view.findViewById(R.id.name);

        next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextName();
            }
        });

        nextName();

        return view;
    }

    private void nextName() {
        Random random = new Random();
        String[] names = getNames();
        Log.d("Names", "size: " + names.length);
        name.setText(names[random.nextInt(names.length)]);
    }

    private String[] getNames() {
        HashSet<String> hashset = new HashSet<>(Arrays.asList("name"));

        SharedPreferences sharedpreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        Set<String> names = sharedpreferences.getStringSet("names", hashset);
        String[] array = new String[names.size()];
        Log.d("Names", "size: " + names.size());
        return names.toArray(array);
    }
}
