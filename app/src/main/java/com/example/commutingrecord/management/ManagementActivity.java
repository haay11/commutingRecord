package com.example.commutingrecord.management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.commutingrecord.R;
import com.example.commutingrecord.commutedrecord.CommutedRecord;

public class ManagementActivity extends AppCompatActivity {

    private Button managementRecord, managementSettings;
    private Intent intent;
    private String company, nfcID;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private MenuItem menuItem;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        menuItem = menu.findItem(R.id.expanded_menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("관리자 페이지");
        toolbar.setNavigationIcon(R.drawable.ic_back_key_white_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        managementRecord = findViewById(R.id.management_record);
        managementSettings = findViewById(R.id.management_settings);
        Intent intent1 = getIntent();
        if (intent1!=null){
            company = intent1.getExtras().getString("company");
            nfcID = intent1.getExtras().getString("nfcID");
        }

        managementRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ManagementActivity.this, CommutedRecord.class);
                if (company!=null && nfcID!=null){
                    intent.putExtra("company", company);
                    intent.putExtra("nfcID", nfcID);
                }
                startActivity(intent);
            }
        });

        managementSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
