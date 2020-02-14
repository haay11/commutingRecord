package com.example.commutingrecord.storage;

import androidx.annotation.NonNull;

import com.example.commutingrecord.vo.TimeRecord;
import com.example.commutingrecord.vo.UserInfoDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommutedFirebase implements CommutedStorage{
    @Override
    public void insertCompany(String nfcID,String uid,String name,String company) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/companyList/");
        reference.child(nfcID).child(company).child(uid).setValue(name);
    }

    @Override
    public void insertUserInfo(UserInfoDTO userInfoDTO) {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/userInfo/");

        reference.child(uid).setValue(userInfoDTO);
    }

    @Override
    public void insertCompanyEmployees(String uid, String name, String company) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/companyEmployees/");

        reference.child(company).child(name).setValue(uid);
    }

    @Override
    public void insertCommutingRecord(String startTime, String nfcID, String monthAndYear, String day, String name) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/recordList/");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child(nfcID).child(monthAndYear).child(day)
                .child(name).child("startTime").setValue(startTime);
    }

    @Override
    public void updateLeaveWork(String endTime, String nfcID, String monthAndYear, String day, String name) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/recordList/");
        reference.child(nfcID).child(monthAndYear).child(day)
                .child(name).child("endTime").setValue(endTime);
    }

    @Override
    public void getUserInfo(String uid, final OnDataUserInfoReceivedListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserInfoDTO> userInfo = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    userInfo.add(dataSnapshot1.getValue(UserInfoDTO.class));
                } listener.OnDataReceived(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getCompanyInfo(OnDataCompanyReceivedListener listener) {
        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        database.getReference().child("/companyList/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Map<String, String>> companyList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Map<String, String> elements = new HashMap<>();
                    companyList.add(elements);
                    elements.put(Objects.requireNonNull(dataSnapshot1.getKey()), (String) Objects.requireNonNull(dataSnapshot1.getValue()));
                }

                listener.OnDataReceived(companyList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getRecordList(String nfcID, /*String monthAndYear, String day, String name,*/ OnDataRecordReceivedListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("/recordList/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, List<Map<String, List<Map<String, TimeRecord>>>>> listMap = new HashMap<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (nfcID.equals(dataSnapshot1.getKey())){
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                            List<Map<String, List<Map<String, TimeRecord>>>> maps = new ArrayList<>();
                            listMap.put(Objects.requireNonNull(dataSnapshot2.getKey()), maps);
                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {

                                Map<String, List<Map<String, TimeRecord>>> stringListMap = new HashMap<>();
                                List<Map<String, TimeRecord>> mapList = new ArrayList<>();
                                stringListMap.put(Objects.requireNonNull(dataSnapshot3.getKey()), mapList);

                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
    /*                                Log.e("로그로그88", dataSnapshot4.getKey());
                                    Log.e("로그로그99", String.valueOf(dataSnapshot4.getValue()));*/
                                    Map<String, TimeRecord> recordMap = new HashMap<>();
                                    recordMap.put(dataSnapshot4.getKey(), dataSnapshot4.getValue(TimeRecord.class));
                                    mapList.add(recordMap);
                                } maps.add(stringListMap);
                            }
                        }
                }

                }   listener.OnDataReceived(listMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getCompanyEmployees(String company, OnDataEmployeesReceivedListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("/companyEmployees/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> companyEmployees = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (company!=null){
                    if (company.equals(dataSnapshot1.getKey())){
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            companyEmployees.add(dataSnapshot2.getKey());
                        }
                    }}
                } listener.OnDataReceived(companyEmployees);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
