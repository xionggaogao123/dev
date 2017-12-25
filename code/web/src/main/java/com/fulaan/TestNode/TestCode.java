package com.fulaan.TestNode;

/**
 * Created by admin on 2017/12/20.
 */
public class TestCode {

    public static void main(String[] args){
        String s="bxxaaadd";
        System.out.println(longestPalindrome(s));
    }

    public static String longestPalindrome(String s) {
        int top=0;
        int start=0;
        int end=0;
        int tstart=0;
        int tend=0;
        int maxTop=0;
        char record='\0';
        for(int i=0;i<s.length();i++){
            char item=s.charAt(i);
            if(record!='\0'){
                if(item==record){
                    top++;
                    tend=i;
                    if(maxTop<top){
                        maxTop=top;
                        start=tstart;
                        end=tend;
                    }
                }else{
                    top--;
                }
                if(top==0){
                    tstart=i;
                    tend=i;
                    record=item;
                    top++;
                }
            }else{
                top++;
                record=item;
            }
        }
        if(maxTop==0){
            start=tstart;
            end=tend;
        }
        System.out.println(maxTop);
        String result="";
        for(int i=start;i<=end;i++){
            result+=s.charAt(i);
        }
        return result;
    }
}
