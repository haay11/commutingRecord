package com.example.commutingrecord.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.commutingrecord.R;
import com.example.commutingrecord.main.MainActivity;
import com.example.commutingrecord.signup.SignUpActivity;
import com.example.commutingrecord.findaccount.FindAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

        private EditText signInIdEd, signInPwEd;
        private Button signUpBtn, signInBtn;
        private TextView findAccountBtn;
        private String email = "";
        private String password = "";
        private Intent intent;

        // 비밀번호 정규식
        private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");

        // 이메일 정규식
        private static final Pattern VALID_EMAIL_ADDRESS_REGEX
                = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        // 파이어베이스 인증 객체 생성
        private FirebaseAuth firebaseAuth;


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_in);

            signInIdEd = findViewById(R.id.sign_in_id_ed);
            signInPwEd = findViewById(R.id.sign_in_pw_ed);
            signUpBtn = findViewById(R.id.sign_up_btn);
            signInBtn = findViewById(R.id.sign_in_btn);
            findAccountBtn = findViewById(R.id.find_account_btn);

            // 파이어베이스 인증 객체 선언
            firebaseAuth = FirebaseAuth.getInstance();

            signInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    signIn(v);
                }
            });
            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(SignInActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            findAccountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(SignInActivity.this, FindAccountActivity.class);
                    startActivity(intent);
                }
            });

            signInPwEd.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(event.getAction() == KeyEvent.ACTION_DOWN){
                        if(keyCode == KeyEvent.KEYCODE_ENTER){
                        }
                    }
                    return false;
                }

            });
        }



        public void signIn(View view){
            email = signInIdEd.getText().toString().trim();
            password = signInPwEd.getText().toString().trim();

            if (isValidEmail() && isValidPassword()){
                loginUser(email, password);
            } else if (!(isValidEmail()||isValidEmail())){
                Toast.makeText(SignInActivity.this, "이메일 or 비밀번호를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
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


        // 로그인
        private void loginUser(String email, String password) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                // 로그인 성공
                                Toast.makeText(SignInActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                                intent = new Intent(SignInActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            } else {
                                // 로그인 실패
                                Toast.makeText(SignInActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
