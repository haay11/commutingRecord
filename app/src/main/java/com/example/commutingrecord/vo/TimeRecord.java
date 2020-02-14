package com.example.commutingrecord.vo;

import androidx.annotation.Nullable;

public class TimeRecord {

    String startTime, endTime;

    public TimeRecord(){

    }

    public TimeRecord(@Nullable String startTime, @Nullable String endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // getter


    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    // setter

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
