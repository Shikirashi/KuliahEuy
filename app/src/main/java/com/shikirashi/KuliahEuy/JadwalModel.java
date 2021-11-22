package com.shikirashi.KuliahEuy;

public class JadwalModel {

    private String Judul;
    private String Desc;

    private JadwalModel(){

    }

    private JadwalModel(String judul, String desc){
        this.Judul = judul;
        this.Desc = desc;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        Judul = judul;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
