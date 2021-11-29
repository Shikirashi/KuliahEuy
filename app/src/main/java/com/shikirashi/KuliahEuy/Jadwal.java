package com.shikirashi.KuliahEuy;

import java.util.Date;

public class Jadwal {

    private String Judul;
    private String Desc;
    private String Hari;
    private Date waktu;

    public Jadwal(){

    }

    public Jadwal(String judul, String desc, String hari, Date waktu){
        this.Judul = judul;
        this.Desc = desc;
        this.Hari = hari;
        this.waktu = waktu;
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

    public String getHari() {
        return Hari;
    }

    public void setHari(String hari) {
        Hari = hari;
    }

    public Date getWaktu() {
        return waktu;
    }

    public void setWaktu(Date waktu) {
        this.waktu = waktu;
    }
}
