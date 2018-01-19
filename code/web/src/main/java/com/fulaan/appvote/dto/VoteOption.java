package com.fulaan.appvote.dto;

/**
 * Created by scott on 2018/1/19.
 */
public class VoteOption {
    private int number;
    private String option;

    public VoteOption(){

    }

    public VoteOption(int  number,String option){
        this.number=number;
        this.option=option;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
