package com.example.commutingrecord.findaccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.commutingrecord.R;
import com.example.commutingrecord.signin.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindAccountActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public TextView toolbarTitle;
    public ImageView titleImg;
    private EditText findAccountEmail;
    private Button findAccountEmailBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        // toolbar 설정
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("비밀번호 재설정");

        titleImg.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findAccountEmail = findViewById(R.id.find_account_email);
        findAccountEmailBtn = findViewById(R.id.find_account_email_btn);

        mAuth = FirebaseAuth.getInstance();
        findAccountEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = findAccountEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(FindAccountActivity.this, "비밀번호를 재설정할 이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(FindAccountActivity.this, "이메일 확인 후 비밀번호를 재설정 하세요.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(FindAccountActivity.this, SignInActivity.class));
                                finish();
                            }else {
                                String message = task.getException().getMessage();
                                Toast.makeText(FindAccountActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}

