package com.example.san;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TimetableActivity extends AppCompatActivity {

    private TextView monday[] = new TextView[24];
    private TextView tuesday[] = new TextView[24];
    private TextView wednesday[] = new TextView[24];
    private TextView thursday[] = new TextView[24];
    private TextView friday[] = new TextView[24];
    private Schedule schedule = new Schedule();

    public static Activity AActivity;
    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        AActivity = TimetableActivity.this;
        Integer[] mon_cell = {
                R.id.monday0,
                R.id.monday1, R.id.monday2, R.id.monday3, R.id.monday4, R.id.monday5,
                R.id.monday6, R.id.monday7, R.id.monday8, R.id.monday9, R.id.monday10,
                R.id.monday11, R.id.monday12, R.id.monday13, R.id.monday14, R.id.monday15,
                R.id.monday16, R.id.monday17, R.id.monday18, R.id.monday19, R.id.monday20,
                R.id.monday21, R.id.monday22, R.id.monday23
        };
        Integer[] tue_cell = {
                R.id.tuesday0,
                R.id.tuesday1, R.id.tuesday2, R.id.tuesday3, R.id.tuesday4, R.id.tuesday5,
                R.id.tuesday6, R.id.tuesday7, R.id.tuesday8, R.id.tuesday9, R.id.tuesday10,
                R.id.tuesday11, R.id.tuesday12, R.id.tuesday13, R.id.tuesday14, R.id.tuesday15,
                R.id.tuesday16, R.id.tuesday17, R.id.tuesday18,R.id.tuesday19, R.id.tuesday20,
                R.id.tuesday21, R.id.tuesday22, R.id.tuesday23
        };
        Integer[] wed_cell = {
                R.id.wednesday0,
                R.id.wednesday1, R.id.wednesday2, R.id.wednesday3, R.id.wednesday4, R.id.wednesday5,
                R.id.wednesday6, R.id.wednesday7, R.id.wednesday8, R.id.wednesday9, R.id.wednesday10,
                R.id.wednesday11, R.id.wednesday12, R.id.wednesday13, R.id.wednesday14, R.id.wednesday15,
                R.id.wednesday16, R.id.wednesday17, R.id.wednesday18, R.id.wednesday19, R.id.wednesday20,
                R.id.wednesday21, R.id.wednesday22, R.id.wednesday23
        };
        Integer[] thu_cell = {
                R.id.thursday0,
                R.id.thursday1, R.id.thursday2, R.id.thursday3, R.id.thursday4, R.id.thursday5,
                R.id.thursday6, R.id.thursday7, R.id.thursday8, R.id.thursday9, R.id.thursday10,
                R.id.thursday11, R.id.thursday12, R.id.thursday13, R.id.thursday14,  R.id.thursday15,
                R.id.thursday16,  R.id.thursday17,  R.id.thursday18,  R.id.thursday19,  R.id.thursday20,
                R.id.thursday21,  R.id.thursday22,  R.id.thursday23
        };
        Integer[] fri_cell = {
                R.id.friday0,
                R.id.friday1, R.id.friday2, R.id.friday3, R.id.friday4, R.id.friday5,
                R.id.friday6, R.id.friday7, R.id.friday8, R.id.friday9, R.id.friday10,
                R.id.friday11, R.id.friday12, R.id.friday13, R.id.friday14, R.id.friday15,
                R.id.friday16, R.id.friday17, R.id.friday18, R.id.friday19, R.id.friday20,
                R.id.friday21, R.id.friday22, R.id.friday23
        };

        for(int i=0; i<24; i++)
        {
            monday[i] = (TextView) findViewById(mon_cell[i]);
            tuesday[i] = (TextView) findViewById(tue_cell[i]);
            wednesday[i] =(TextView) findViewById(wed_cell[i]);
            thursday[i] = (TextView) findViewById(thu_cell[i]);
            friday[i] = (TextView) findViewById(fri_cell[i]);
        }


        class BackgroundTask extends AsyncTask<Void, Void, String>
        {
            String target;

            @Override
            protected  void onPreExecute() {
                try {
                    target = "http://san19.dothome.co.kr/ScheduleList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8");

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
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //getInputStream 으로 받은 데이터중, 문자(char)만 필터링하는 과정.
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
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String courseProfessor;
                    String courseTime;
                    String courseTitle;
                    int courseID;

                    System.out.println(jsonArray.length());
                    while(count < jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);
                        courseID = object.getInt("courseID");
                        courseProfessor = object.getString("courseProfessor");
                        courseTime = object.getString("courseTime");
                        courseTitle = object.getString("courseTitle");
                        schedule.addSchedule(courseTime, courseTitle, courseProfessor);
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                schedule.setting(monday, tuesday, wednesday, thursday, friday, getApplicationContext());
            }
        }

        new BackgroundTask().execute();

        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(TimetableActivity.this, DeleteListActivity.class);
                startActivityForResult(intent6, 107);

            }
        });
    }
}