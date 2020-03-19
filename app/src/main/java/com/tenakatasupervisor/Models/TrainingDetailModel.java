package com.tenakatasupervisor.Models;

public class TrainingDetailModel {
    private String title;
    private   String subtitle;
    private String readmore;
    public TrainingDetailModel(String title, String subtitle, String readmore){
        this.title=title;
        this.subtitle=subtitle;
        this.readmore=readmore;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getReadmore() {
        return readmore;
    }

    public void setReadmore(String readmore) {
        this.readmore = readmore;
    }
}
