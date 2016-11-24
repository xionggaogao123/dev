package com.fulaan.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/11/9.
 */
public class Test {
    private static String[] is = new String[] { "1", "2","3", "4", "5", "6", "7", "8", "9","10", "11", "12", "13"};
    private static int total;
    private static int m = 3;
    public static void main(String[] args) {
        List<Integer> iL = new ArrayList<Integer>();
        List<String> arglist = new ArrayList<String>();
        List<String> arglist2 = new ArrayList<String>();
        List<Integer> integer = new ArrayList<Integer>();
        new Test().plzh("", iL,  m,arglist);
        for (String s : arglist) {
            integer = new ArrayList<Integer>();
            String[] cls = s.split(",");
            for (String s2 : cls) {
                integer.add(Integer.valueOf(s2));
                Collections.sort(integer);
            }
            String str = "";
            for (Integer s4 : integer) {
                str += s4.toString() + ",";
            }
            arglist2.add(str);
        }
        HashSet<String> hs = new HashSet<String>(arglist2);
        for (String s7:hs) {
            System.out.println(s7);
        }
        System.out.println("total : " + hs.size());
    }
    private void plzh(String s, List<Integer> iL, int m,List<String> arglist) {
        if(m == 0) {
            arglist.add(s);
            total++;
            return;
        }
        List<Integer> iL2;
        for(int i = 0; i < is.length; i++) {
            iL2 = new ArrayList<Integer>();
            iL2.addAll(iL);
            if(!iL.contains(i)) {
                String str = s + is[i]+",";
                iL2.add(i);
                plzh(str, iL2, m-1,arglist);
            }
        }
    }


}
