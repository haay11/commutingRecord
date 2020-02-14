package com.example.commutingrecord.commutedrecord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commutingrecord.R;
import com.example.commutingrecord.adapter.RecordAdapter;
import com.example.commutingrecord.calendar.CalendarActivity;
import com.example.commutingrecord.storage.CommutedFirebase;
import com.example.commutingrecord.storage.CommutedStorage;
import com.example.commutingrecord.vo.TimeRecord;
import com.example.commutingrecord.vo.UserTimeRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommutedRecord extends AppCompatActivity {

    private LinearLayout calBtn;
    private Intent intent;
    private TextView calTv, toolbarTitle;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecordAdapter recordAdapter;
    private Toolbar toolbar;
    private MenuItem menuItem;
    private List<UserTimeRecord> userTimeRecords;
    private Date date = Calendar.getInstance().getTime();
    private Button recordTodayBtn;

    public String name, selectDate, selectMonthAndYear, selectDay, company, nfcID, dateFormat;
    private CommutedStorage commutedStorage;
    private Date calendar = Calendar.getInstance().getTime();


    // 요청 코드 상수 정의
    public static final int REQUEST_CODE = 1;
    // 응답 코드 상수 정의
    public static final int RESULT_CODE = 2;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null) {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE){

                try {
                    Date calDate = (Date) data.getExtras().get("date");
//                            data.getExtras().getString("date");
                    dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(calDate);

                    calTv.setText(dateFormat);
                    setDate(dateFormat);
                }catch (NullPointerException e){
                    return;
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        menuItem = menu.findItem(R.id.expanded_menu);
        menuItem.setVisible(false);
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commuting_record);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("출퇴 현황");
        toolbar.setNavigationIcon(R.drawable.ic_back_key_white_btn);

        recordTodayBtn = findViewById(R.id.record_today_btn);

        Intent intent1 = getIntent();

        if (intent1 != null){
            company = intent1.getExtras().getString("company");
            nfcID = intent1.getExtras().getString("nfcID");
        }
        calBtn = findViewById(R.id.cal_btn);
        calTv = findViewById(R.id.cal_tv);

        date = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date);

        calTv.setText(dateFormat);
        selectDate = calTv.getText().toString();

        recyclerView = findViewById(R.id.recycle_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (company!=null&&nfcID!=null){
            selectMonthAndYear = new SimpleDateFormat("yy년MM월", Locale.getDefault()).format(date);
            selectDay = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
            setElements(selectMonthAndYear, selectDay);
        }


        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(CommutedRecord.this, CalendarActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        recordTodayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = Calendar.getInstance().getTime();
                selectMonthAndYear = new SimpleDateFormat("yy년MM월", Locale.getDefault()).format(date);
                selectDay = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
                dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date);
                calTv.setText(dateFormat);
                setElements(selectMonthAndYear, selectDay);
            }
        });
    }

    public void setElements(String monthYear, String day){

        commutedStorage = new CommutedFirebase();
        commutedStorage.getCompanyEmployees(company, new CommutedStorage.OnDataEmployeesReceivedListener() {
            @Override
            public void OnDataReceived(List<String> companyEmployees) {

                List<UserTimeRecord> userTimeRecords = new ArrayList<>();

                for (String s : companyEmployees){
                    commutedStorage = new CommutedFirebase();
                    commutedStorage.getRecordList(nfcID, new CommutedStorage.OnDataRecordReceivedListener() {
                        @Override
                        public void OnDataReceived(Map<String, List<Map<String, List<Map<String, TimeRecord>>>>> recordList) {

                            for (String key : recordList.keySet()) {
                                int dateCheck = 0;

                                if (key.equals(monthYear)) {

                                    for (Map<String, List<Map<String, TimeRecord>>> stringListMap : recordList.get(key)) {

                                        for (String key1 : stringListMap.keySet()) {

                                            if (key1.equals(day)&& dateCheck==0) {
                                                    Log.e("ddddd", s + " " + key1 + " " + day);
                                                for (Map<String, TimeRecord> mapList : stringListMap.get(key1)) {

                                                    for (String k : mapList.keySet()) {
                                                        Log.e("ddddd  d", s + " " + key1 + " " + day + "  "+k);
                                                        if (k.equals(s) && mapList.get(k).getStartTime() != null && mapList.get(k).getEndTime()!=null) {
                                                            dateCheck ++;

                                                            userTimeRecords.add(new UserTimeRecord(s, mapList.get(k).getStartTime(), mapList.get(k).getEndTime()));

                                                                } else if (k.equals(s) && mapList.get(k).getStartTime() != null && mapList.get(k).getEndTime()==null ){
                                                            dateCheck ++;

                                                            userTimeRecords.add(
                                                                    new UserTimeRecord(s, mapList.get(k).getStartTime(), null));

                                                            }
                                                        }
                                                    }
                                                if (dateCheck == 0){
                                                    userTimeRecords.add(new UserTimeRecord(s, null, null));
                                                }
                                                dateCheck ++;
                                                Log.e("ddddd  d", s + " " + key1 + " " + day + "  "+dateCheck);
                                                }

                                            }

                                        }

                                    }

                                    if (dateCheck == 0){
                                    userTimeRecords.add(new UserTimeRecord(s, null, null));

                                    }
                                }

                            recordAdapter = new RecordAdapter(userTimeRecords, getApplicationContext());
                            recyclerView.setAdapter(recordAdapter);
                            recordAdapter.notifyDataSetChanged();
                        }
                    });

                }

            }
        });
        recordAdapter = new RecordAdapter(userTimeRecords, getApplicationContext());
        recyclerView.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
    }
    public void setDate(String date){
        selectMonthAndYear = date.substring(2,4) + "년" + date.substring(5,7) + "월";
                selectDay = date.substring(8,10);
        setElements(selectMonthAndYear, selectDay);

    }


}
