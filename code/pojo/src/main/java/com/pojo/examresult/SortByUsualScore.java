package com.pojo.examresult;

import java.util.Comparator;

/**
 * Created by fl on 2015/7/16.
 */
public class SortByUsualScore implements Comparator {
    public int compare(Object o1, Object o2) {
        ScoreDTO s1 = (ScoreDTO) o1;
        ScoreDTO s2 = (ScoreDTO) o2;
        Double num1 = s1.getUsualScore()==null ? 0 :s1.getUsualScore();
        Double num2 = s2.getUsualScore()==null ? 0 :s2.getUsualScore();
        return num2.compareTo(num1);
    }
}
