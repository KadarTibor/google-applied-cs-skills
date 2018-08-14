package com.kdrtibor.scarnes_dice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int userOverallScore = 0;
    private int userTurnScore = 0;
    private int computerOverallScore = 0;
    private int computerTurnScore = 0;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        Button rollButton = findViewById(R.id.rollButton);
        rollButton.setOnClickListener(v -> {
            int rollValue = handleRollAction();
            if (rollValue != 1) {
                userTurnScore += rollValue;
            } else {
                userTurnScore = 0;
                handleHoldAction();
                passTurnToComputer();
            }

        });

        Button holdButton = findViewById(R.id.holdButton);
        holdButton.setOnClickListener(v -> {
            handleHoldAction();
            passTurnToComputer();
        });

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> {
            handleResetAction();
        });
    }

    private void handleResetAction() {
        userTurnScore = 0;
        userOverallScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;
        updateLabels("0", "0");
        updateActionLabel("The game has been reset");
    }

    private void handleHoldAction() {
        userOverallScore += userTurnScore;
        userTurnScore = 0;
        computerOverallScore += computerTurnScore;
        computerTurnScore = 0;
        updateLabels(userOverallScore + "", computerOverallScore + "");
    }

    private void passTurnToComputer() {
        enableRollAndHold(false);
        while (computerTurnScore < 20) {
            int rollValue = handleRollAction();
            if (rollValue != 1) {
                computerTurnScore += rollValue;
            } else {
                computerTurnScore = 0;
                break;
            }
        }
        handleHoldAction();
        enableRollAndHold(true);
    }

    private void enableRollAndHold(boolean doEnable) {
        Button rollButton = findViewById(R.id.rollButton);
        rollButton.setEnabled(doEnable);
        Button holdButton = findViewById(R.id.holdButton);
        holdButton.setEnabled(doEnable);
    }

    private void updateLabels(String yourScore, String computerScore) {
        TextView yourScoreTextView = findViewById(R.id.yourScoreVal);
        yourScoreTextView.setText(yourScore);
        TextView computerScoreTextView = findViewById(R.id.computerScoreVal);
        computerScoreTextView.setText(computerScore);
    }

    private void updateActionLabel(String description) {
        TextView actionLabel = findViewById(R.id.action_label);
        actionLabel.setText(description);
    }

    private int handleRollAction() {
        int rollValue = Math.abs(random.nextInt()) % 6 + 1;
        ImageView image = findViewById(R.id.image);
        switch (rollValue) {
            case 1: {
                image.setImageResource(R.drawable.dice1);
                break;
            }
            case 2: {
                image.setImageResource(R.drawable.dice2);
                break;
            }
            case 3: {
                image.setImageResource(R.drawable.dice3);
                break;
            }
            case 4: {
                image.setImageResource(R.drawable.dice4);
                break;
            }
            case 5: {
                image.setImageResource(R.drawable.dice5);
                break;
            }
            case 6: {
                image.setImageResource(R.drawable.dice6);
                break;
            }
            default: {
                break;
            }
        }
        updateActionLabel("Just rolled: " + rollValue);
        return rollValue;
    }
}
