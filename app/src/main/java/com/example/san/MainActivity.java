package com.example.san;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv_id, tv_pass;

    Button btn_attend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");

        tv_id.setText(userID);
        tv_pass.setText(userPass);

        btn_attend = findViewById (R.id.btn_attend);
        btn_attend.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, AttendActivity.class);
                startActivity (intent);
            }
        });

    }

    // 두번 뒤로가기 누르면 종료되도록 함
    private long lastTimeBackPressd;

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressd < 1500)
        {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT);
        lastTimeBackPressd = System.currentTimeMillis();
    }
}
