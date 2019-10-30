package com.example.san;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    private AutoResizeTextView monday[] = new AutoResizeTextView[13];
    private AutoResizeTextView tuesday[] = new AutoResizeTextView[13];
    private AutoResizeTextView wednesday[] = new AutoResizeTextView[13];
    private AutoResizeTextView thursday[] = new AutoResizeTextView[13];
    private AutoResizeTextView friday[] = new AutoResizeTextView[13];
    private Schedule schedule = new Schedule();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        Integer[] mon_cell = {
                R.id.monday1, R.id.monday2, R.id.monday3, R.id.monday4, R.id.monday5,
                R.id.monday6, R.id.monday7, R.id.monday8, R.id.monday9, R.id.monday10,
                R.id.monday11, R.id.monday12, R.id.monday13
        };
        Integer[] tue_cell = {
                R.id.tuesday1, R.id.tuesday2, R.id.tuesday3, R.id.tuesday4, R.id.tuesday5,
                R.id.tuesday6, R.id.tuesday7, R.id.tuesday8, R.id.tuesday9, R.id.tuesday10,
                R.id.tuesday11, R.id.tuesday12, R.id.tuesday13
        };
        Integer[] wed_cell = {
                R.id.wednesday1, R.id.wednesday2, R.id.wednesday3, R.id.wednesday4, R.id.wednesday5,
                R.id.wednesday6, R.id.wednesday7, R.id.wednesday8, R.id.wednesday9, R.id.wednesday10,
                R.id.wednesday11, R.id.wednesday12, R.id.wednesday13
        };
        Integer[] thu_cell = {
                R.id.thursday1, R.id.thursday2, R.id.thursday3, R.id.thursday4, R.id.thursday5,
                R.id.thursday6, R.id.thursday7, R.id.thursday8, R.id.thursday9, R.id.thursday10,
                R.id.thursday11, R.id.thursday12, R.id.thursday13
        };
        Integer[] fri_cell = {
                R.id.friday1, R.id.friday2, R.id.friday3, R.id.friday4, R.id.friday5,
                R.id.friday6, R.id.friday7, R.id.friday8, R.id.friday9, R.id.friday10,
                R.id.friday11, R.id.friday12, R.id.friday13
        };

        for(int i=0; i<13; i++)
        {
            monday[i] = (AutoResizeTextView) findViewById(mon_cell[i]);
            tuesday[i] = (AutoResizeTextView) findViewById(tue_cell[i]);
            wednesday[i] = (AutoResizeTextView) findViewById(wed_cell[i]);
            thursday[i] = (AutoResizeTextView) findViewById(thu_cell[i]);
            friday[i] = (AutoResizeTextView) findViewById(fri_cell[i]);
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
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String courseProfessor;
                    String courseTime;
                    String courseTitle;
                    int courseID;
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

    }
}