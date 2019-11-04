package com.example.san;

import androidx.annotation.Nullable;
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
    public static String userID, userState;

    long backKeyPressedTime;

    Button btn_attend, btn_attendance, btn_time, btn_course, notice;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
            Toast.makeText(this, "정보를 불러오는데 실패하였습니다", Toast.LENGTH_LONG).show();
            return;
        }


        if (data == null) {
          //  Toast.makeText(this, "전달된 값이 없습니다", Toast.LENGTH_LONG).show();
            return;
        }


        switch (requestCode) {
            case 100:
                userState = data.getStringExtra("userState");
                break;

            case 101:
                break;

            case 102:
                break;

            case 103:
                break;

            case 104:
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        userID = getIntent().getStringExtra("userID");
        userState = getIntent().getStringExtra("userState");
        tv_id = findViewById(R.id.tv_id);
        tv_name = findViewById(R.id.tv_name);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");

        tv_id.setText(userID);
        tv_name.setText(userName);

        notice = findViewById(R.id.notice);

        if (userState.equals("교수")) {
            notice.setVisibility(View.VISIBLE);
        }

        btn_attend = findViewById(R.id.btn_attend);
        btn_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendListActivity.class);
                startActivityForResult(intent, 104);
            }
        });

        btn_time = findViewById(R.id.btn_time);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, TimetableActivity.class);
                startActivityForResult(intent2, 103);
            }
        });

        btn_attendance = findViewById(R.id.btn_attendance);
        btn_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivityForResult(intent3, 102);
            }
        });

        btn_course = findViewById(R.id.btn_course);
        btn_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this, CourseListActivity.class);
                startActivityForResult(intent4, 101);
                // startActivity (intent4);
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(MainActivity.this, NoticeWriteActivity.class);
                startActivityForResult(intent5, 100);
            }
        });


    }

    // 두번 뒤로가기 누르면 종료되도록 함
    private long lastTimeBackPressd;

    @Override
    public void onBackPressed() {

        /*
        if(System.currentTimeMillis() - lastTimeBackPressd < 1500)
        {
            System.exit(0);
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
      //  lastTimeBackPressd = System.currentTimeMillis();
      */
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this,"'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else {
            AppFinish();
        }
    }

    public void AppFinish () {
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
