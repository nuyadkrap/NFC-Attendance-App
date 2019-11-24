package com.example.san;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv_id, tv_name;
    private ListView noticeListView;
    private NoticeAdapter adapter;
    private List<Notice> noticeList;

    public static String userID, userState, userName;
    public static ArrayList<String> scheduleTime = new ArrayList<String>();
    public static ArrayList<String> course_Title = new ArrayList<String>();

    Button btn_attend, btn_attendance, btn_time, btn_course, notice, logout;

    private static int ONE_MINUTE = 5626;

    String channelId = "channel";
    String channelName = "Channel Name";
    BackgroundTask task;
    ArrayList<String> final_schedule = new ArrayList<String>();

    String[] fiveMinute = {"0855", "0925", "0955", "1025", "1055", "1125", "1155", "1225",
            "1255", "1325", "1355", "1425", "1455", "1525", "1555", "1625", "1655", "1725",
            "1755", "1825", "1855", "1925", "1955", "2025"};

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
            case 104:
            case 102:
                userState = data.getStringExtra("userState");
                break;

            case 101:
                break;

            case 103:
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
/*
        noticeListView = (ListView)findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();
        adapter = new NoticeAdapter(getApplicationContext(),noticeList);
        noticeListView.setAdapter(adapter);
*/
        userID = getIntent().getStringExtra("userID");
        userState = getIntent().getStringExtra("userState");
        tv_id = findViewById(R.id.tv_id);
        tv_name = findViewById(R.id.tv_name);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");

        tv_id.setText(userID);
        tv_name.setText(userName);

        Intent intent9 = new Intent(MainActivity.this, NoticeFragment.class);
        intent9.putExtra("userID", userID);


        task = new BackgroundTask();
        task.execute();

        notice = findViewById(R.id.notice);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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
                if (userState.equals("교수")) {
                    Intent intent = new Intent(MainActivity.this, AttendListActivity.class);
                    startActivityForResult(intent, 104);
                }
                else {
                    Intent intent3 = new Intent(MainActivity.this, AttendanceActivity.class);
                    startActivityForResult(intent3, 102);
                }
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
                intent5.putExtra("userName", userName);
                intent5.putExtra("courseTitle", course_Title);
                startActivityForResult(intent5, 100);
            }
        });

        //Intent intent1 = new Intent(MainActivity.this, MyService.class);
        //startService(intent1);

   //     new AlarmHATT(getApplicationContext()).Alarm();

    }

    public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            System.out.println("77777777777");
            System.out.println(scheduleTime);
            String s;
            String time;

            for(int i=0;i<scheduleTime.size();i++) {
                s = scheduleTime.get(i);
                int count = 0;
                for(int j=0;j<s.length();j++){
                    if(s.charAt(j) == ':')
                        count++;
                }
                String day1="", start1="", start2="";

                day1 = s.split(":")[0];
                start1 = s.split(":")[1];
                final_schedule.add(day1);
                final_schedule.add(start1.substring(1,2));

                if(count != 1){
                    start2 = s.split(":")[2];
                    final_schedule.add(start1.substring(start1.length()-1, start1.length()));
                    final_schedule.add(start2.substring(1,2));
                }
            }

            System.out.println(final_schedule);   //[화,4,수,9,수,1]

            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BroadcastD.class);

            final int _id = (int) System.currentTimeMillis();

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            for(int i=0; i<final_schedule.size(); i+=2){
                time = fiveMinute[Integer.parseInt((final_schedule.get(i+1)))-1];
                switch (final_schedule.get(i)){
                    case "월":
                        calendar.set(Calendar.DAY_OF_WEEK, 2);
                        calendar.set(Calendar.HOUR, Integer.parseInt(time.substring(0,2)));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2,4)));
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case "화":
                        calendar.set(Calendar.DAY_OF_WEEK, 3);
                        calendar.set(Calendar.HOUR, Integer.parseInt(time.substring(0,2)));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2,4)));
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case "수":
                        calendar.set(Calendar.DAY_OF_WEEK, 4);
                        calendar.set(Calendar.HOUR, Integer.parseInt(time.substring(0,2)));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2,4)));
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case "목":
                        calendar.set(Calendar.DAY_OF_WEEK, 5);
                        calendar.set(Calendar.HOUR, Integer.parseInt(time.substring(0,2)));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2,4)));
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case "금":
                        calendar.set(Calendar.DAY_OF_WEEK, 6);
                        calendar.set(Calendar.HOUR, Integer.parseInt(time.substring(0,2)));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2,4)));
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                }
            }

            //calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 12, 18, 0);

            //알람 예약
            //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            Calendar now = Calendar.getInstance();
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);
            if(calendar.before(now)){
                calendar.add(Calendar.DATE, 7);
            }

            //알람 매주 스케줄따라 반복
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7*24*60^60*1000, sender);
            //알람 한번만
            //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        }
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/AlarmSchedule.php?userID=" + userID;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//getInputStream 으로 받은 데이터중, 문자(char)만 필터링하는 과정.
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) { super.onProgressUpdate(); }

        @Override
        public void onPostExecute(String result) {
            try {
                System.out.println("11111111");
                System.out.println(result);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                System.out.println(jsonObject);
                System.out.println(jsonArray);
                int count=0;
                String courseSchedule;
                String courseTitle;
                String courseID2;
                String courseDivide;

                while (count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    courseSchedule = object.getString("scheduleTime");
                    scheduleTime.add(courseSchedule);
                    courseTitle = object.getString("courseTitle");
                    courseID2 = object.getString("courseID");
                    courseDivide = object.getString("courseDivide");
                    course_Title.add(courseID2);
                    course_Title.add(courseTitle);
                    course_Title.add(courseDivide);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 두번 뒤로가기 누르면 종료되도록 함
    private long lastTimeBackPressd;

    long backKeyPressedTime;

    @Override
    public void onBackPressed() {

        /*
        if(System.currentTimeMillis() - lastTimeBackPressd < 1500)
        {
            System.exit(0);
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressd = System.currentTimeMillis();
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
