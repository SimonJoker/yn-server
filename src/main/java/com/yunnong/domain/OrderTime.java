package com.yunnong.domain;

import java.io.Serializable;

/**
 * Created by joker on 2016/4/26.
 */
public class OrderTime implements Serializable {
    private Long tid;
    private Long pid;
    private String date;
    private Integer ten_am;
    private Integer two_pm;
    private Integer three_h_pm;
    private Long ch_date;

    public OrderTime() {
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTen_am() {
        return ten_am;
    }

    public void setTen_am(Integer ten_am) {
        this.ten_am = ten_am;
    }

    public Integer getTwo_pm() {
        return two_pm;
    }

    public void setTwo_pm(Integer two_pm) {
        this.two_pm = two_pm;
    }

    public Integer getThree_h_pm() {
        return three_h_pm;
    }

    public void setThree_h_pm(Integer three_h_pm) {
        this.three_h_pm = three_h_pm;
    }

    public Long getCh_date() {
        return ch_date;
    }

    public void setCh_date(Long ch_date) {
        this.ch_date = ch_date;
    }
}
