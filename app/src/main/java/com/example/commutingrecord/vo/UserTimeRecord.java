package com.example.commutingrecord.vo;

import androidx.annotation.Nullable;

public class UserTimeRecord {

    private String name;
    private String startTime;
    private String endTime;

    public UserTimeRecord(){

    }

    public UserTimeRecord(String name, @Nullable String startTime, @Nullable String endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // getter

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
