package com.example.san;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class AttendanceActivity extends AppCompatActivity {

    Spinner spinner;
    private ArrayAdapter attendAdapter;
    private Spinner attendSpinner;


    private String courseTitle = "";
    private String attdState = "";
    private String userID = MainActivity.userID;

    private ListView attendListView;
    private AttendanceAdapter adapter;
    private List<Attendance> Attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);


        attendSpinner = (Spinner) findViewById(R.id.attendSpinner);

        attendAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.attend));
        attendSpinner.setAdapter(attendAdapter);


        attendListView = (ListView) findViewById(R.id.attendListView);
        Attendance = new ArrayList<Attendance>();
        adapter = new AttendanceAdapter(getApplicationContext(), Attendance, this);
        attendListView.setAdapter(adapter);

        Button searchButton = (Button) findViewById(R.id.searchButton2);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/Attendance.php?attdState=" + URLEncoder.encode(attendSpinner.getSelectedItem().toString(), "UTF-8") +
                "&userID=" + URLEncoder.encode(userID, "UTF-8");
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
                while ((temp = bufferedReader.readLine()) != null) {
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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                Attendance.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
               // String userID;
                String courseTitle;
                String attdState;

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                  //  userID = object.getString("userID");
                    courseTitle = object.getString("courseTitle");
                    attdState = object.getString("attdState");
                    Attendance attd = new Attendance(courseTitle, attdState);
                    Attendance.add(attd);
                    count++;
                }
                if (count == 0) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceActivity.this);
                    dialog = builder.setMessage("조회된 강의가 없습니다.\n출석을 진행하세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

