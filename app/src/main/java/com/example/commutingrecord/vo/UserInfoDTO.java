package com.example.commutingrecord.vo;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {

    public String userEmail;
    public String userName;
    public String userCompany;
    public String userPosition;
    public String userDepartment;
    public String uid;
    public int authority;
    public String nfcID;

    public UserInfoDTO(){

    }

    public UserInfoDTO(String uid, String userEmail, String userName, String userCompany, String userPosition, String userDepartment, int authority, @Nullable String nfcID){
        this.uid = uid;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userCompany = userCompany;
        this.userPosition = userPosition;
        this.userDepartment = userDepartment;
        this.authority = authority;
        this.nfcID = nfcID;
    }

    // getter

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public String getUid() {
        return uid;
    }

    public int getAuthority() {
        return authority;
    }

    public String getNfcID() {
        return nfcID;
    }

    // setter

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public void setNfcID(String nfcID) {
        this.nfcID = nfcID;
    }
}
