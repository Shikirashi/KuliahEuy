package com.shikirashi.KuliahEuy;

public class JadwalModel {

    private String Title;
    private String Desc;

    private JadwalModel(){

    }

    private JadwalModel(String Title, String Desc){
        this.Title = Title;
        this.Desc = Desc;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
