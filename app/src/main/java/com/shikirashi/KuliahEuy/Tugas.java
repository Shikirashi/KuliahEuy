package com.shikirashi.KuliahEuy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tugas {

    private String Judul;
    private Date Deadline;
    private Boolean ifDone;

    private Tugas(){

    }

    private Tugas(String judul, Date deadline, Boolean isDone){
        this.Judul = judul;
        this.Deadline = deadline;
        this.ifDone = isDone;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        this.Judul = judul;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date deadline) {
        this.Deadline = deadline;
    }

    public Boolean isIfDone() {
        return ifDone;
    }

    public void setIfDone(Boolean isdone) {
        this.ifDone = isdone;
    }
}
