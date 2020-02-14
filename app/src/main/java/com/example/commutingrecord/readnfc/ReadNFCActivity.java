package com.example.commutingrecord.readnfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.commutingrecord.R;
import com.example.commutingrecord.main.MainActivity;
import com.example.commutingrecord.storage.CommutedFirebase;
import com.example.commutingrecord.storage.CommutedStorage;
import com.example.commutingrecord.vo.CommutingRecordDTO;
import com.example.commutingrecord.vo.UserInfoDTO;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReadNFCActivity extends AppCompatActivity {

    private TextView nfcCountTv, nfcAnnounce;
    private CountDownTimer countDownTimer;
    private Date date;
    private int status;
    private Intent intent;
    private CommutedStorage commutedStorage;
    private ImageView nfcPhoneImg;

    // Timer
    private final int MILLISINFUTURE  = 60 * 1000; // 60초
    private final int MILLISINFUTURE2 = 2 * 1000; // 2초
    private final int COUNT_DOWN_INTERVAL = 1000; // onTick 메소드를 호출할 간격 (1초)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_nfc);

        nfcCountTv = findViewById(R.id.nfc_count_tv);
        nfcAnnounce = findViewById(R.id.nfc_announce);
        nfcPhoneImg = findViewById(R.id.nfc_phone_img);

        intent = getIntent();
        if (intent != null){
            status = intent.getExtras().getInt("status");
        }

        countDownTimer();

    }

    public void countDownTimer(){

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long nfcIDCount = millisUntilFinished / 1000;
                if ((nfcIDCount >= 10)){
                    nfcCountTv.setText("" + nfcIDCount);
                } else {
                    nfcCountTv.setText("0" + nfcIDCount);
                }
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                finish();
            }
        }.start();

    }

    public void afterChangeImageTimer(){
        countDownTimer = new CountDownTimer(MILLISINFUTURE2, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
//                long count = millisUntilFinished / 1000;
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                finish();
            }
        }.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String byteArrayToHexString = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            intent = new Intent(ReadNFCActivity.this, MainActivity.class);

            commutedStorage = new CommutedFirebase();
            commutedStorage.getCompanyInfo(new CommutedStorage.OnDataCompanyReceivedListener() {
                @Override
                public void OnDataReceived(List<Map<String, String>> companyList) {
                    for (Map<String, String> map : companyList) {
                        for (String key : map.keySet()) {
                            String uid = FirebaseAuth.getInstance().getUid();
                            commutedStorage = new CommutedFirebase();
                            commutedStorage.getUserInfo(uid, new CommutedStorage.OnDataUserInfoReceivedListener() {
                                @Override
                                public void OnDataReceived(List<UserInfoDTO> userNameList) {

                                    for (UserInfoDTO s : userNameList) {
                                        if (uid.equals(s.getUid())) {
                                            if (map.get(key).equals(byteArrayToHexString) && key.equals(s.getUserCompany())) {
                                                date = Calendar.getInstance().getTime();
                                                String monthAndYearFormat = new SimpleDateFormat("yy년MM월", Locale.getDefault()).format(date);
                                                String dayFormat = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
                                                String timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
                                                String name = s.getUserName();
                                                if (status == 1) {
                                                    commutedStorage = new CommutedFirebase();
                                                    commutedStorage.insertCommutingRecord(timeFormat, byteArrayToHexString, monthAndYearFormat, dayFormat, name);
                                                    changeImage();
                                                    afterChangeImageTimer();

                                                } else if (status == 2) {
                                                    commutedStorage = new CommutedFirebase();
                                                    commutedStorage.updateLeaveWork(timeFormat, byteArrayToHexString, monthAndYearFormat, dayFormat, name);
                                                    changeImage();
                                                    afterChangeImageTimer();

                                                }
                                            } else if (!map.get(key).equals(byteArrayToHexString) && key.equals(s.getUserCompany())) {
                                                Toast.makeText(getApplicationContext(), "등록된 TAG를 사용해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                }
                            });
                        }
                    }
                }
            });

            countDownTimer.cancel();

//            finish();
        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }
                // Process the messages array.

            }
        }
    }

    private void changeImage(){
        nfcPhoneImg.setImageResource(R.drawable.ic_check_white);
        nfcAnnounce.setVisibility(View.VISIBLE);
        nfcCountTv.setVisibility(View.INVISIBLE);
        nfcAnnounce.setText("체크 완료");
        nfcAnnounce.setTextSize(24);
    }


    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }


}
