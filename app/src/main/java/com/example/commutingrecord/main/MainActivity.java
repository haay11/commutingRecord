package com.example.commutingrecord.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.commutingrecord.R;
import com.example.commutingrecord.management.ManagementActivity;
import com.example.commutingrecord.readnfc.ReadNFCActivity;
import com.example.commutingrecord.storage.CommutedFirebase;
import com.example.commutingrecord.storage.CommutedStorage;
import com.example.commutingrecord.vo.TimeRecord;
import com.example.commutingrecord.vo.UserInfoDTO;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.commutingrecord.R.color.purpleish_blue;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Toolbar toolbar;
    private MenuItem menuItem;
    private CommutedStorage commutedStorage;
    private String uid = FirebaseAuth.getInstance().getUid();
    private TextView mainText, attendanceTime, attendanceTvCheck, leaveWorkTime, leaveWorkTvCheck, announceTv;
    private ImageView attendance, leaveWork;
    private Date calendar = Calendar.getInstance().getTime();
    private String dateFormat, timeFormat, monthAndYearFormat, dayFormat;
    private int color;
    private String company, nfcID;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        menuItem = menu.findItem(R.id.expanded_menu);
        commutedStorage = new CommutedFirebase();
        commutedStorage.getUserInfo(uid, new CommutedStorage.OnDataUserInfoReceivedListener() {
            @Override
            public void OnDataReceived(List<UserInfoDTO> userNameList) {

                for (UserInfoDTO userInfoDTO : userNameList){
                    if (uid.equals(userInfoDTO.getUid())){
                        if (userInfoDTO.getAuthority()==0){
                            menuItem.setVisible(false);
                        }
                    }
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.expanded_menu){
            intent = new Intent(MainActivity.this, ManagementActivity.class);
            if (company != null && nfcID != null){
                intent.putExtra("company", company);
                intent.putExtra("nfcID", nfcID);
            }
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        attendanceTime = findViewById(R.id.main_attendance_time);
        attendanceTvCheck = findViewById(R.id.main_attendance_tv_check);
        leaveWorkTime = findViewById(R.id.main_leave_work_time);
        leaveWorkTvCheck = findViewById(R.id.main_leave_work_tv_check);
        attendance = findViewById(R.id.main_attendance);
        leaveWork = findViewById(R.id.main_leave_work);
        announceTv = findViewById(R.id.main_announce);


        mainText = findViewById(R.id.main_text);
        commutedStorage = new CommutedFirebase();
        commutedStorage.getUserInfo(uid, new CommutedStorage.OnDataUserInfoReceivedListener() {
            @Override
            public void OnDataReceived(List<UserInfoDTO> userNameList) {
                for (UserInfoDTO userInfoDTO : userNameList){
                    if (uid.equals(userInfoDTO.getUid())){
                        mainText.setText("반갑습니다. " + userInfoDTO.getUserName() + "님");
                        companyData(userInfoDTO.getUserCompany(), userInfoDTO.getNfcID());
                        commutedStorage = new CommutedFirebase();
                        commutedStorage.getRecordList(userInfoDTO.getNfcID(), new CommutedStorage.OnDataRecordReceivedListener() {
                            @Override
                            public void OnDataReceived(Map<String, List<Map<String, List<Map<String, TimeRecord>>>>> recordList) {
                                monthAndYearFormat = new SimpleDateFormat("yy년MM월", Locale.getDefault()).format(calendar);

                                dayFormat = new SimpleDateFormat("dd", Locale.getDefault()).format(calendar);
                                for (String key : recordList.keySet()) {
                                if (key.equals(monthAndYearFormat)) {
                                    for (Map<String, List<Map<String, TimeRecord>>> stringListMap : recordList.get(key)) {
                                        for (String key1 : stringListMap.keySet()) {

                                            if (key1.equals(dayFormat)) {
                                                for (Map<String, TimeRecord> mapList : stringListMap.get(key1)) {

                                                    for (String k : mapList.keySet()) {

                                                        if (k.equals(userInfoDTO.getUserName())){

                                                            if (mapList.get(k).getStartTime() != null) {
                                                                attendanceTvCheck.setText(mapList.get(k).getStartTime());
                                                                color = 1;
                                                                changeColor(color);
                                                            } else if (mapList.get(k).getStartTime() == null) {
                                                                attendanceTvCheck.setText("출근하기");
                                                                announceTv.setText("출근하기 버튼을 눌러주세요.");
                                                                color = 0;
                                                                changeColor(color);
                                                            }
                                                        if (mapList.get(k).getEndTime() != null) {
                                                            color = 3;
                                                            changeColor(color);
                                                            leaveWorkTvCheck.setText(mapList.get(k).getEndTime());
                                                            announceTv.setText("");
                                                        } else if (mapList.get(k).getEndTime() == null) {
                                                            color = 2;
                                                            changeColor(color);
                                                            leaveWorkTvCheck.setText("퇴근하기");
                                                            announceTv.setText("퇴근하기 버튼을 눌러주세요.");
                                                        }
                                                    }
                                                    }
                                                }
                                            } else {
                                                attendanceTvCheck.setText("출근하기");
                                                announceTv.setText("출근하기 버튼을 눌러주세요.");
                                                leaveWorkTvCheck.setText("퇴근하기");
                                                color = 0;
                                                changeColor(color);
                                                color = 2;
                                                changeColor(color);
                                            }
                                        }
                                    }
                                }
                            }
                            }
                        });
                        }
                    }
                }
            });



        dateFormat = new SimpleDateFormat("MM.dd(EE)", Locale.getDefault()).format(calendar);
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar);

        attendanceTime.setText(dateFormat + " " + "출근");
        leaveWorkTime.setText(dateFormat + " " + "퇴근");
        attendanceTvCheck.setText(timeFormat);
        leaveWorkTvCheck.setText(timeFormat);

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendanceTvCheck.getText().toString().equals("출근하기")){
                    intent = new Intent(MainActivity.this, ReadNFCActivity.class);
                    intent.putExtra("status", 1);
                    startActivity(intent);
                } else {

                }
            }
        });
        leaveWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendanceTvCheck.getText().toString().equals("출근하기") && leaveWorkTvCheck.getText().toString().equals("퇴근하기")){
                    Toast.makeText(getApplicationContext(), "아직 출근 전입니다.", Toast.LENGTH_SHORT).show();
                } else if (!(attendanceTvCheck.getText().toString().equals("출근하기")) && leaveWorkTvCheck.getText().toString().equals("퇴근하기")){
                    intent = new Intent(MainActivity.this, ReadNFCActivity.class);
                    intent.putExtra("status", 2);
                    startActivity(intent);
                } else {}
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    public void changeColor(int color){
        // color == 0 출근 전, color == 1 출근 후, color == 2 퇴근 전, color == 3 퇴근 후
        switch (color) {
            case 0 :
                attendance.setImageResource(R.drawable.ic_btn_white);
                attendanceTime.setTextColor(purpleish_blue);
                attendanceTvCheck.setTextColor(purpleish_blue);
                break;
            case 1 :
                attendance.setImageResource(R.drawable.ic_btn_blue);
                attendanceTime.setTextColor(R.color.white_two);
                attendanceTvCheck.setTextColor(R.color.white_two);
                break;
            case 2 :
                leaveWork.setImageResource(R.drawable.ic_btn_white);
                leaveWorkTime.setTextColor(R.color.purpleish_blue);
                leaveWorkTvCheck.setTextColor(R.color.purpleish_blue);
                break;
            case 3 :
                leaveWork.setImageResource(R.drawable.ic_btn_blue);
                leaveWorkTime.setTextColor(R.color.white_two);
                leaveWorkTvCheck.setTextColor(R.color.white_two);
                break;

        }

    }
    public void companyData(String company, String nfcID){
        this.company = company;
        this.nfcID = nfcID;

    }

}
