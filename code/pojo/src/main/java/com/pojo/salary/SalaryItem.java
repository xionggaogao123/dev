package com.pojo.salary;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/21.
 */
public class SalaryItem {
    private List<SalaryItemDto> debitList;
    private List<SalaryItemDto> sendList;
    private int debit;
    private int send;

    public List<SalaryItemDto> getDebitList() {
        return debitList;
    }

    public void setDebitList(List<SalaryItemDto> debitList) {
        this.debitList = debitList;
    }

    public List<SalaryItemDto> getSendList() {
        return sendList;
    }

    public void setSendList(List<SalaryItemDto> sendList) {
        this.sendList = sendList;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }
}
