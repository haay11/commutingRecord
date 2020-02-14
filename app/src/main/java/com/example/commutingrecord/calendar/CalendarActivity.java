package com.example.commutingrecord.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.commutingrecord.R;
import com.example.commutingrecord.commutedrecord.CommutedRecord;
import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Date;

public class CalendarActivity extends AppCompatActivity implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;
    private Button calCheck, calCancel;
    private Intent intent;
    private Date day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_pick);


        // Gets the calendar from the view
        robotoCalendarView = findViewById(R.id.robotoCalendarPicker);


        calCheck = findViewById(R.id.cal_check);
        calCancel = findViewById(R.id.cal_cancel);

        calCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                intent = new Intent(CalendarActivity.this, CommutedRecord.class);


                if (day != null){
                    intent.putExtra("date", day);
                    setResult(CommutedRecord.RESULT_CODE, intent);
                    finish();

                } else {
                    Toast.makeText(CalendarActivity.this, "조회하실 날짜를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.setDate(new Date());

    }


    @Override
    public Date onDayClick(Date date) {
          day = date;
        return day;

    }

    public String[] getMonth(String date) {

        String[] monthE = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] monthN = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] dayList1 = date.split(" ");
        String[] month = new String[2];
        month[0] = dayList1[1];
        for (int i = 0; i < monthE.length; i++) {
            if (month[0].equals(monthE[i])) {
                month[0] = String.valueOf(monthN[i]);
                break;
            }
        }
        month[1] = dayList1[2];

        return month;
    }
}