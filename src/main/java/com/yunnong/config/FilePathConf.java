package com.yunnong.config;

/**
 * Created by joker on 2016/4/11.
 */
public class FilePathConf {

    private String imageUrl;
    private String imagePath;
    private String conlImage;
    private String userImage;

    public FilePathConf() {
    }

    public FilePathConf(String imageUrl, String imagePath, String conlImage, String userImage) {
        this.imageUrl = imageUrl;
        this.imagePath = imagePath;
        this.conlImage = conlImage;
        this.userImage = userImage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getConlImage() {
        return conlImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getConsultantImagePath(){
        return this.imagePath+this.conlImage;
    }

    public String getConsultantImageUrl(){
        return this.imageUrl+this.conlImage;
    }

    public String getUserImagePath(){
        return this.imagePath+this.userImage;
    }

    public String getUserImageUrl(){
        return this.imageUrl+this.userImage;
    }
}