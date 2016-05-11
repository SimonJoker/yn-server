package com.yunnong.domain;

import java.io.Serializable;

/**
 * Created by joker on 2016/4/26.
 */
public class ConsultantTem implements Serializable{
    private long pid;
    private String p_name;
    private String image;
    private String description;

    public ConsultantTem() {
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
