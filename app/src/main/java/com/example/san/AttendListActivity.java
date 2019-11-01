package com.example.san;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class AttendListActivity extends AppCompatActivity {
    //test

    private ListView attend_ListView;
    private AttendListAdapter adapter;
    private List<AttendList> AttendList;
    private String userID = MainActivity.userID;
    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendlist);


        Intent intent = getIntent();

        attend_ListView = (ListView) findViewById(R.id.attend_ListView);
        AttendList = new ArrayList<AttendList>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.
        adapter = new AttendListAdapter(getApplicationContext(), AttendList);
        attend_ListView.setAdapter(adapter);

        task = new BackgroundTask();
        task.execute();

    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/AttendList.php?userID=" + URLEncoder.encode(userID, "UTF-8");

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
                AttendList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                String courseTitle;
                String courseTime;
                String courseRoom;
                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);

                    courseTitle = object.getString("courseTitle");
                    courseTime = object.getString("courseTime");
                    courseRoom = object.getString("courseRoom");
                    AttendList attendList = new AttendList(courseTitle, courseTime, courseRoom);
                    AttendList.add(attendList);
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendListActivity.this);
                    dialog = builder.setMessage("조회된 강의가 없습니다.\n강의를 추가하세요.")
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