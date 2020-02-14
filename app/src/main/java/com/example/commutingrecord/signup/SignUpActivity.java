package com.example.commutingrecord.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.commutingrecord.R;
import com.example.commutingrecord.main.MainActivity;
import com.example.commutingrecord.signin.SignInActivity;
import com.example.commutingrecord.storage.CommutedFirebase;
import com.example.commutingrecord.storage.CommutedStorage;
import com.example.commutingrecord.vo.UserInfoDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle, emailWarningTv, pwWarningTv, pwCheckWarningTv;
    private EditText signUpEmailEd, signUpPwEd,signUpCheckPwEd, signUpNameEd,
            signUpPositionEd, signUpDepartmentEd;
    private Spinner signUpCompanySpinner;
    private Button emailCheckBtn, signUpCancelBtn, signUpBtn;
    private ImageView emailWarningImg,pwWarningImg, pwCheckWarningImg;
    private Intent intent;
        
    private CommutedStorage commutedStorage;

    public String email = "";
    public String password = "" ;
    public String passwordCheck = "" ;
    public String nameEd = "" ;
    public String departmentEd = "" ;
    public String positionEd = "" ;
    public String company;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");

    // 이메일 정규식
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{4,16}$", Pattern.CASE_INSENSITIVE);

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // toolbar 설정
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("회원가입");
        toolbarTitle.setTextColor(getResources().getColor(R.color.dark));
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.ic_back_key_btn);
        toolbar.setBackgroundColor(getResources().getColor(R.color.pale_grey));

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        emailWarningTv = findViewById(R.id.email_warning_tv);
        pwWarningTv = findViewById(R.id.pw_warning_tv);
        pwCheckWarningTv = findViewById(R.id.pw_check_warning_tv);
        emailWarningImg = findViewById(R.id.email_warning_img);
        pwWarningImg = findViewById(R.id.pw_warning_img);
        pwCheckWarningImg = findViewById(R.id.pw_check_warning_img);

        signUpEmailEd = findViewById(R.id.sign_up_email_ed);
        signUpPwEd = findViewById(R.id.sign_up_pw_ed);
        signUpCheckPwEd = findViewById(R.id.sign_up_check_pw_ed);
        signUpNameEd = findViewById(R.id.sign_up_name_ed);
        signUpDepartmentEd = findViewById(R.id.sign_up_department_ed);
        signUpPositionEd = findViewById(R.id.sign_up_position_ed);
        signUpCompanySpinner = findViewById(R.id.sign_up_company_spinner);
        signUpCancelBtn = findViewById(R.id.sign_up_cancel_btn);

        signUpBtn = findViewById(R.id.sign_up_btn2);
        emailCheckBtn = findViewById(R.id.email_check_btn);

        email = signUpEmailEd.getText().toString().trim();
        password = signUpPwEd.getText().toString().trim();


        passwordCheck = signUpCheckPwEd.getText().toString().trim();
        nameEd = signUpNameEd.getText().toString().trim();

        positionEd = signUpPositionEd.getText().toString().trim();

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        commutedStorage = new CommutedFirebase();

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.add("업체명 선택");
        dataAdapter.add("(주)레이보");
        signUpCompanySpinner.setAdapter(dataAdapter);
        signUpCompanySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("업체명 선택")){
                    Toast.makeText(SignUpActivity.this, "업체명을 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    company = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        emailCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = signUpEmailEd.getText().toString().trim();
                getUserEmail(email);
            }

        });


        signUpEmailEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailCheck(v);
                if (hasFocus){

                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        emailWarningImg.setVisibility(View.INVISIBLE);
                        emailWarningTv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        signUpPwEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                pwCheck(v);
                if (hasFocus) {
                    if (Patterns.EMAIL_ADDRESS.matcher(password).matches()) {
                        pwWarningImg.setVisibility(View.INVISIBLE);
                        pwWarningTv.setVisibility(View.INVISIBLE);
                    } else if (isValidCheckPassword()==true){
                        pwWarningImg.setVisibility(View.INVISIBLE);
                        pwWarningTv.setVisibility(View.INVISIBLE);
                    }
                }
            }

        });

        signUpCheckPwEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    pwDoubleCheck(v);
                } else if (isValidCheckPassword()==true){
                    pwCheckWarningImg.setVisibility(View.INVISIBLE);
                    pwCheckWarningTv.setVisibility(View.INVISIBLE);
                }
            }
        });

        // 회원가입
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(v);

            }
        });

        signUpPositionEd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_ENTER){

                        try {
                            signUp(v);
                            return true;
                        }catch (NullPointerException e){
                            getCurrentFocus();
                        }
                    }
                }
                return false;
            }
        });

        signUpCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void emailCheck(View view){

        signUpEmailEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = s.toString().trim();
                if (!isValidEmail()||emailCheckBtn.getText().equals("확인")){
                    emailWarningImg.setVisibility(View.VISIBLE);
                    emailWarningTv.setVisibility(View.VISIBLE);
                } else if  (emailCheckBtn.getText().equals("인증")){
                    emailWarningImg.setVisibility(View.INVISIBLE);
                    emailWarningTv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void pwCheck(View view){

        signUpPwEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString().trim();
                if (!isValidCheckPassword()&&(password.length()<6)){
                    pwWarningImg.setVisibility(View.VISIBLE);
                    pwWarningTv.setVisibility(View.VISIBLE);
                }else {
                    pwWarningImg.setVisibility(View.INVISIBLE);
                    pwWarningTv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void pwDoubleCheck(View view){

        signUpCheckPwEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordCheck = s.toString().trim();
                if ((!isValidCheckPassword())){
                    pwCheckWarningTv.setVisibility(View.VISIBLE);
                    pwCheckWarningImg.setVisibility(View.VISIBLE);
                } else {
                    pwCheckWarningImg.setVisibility(View.INVISIBLE);
                    pwCheckWarningTv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void signUp(View view){
        email = signUpEmailEd.getText().toString().trim();
        password = signUpPwEd.getText().toString().trim();

        passwordCheck = signUpCheckPwEd.getText().toString().trim();
        nameEd = signUpNameEd.getText().toString().trim();

        departmentEd = signUpDepartmentEd.getText().toString().trim();
        positionEd = signUpPositionEd.getText().toString().trim();


        if (!emailCheckBtn.getText().equals("인증")){
            Toast.makeText(SignUpActivity.this, "사용 가능한 이메일인지 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else if (isValidEmail() && isValidPassword() && isValidCheckPassword() && (nameEd != "")
                && (company != "") && (departmentEd != "") && (positionEd != "") &&(!company.equals("업체명 선택"))){
            createUser(email, password);
        } else {
            Toast.makeText(SignUpActivity.this, "입력창을 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword() {
        if (password.isEmpty()){
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()){
            // 비밀번호 형식 불일치
            return false;
        }
        else return true;
    }

    // 비밀번호 체크
    private boolean isValidCheckPassword(){
        if(passwordCheck.isEmpty()){
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordCheck).matches()){
            return false;
        } else if (!password.equals(passwordCheck)){
            return false;
        } else return true;
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()){
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // 이메일 형식 불일치
            return false;
        } else return true;
    }

    // 회원가입
    private void createUser(String email, String Password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        passwordCheck = signUpCheckPwEd.getText().toString().trim();
                        nameEd = signUpNameEd.getText().toString().trim();
                        departmentEd = signUpDepartmentEd.getText().toString().trim();
                        positionEd = signUpPositionEd.getText().toString().trim();
                        if (task.isSuccessful()) {
                            // 회원 가입 성공
                            CommutedStorage commutedStorage = new CommutedFirebase();
                            Toast.makeText(SignUpActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            commutedStorage.getCompanyInfo(new CommutedStorage.OnDataCompanyReceivedListener() {
                                @Override
                                public void OnDataReceived(List<Map<String, String>> companyList) {
                                    for (Map<String, String> map : companyList) {
                                        for (String key : map.keySet()) {
                                            if (key.equals(company)){
                                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                commutedStorage.insertUserInfo(new UserInfoDTO(uid, email, nameEd, company, positionEd, departmentEd, 0, map.get(key)));
                                                commutedStorage.insertCompanyEmployees(uid, nameEd, company);
                                            }
                                        }
                                    }
                                }
                            });

                            startActivity(intent);
                            finish();
                        } else if (!task.isSuccessful()){
                            // 회원 가입 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



    // 이메일 중복 확인
    public void getUserEmail(String email){
        commutedStorage = new CommutedFirebase();
        commutedStorage.getUserInfo(null, new CommutedStorage.OnDataUserInfoReceivedListener() {
            @Override
            public void OnDataReceived(List<UserInfoDTO> userList) {
                for (UserInfoDTO userInfo : userList){
                    if (userInfo.getUserEmail().equals(email)){
                        Toast.makeText(SignUpActivity.this, "이미 사용중인 이메일 입니다.", Toast.LENGTH_LONG).show();
                        return ;
                    }
                }
                if (email == null || email.equals("")){
                    Toast.makeText(SignUpActivity.this, "이메일을 인증해주세요.", Toast.LENGTH_LONG).show();

                } else if (!isValidEmail()){
                    Toast.makeText(SignUpActivity.this, "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT ).show();
                }else{

                    Toast.makeText(SignUpActivity.this, "사용 가능한 이메일 입니다.", Toast.LENGTH_LONG).show();
                    emailCheckBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_color_darkgray));
                    emailCheckBtn.setText("인증");
                    emailWarningImg.setVisibility(View.INVISIBLE);
                    emailWarningTv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



    public void checkEmail(String email){
        email = signUpEmailEd.getText().toString().trim();
    }
}

