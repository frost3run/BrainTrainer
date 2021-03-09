package com.shv.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewCounter;
    private TextView textViewTimer;
    private TextView textViewQuestion;

    private TextView textViewOption0;
    private TextView textViewOption1;
    private TextView textViewOption2;
    private TextView textViewOption3;

    private ArrayList<TextView> buttons;

    private int maxScore = 0;
    private int currentCount = 0;
    private int numberOfQuestion = 0;

    private int operand1 = 0;
    private int operand2 = 0;
    private String operator = "";

    private int answer;
    private int buttonAnswer;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCounter = findViewById(R.id.textViewCount);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewQuestion = findViewById(R.id.textViewQuestion);

        textViewOption0 = findViewById(R.id.textViewOption0);
        textViewOption1 = findViewById(R.id.textViewOption1);
        textViewOption2 = findViewById(R.id.textViewOption2);
        textViewOption3 = findViewById(R.id.textViewOption3);

        buttons = new ArrayList<>();
        buttons.add(textViewOption0);
        buttons.add(textViewOption1);
        buttons.add(textViewOption2);
        buttons.add(textViewOption3);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        new CountDownTimer(18000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (currentCount == 5)
                    millisUntilFinished += 5000;
                if (currentCount == 9)
                    millisUntilFinished += 5000;
                int seconds = (int) (millisUntilFinished / 1000);
                if (seconds <= 10)
                    textViewTimer.setTextColor(Color.RED);
                textViewTimer.setText(String.format(Locale.getDefault(),"%d", seconds));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, ActivityScore.class);
                if (preferences.getInt("maxCount", 0) < currentCount) {
                    maxScore = currentCount;
                    preferences.edit().putInt("maxCount", maxScore).apply();
                }
                intent.putExtra("current", currentCount);
                startActivity(intent);
            }
        }.start();
        generateQuestion();
        playGame();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void playGame() {
        generateQuestion();
        textViewCounter.setText(String.format("%d / %d", currentCount, numberOfQuestion));
        textViewQuestion.setText(String.format("%d %s %d", operand1, operator, operand2));
        for (int i = 0; i < buttons.size(); i++) {
            if (i == buttonAnswer)
                buttons.get(i).setText(Integer.toString(answer));
            else
                buttons.get(i).setText(Integer.toString(generateWrongAnswer()));
        }
    }

    private void generateQuestion() {
        String[] operators = {"+", "-"};
        operand1 = (int) (Math.random() * 30);
        operand2 = (int) (Math.random() * 30);
        operator = ((int) (Math.random() * operators.length) == 1) ? "+" : "-";

        answer = (operator.contains("+")) ? operand1 + operand2 : operand1 - operand2;
        buttonAnswer = (int) (Math.random() * buttons.size());

        //Log.i("op", operand1 + operator + operand2 + "=" + answer);
    }

    private int generateWrongAnswer() {
        return (int) (Math.random() * 31);
    }

    public void onClickChooseAnswer(View view) {
        TextView button = (TextView) view;
        String tag = button.getTag().toString();
        if (Integer.parseInt(tag) == buttonAnswer) {
            Toast.makeText(this, "Верно!", Toast.LENGTH_LONG).show();
            currentCount++;
        }
        else Toast.makeText(this, "Неверно! Правильный ответ: " + answer, Toast.LENGTH_LONG).show();
        numberOfQuestion++;
        playGame();
    }
}