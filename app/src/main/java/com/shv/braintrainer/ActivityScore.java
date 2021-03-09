package com.shv.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ActivityScore extends AppCompatActivity {
    private TextView results;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Button buttonStart = findViewById(R.id.buttonStart);
        results = findViewById(R.id.textViewResults);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        if (intent.hasExtra("current")) {
            int current = intent.getIntExtra("current", -1);
            int maxScoreValue = preferences.getInt("maxCount", -1);
            results.setText(String.format(Locale.getDefault(),   "Текущий результат: %d" +
                                                                        "\nМаксимальный результат: %d", current, maxScoreValue));
        } else
            Toast.makeText(this, "Данные отсутствуют!", Toast.LENGTH_LONG).show();
    }

    public void onClickStartAgain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}