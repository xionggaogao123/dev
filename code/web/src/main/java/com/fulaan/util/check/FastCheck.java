package com.fulaan.util.check;

import java.util.HashSet;

/**
 * Created by admin on 2016/12/23.
 */
public class FastCheck{
    public static HashSet<String> hash = new HashSet<String>();
    public static byte[] fastCheck = new byte[65536];
    private static byte[] fastLength = new byte[65536];
    private static boolean[] charCheck = new boolean[65536];
    private static boolean[] endCheck = new boolean[65536];

    private static int maxWordLength = 0;
    private static int minWordLength = Integer.MAX_VALUE;

    public static void addWord(String word) {
        maxWordLength = Math.max(maxWordLength, word.length());
        minWordLength = Math.min(minWordLength, word.length());

        for (int i = 0; i < 7 && i < word.length(); i++) {
            fastCheck[word.charAt(i)] |= (byte) (1 << i);
        }
        for (int i = 7; i < word.length(); i++) {
            fastCheck[word.charAt(i)] |= 0x80;
        }

        if (word.length() == 1) {
            charCheck[word.charAt(0)] = true;
        } else {
            fastLength[word.charAt(0)] |= (byte) (1 << (Math.min(7, word.length() - 2)));
            endCheck[word.charAt(word.length() - 1)] = true;
            hash.add(word);
        }
    }

    public static boolean hasBadWord(String text) {
        int index = 0;
        while (index < text.length()) {
            int count = 1;
            if (index > 0 || (fastCheck[text.charAt(index)] & 1) == 0) {
                while (index < text.length() - 1 && (fastCheck[text.charAt(++index)] & 1) == 0) ;
            }
            char begin = text.charAt(index);
            if (minWordLength == 1 && charCheck[begin]) {
                return true;
            }
            for (int j = 1; j <= Math.min(maxWordLength, text.length() - index - 1); j++) {
                char current = text.charAt(index + j);
                if ((fastCheck[current] & 1) == 0) {
                    ++count;
                }
                if ((fastCheck[current] & (1 << Math.min(j, 7))) == 0) {
                    break;
                }
                if (j + 1 >= minWordLength) {
                    if ((fastLength[begin] & (1 << Math.min(j - 1, 7))) > 0 && endCheck[current]) {
                        if (hash.contains(text.substring(index, index + j + 1))) {
                            return true;
                        }
                    }
                }
            }
            index += count;
        }
        return false;
    }

    public static String replaceWith(String text, char mark) {
        int index = 0;
        char[] ca = text.toCharArray();
        while (index < text.length()) {
            int count = 1;
            if (index > 0 || (fastCheck[text.charAt(index)] & 1) == 0) {
                while (index < text.length() - 1 && (fastCheck[text.charAt(++index)] & 1) == 0) ;
            }
            char begin = text.charAt(index);
            if (minWordLength == 1 && charCheck[begin]) {
                ca[index] = mark;
                index++;
                continue;
            }
            for (int j = 1; j <= Math.min(maxWordLength, text.length() - index - 1); j++) {
                char current = text.charAt(index + j);
                if ((fastCheck[current] & 1) == 0) {
                    ++count;
                }
                if ((fastCheck[current] & (1 << Math.min(j, 7))) == 0) {
                    break;
                }
                if (j + 1 >= minWordLength) {
                    if ((fastLength[begin] & (1 << Math.min(j - 1, 7))) > 0 && endCheck[current]) {
                        if (hash.contains(text.substring(index, index + j + 1))) {
                            for(int m = index;m<(index+j+1);m++){
                                ca[m] = mark;
                            }
                            break;
                        }
                    }
                }
            }
            index += count;
        }
        StringBuilder sb = new StringBuilder();
        for(char c:ca){
            sb.append(c);
        }
        return sb.toString();
    }

}