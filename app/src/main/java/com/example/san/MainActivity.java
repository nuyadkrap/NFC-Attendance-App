package com.example.san;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv_id, tv_name;
    public static String userID;

    Button btn_attend, btn_attendance, btn_time, btn_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        userID = getIntent().getStringExtra("userID");
        tv_id = findViewById(R.id.tv_id);
        tv_name = findViewById(R.id.tv_name);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");

        tv_id.setText(userID);
        tv_name.setText(userName);

        btn_attend = findViewById (R.id.btn_attend);
        btn_attend.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, AttendListActivity.class);
                startActivity (intent);
            }
        });

        btn_time = findViewById (R.id.btn_time);
        btn_time.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent (MainActivity.this, TimetableActivity.class);
                startActivity (intent2);
            }
        });

        btn_attendance = findViewById (R.id.btn_attendance);
        btn_attendance.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent (MainActivity.this, AttendanceActivity.class);
                startActivity (intent3);
            }
        });

        btn_course = findViewById (R.id.btn_course);
        btn_course.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent (MainActivity.this, CourseListActivity.class);
                startActivity (intent4);
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
