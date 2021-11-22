package com.shikirashi.KuliahEuy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TugasModel {

    private String judul;
    private Date deadline;

    private TugasModel(){

    }

    private TugasModel(String judul, Date deadline){
        this.judul = judul;
        this.deadline = deadline;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeadline() {
        SimpleDateFormat sfd = new SimpleDateFormat(" HH:mm dd-MM-yyyy");
        String strDate = sfd.format(deadline);
        return strDate;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
