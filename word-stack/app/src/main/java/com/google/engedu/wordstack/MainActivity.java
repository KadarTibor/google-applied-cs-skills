/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private Stack<LetterTile> palcedTiles = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length() == WORD_LENGTH) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                palcedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    palcedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
        View word1LinearLayout = findViewById(R.id.word1);
        View word2LinearLayout = findViewById(R.id.word2);

        if (word1LinearLayout != null) {
            ((LinearLayout) word1LinearLayout).removeAllViews();
        }
        if (word2LinearLayout != null) {
            ((LinearLayout) word2LinearLayout).removeAllViews();
        }
        stackedLayout.removeAllViews();

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        word1 = words.get(Math.abs(random.nextInt()) % words.size());
        word2 = words.get(Math.abs(random.nextInt()) % words.size());
        String scrambledWords = scrambleWords(word1, word2);
        if (messageBox != null) {
            messageBox.setText(scrambledWords);
        }

        for(int i = scrambledWords.length() - 1; i >= 0; i--){
            stackedLayout.push(new LetterTile(this, scrambledWords.toCharArray()[i]));
        }

        return true;
    }

    private String scrambleWords(String word1, String word2){
        StringBuilder scrambledString = new StringBuilder();
        int c1 = 0;
        int c2 = 0;
        for(int i = 0; i < word1.length() + word2.length(); i++){

            if(random.nextInt(2)==0 && c1 < word1.length()){
                scrambledString.append(word1.toCharArray()[c1++]);
            } else if(random.nextInt(2)==1 && c2 < word2.length()){
                scrambledString.append(word2.toCharArray()[c2++]);
            }

            if(c1 == word1.length()){
                scrambledString.append(word2.substring(c2));
                break;
            }
            if(c2 == word2.length()){
                scrambledString.append(word1.substring(c1));
                break;
            }
        }

        return scrambledString.toString();
    }

    public boolean onUndo(View view) {
        if(!palcedTiles.empty()){
            LetterTile lastTile = palcedTiles.pop();
            lastTile.moveToViewGroup(stackedLayout);
            return true;
        } else {
            return false;
        }
    }
}
