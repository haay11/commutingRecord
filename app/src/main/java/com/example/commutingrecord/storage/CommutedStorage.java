package com.example.commutingrecord.storage;

import androidx.annotation.Nullable;

import com.example.commutingrecord.vo.CommutingRecordDTO;
import com.example.commutingrecord.vo.TimeRecord;
import com.example.commutingrecord.vo.UserInfoDTO;

import java.util.List;
import java.util.Map;

public interface CommutedStorage {

    public void insertCompany(String nfcID,String uid,String name, String company);
    public void insertUserInfo(UserInfoDTO userInfoDTO);
    public void insertCompanyEmployees(@Nullable String uid,String name, String company);
    public void insertCommutingRecord(String startTime, String nfcID, String monthAndYear, String day, String name);
    public void updateLeaveWork(String endTime, String nfcID, String monthAndYear, String day, String name);
    public void getUserInfo(@Nullable String uid, OnDataUserInfoReceivedListener listener);
    public void getCompanyInfo(OnDataCompanyReceivedListener listener);
    public void getRecordList(String nfcID,/* String monthAndYear, String day, String name, */OnDataRecordReceivedListener listener);
    public void getCompanyEmployees(String company, OnDataEmployeesReceivedListener listener);


    public interface OnDataUserInfoReceivedListener {
        void OnDataReceived(List<UserInfoDTO> userNameList);
    }
    public interface OnDataCompanyReceivedListener{
        void OnDataReceived(List<Map<String, String>> companyList);
    }
    public interface OnDataRecordReceivedListener{
        void OnDataReceived(Map<String, List<Map<String, List<Map<String, TimeRecord>>>>> recordList);
    }
    public interface OnDataEmployeesReceivedListener{
        void OnDataReceived(List<String> companyEmployees);
    }
}
