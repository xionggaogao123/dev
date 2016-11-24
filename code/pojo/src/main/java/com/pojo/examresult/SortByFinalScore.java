package com.pojo.examresult;

import java.util.Comparator;

/**
 * Created by fl on 2015/7/16.
 */
public class SortByFinalScore implements Comparator {
    public int compare(Object o1, Object o2) {
        ScoreDTO s1 = (ScoreDTO) o1;
        ScoreDTO s2 = (ScoreDTO) o2;
        Double num1 = s1.getFinalScore()==null ? 0 :s1.getFinalScore();
        Double num2 = s2.getFinalScore()==null ? 0 :s2.getFinalScore();
        return num2.compareTo(num1);
    }
}
