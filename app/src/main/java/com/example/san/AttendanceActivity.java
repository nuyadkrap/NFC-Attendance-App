package com.example.san;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AttendanceActivity extends AppCompatActivity {

    Spinner spinner;
    private ArrayAdapter attendAdapter;
    private Spinner attendSpinner;

    private String userID = MainActivity.userID;
    private String userState = MainActivity.userState;
    String course_title;

    private ListView attendListView;
    private AttendanceAdapter adapter;
    private List<Attendance> Attendance;
    private String courseID, course_room;
    Button selectAll, absent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("course_id");
        course_title = intent.getStringExtra("course_title");
        course_room = intent.getStringExtra("course_room");

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

        selectAll = findViewById(R.id.selectAll);
        absent = findViewById(R.id.absent);
        if (userState.equals("교수")) {
            selectAll.setVisibility(View.VISIBLE);
            absent.setVisibility(View.VISIBLE);
        }

        selectAll.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                int count = 0;
                count = adapter.getCount();

                for(int i=0;i<count;i++){
                    attendListView.setItemChecked(i, true);
                }
            }
        });

        absent.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                SparseBooleanArray checkedItems = attendListView.getCheckedItemPositions();
                int count = adapter.getCount();

                if(checkedItems.size() != 0){
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println(response);
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "결석처리 되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "결석 처리에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    };
                    for(int i=0; i<checkedItems.size(); i++){
                        AbsentRequest absentRequest = new AbsentRequest(Attendance.get(i).userID, courseID, course_room, course_title, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AttendanceActivity.this);
                        queue.add(absentRequest);
                    }
                }
                else{
                    Toast.makeText(AttendanceActivity.this, "결석처리 할 학생을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                attendListView.clearChoices();
                adapter.notifyDataSetChanged();
            }
        });

        System.out.println("**********2342342*********");
        System.out.println(courseID);

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                if (userState.equals("교수"))
                {
                    if(Objects.equals(attendSpinner.getSelectedItem().toString(), "미출결")){
                        target = "http://san19.dothome.co.kr/NotChecked.php?userID=" + userID + "&courseID=" + courseID;
                    }
                    else {
                        target = "http://san19.dothome.co.kr/Checkattd.php?attdState=" + URLEncoder.encode(attendSpinner.getSelectedItem().toString(), "UTF-8") +
                                "&courseID=" + courseID;
                    }
                }
               else {
                    target = "http://san19.dothome.co.kr/Attendance.php?attdState=" + URLEncoder.encode(attendSpinner.getSelectedItem().toString(), "UTF-8") +
                            "&userID=" + URLEncoder.encode(userID, "UTF-8");
                }
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
                String userName;
                String userID2;
                //String courseTitle;
                String attdState;

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);

                    //courseTitle = object.getString("courseTitle");
                    if(Objects.equals(attendSpinner.getSelectedItem().toString(), "미출결")){
                        attdState = "";
                    }
                    else{
                        attdState = object.getString("attdState");
                    }

                    if (userState.equals("교수"))
                    {
                        userID2 = object.getString("userID");
                        userName = object.getString("userName");
                        Attendance attd = new Attendance(course_title, attdState, userID2, userName);
                        Attendance.add(attd);
                        count++;
                    }

                    else {
                        Attendance attd = new Attendance(course_title, attdState);
                        Attendance.add(attd);
                        count++;
                    }
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

