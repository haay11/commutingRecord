package com.example.commutingrecord.vo;

import androidx.annotation.Nullable;

public class CommutingRecordDTO {


    private String nfcID;
    private String monthAndYear;
    private String day;
    private String name;
    private String startTime;
    private String endTime;

    public CommutingRecordDTO(){

    }

    public CommutingRecordDTO(@Nullable String nfcID, String monthAndYear, String day, String name, String startTime, @Nullable String endTime){
        this.nfcID = nfcID;
        this.monthAndYear = monthAndYear;
        this.day = day;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // getter
    public String getNfcID() {
        return nfcID;
    }

    public String getMonthAndYear() {
        return monthAndYear;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    // setter

    public void setNfcID(String nfcID) {
        this.nfcID = nfcID;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
