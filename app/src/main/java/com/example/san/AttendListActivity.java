package com.example.san;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;

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
    private String userState = MainActivity.userState;
    BackgroundTask task;
    String courseTime;

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
           // Toast.makeText(this, "정보를 불러오는데 실패하였습니다", Toast.LENGTH_LONG).show();
            return;
        }

        if (data == null) {
            return;
        }


        switch (requestCode) {
            case 105:
            case 106:
                break;

            default:
                break;
        }
    }

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

        attend_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userState.equals("교수")){
                    Intent intent1 = new Intent(AttendListActivity.this, AttendanceActivity.class);
                    intent1.putExtra("course_id", AttendList.get(position).courseID);
                    startActivityForResult(intent1, 105);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), AttendActivity.class);
                    intent.putExtra("course_id", AttendList.get(position).courseID);
                    intent.putExtra("title", AttendList.get(position).courseTitle);
                    intent.putExtra("room", AttendList.get(position).courseRoom);
                    intent.putExtra("time", AttendList.get(position).courseTime);
                    startActivityForResult(intent, 106);
                }
            }
        });

        task = new BackgroundTask();
        task.execute();

        System.out.println(courseTime);


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
            super.onPostExecute(result);
            if(result != null){
                try {
                    AttendList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;

                    String courseID;
                    String courseTitle;
                    String courseRoom;
                    while (count < jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);

                        courseID = object.getString("courseID");
                        courseTitle = object.getString("courseTitle");
                        courseTime = object.getString("courseTime");
                        courseRoom = object.getString("courseRoom");
                        AttendList attendList = new AttendList(courseID, courseTitle, courseRoom, courseTime);
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
}