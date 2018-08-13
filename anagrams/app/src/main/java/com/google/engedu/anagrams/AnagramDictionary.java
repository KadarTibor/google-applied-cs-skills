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

package com.google.engedu.anagrams;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private List<String> wordList;
    private Set<String> wordSet;
    private Map<String, List<String>> lettersToWord;
    private Map<Integer, List<String>> sizeToWord;
    private int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWord = new HashMap<>();
        wordLength = DEFAULT_WORD_LENGTH;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            if (lettersToWord.containsKey(sortLetters(word))) {
                lettersToWord.get(sortLetters(word)).add(word);
            } else {
                lettersToWord.put(sortLetters(word), new ArrayList<>(Arrays.asList(word)));
            }
            if (sizeToWord.get(word.length()) != null) {
                sizeToWord.get(word.length()).add(word);
            } else {
                sizeToWord.put(word.length(), new ArrayList<>(Arrays.asList(word)));
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public List<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        List<String> result = new ArrayList<>();
        char letter = 'a';
        for (int i = 0; i < 26; i++) {
            List<String> partialAnagrams = getAnagrams(sortLetters(word + letter));
            if (partialAnagrams != null) {
                result.addAll(partialAnagrams);
            }
            letter++;
        }
        return result;
    }


    public List<String> getAnagramsWithTwoMoreLetters(String word) {
        List<String> result = new ArrayList<>();
        char letter1 = 'a';
        for (int i = 0; i < 26; i++) {
            char letter2 = 'a';
            for(int j = 0; j < 26; j++){
                List<String> partialAnagrams = getAnagrams(sortLetters(word + letter1 + letter2));
                if (partialAnagrams != null) {
                    result.addAll(partialAnagrams);
                }
                letter2++;
            }
            letter1++;
        }
        return result;
    }

    public String pickGoodStarterWord() {
        List<String> anagrams = new ArrayList<>();
        String word = "";
        while (anagrams.size() <= MIN_NUM_ANAGRAMS) {
            word = wordList.get(Math.abs(random.nextInt()) % wordList.size());
            if (word.length() < wordLength) {
                continue;
            } else {
                anagrams = getAnagramsWithOneMoreLetter(word);

            }
            wordLength = wordLength + 1 > MAX_WORD_LENGTH ? wordLength : wordLength++;
        }
        return word;
    }

    private String sortLetters(String input) {
        char[] charArray = input.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);

    }
}
