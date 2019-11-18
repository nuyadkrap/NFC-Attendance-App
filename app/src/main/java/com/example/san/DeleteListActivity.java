package com.example.san;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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


public class DeleteListActivity extends AppCompatActivity {
    //test

    private ListView delete_ListView;
    private DeleteListAdapter adapter;
    private List<DeleteList> DeleteList;
    private String userID = MainActivity.userID;

    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletelist);


        Intent intent = getIntent();

        delete_ListView = (ListView) findViewById(R.id.delete_ListView);
        DeleteList = new ArrayList<DeleteList>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.


        task = new BackgroundTask();
        task.execute();


    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/DeleteList.php?userID=" + URLEncoder.encode(userID, "UTF-8");

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
                DeleteList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                String courseID;
                String courseTitle;
                String courseTime;
                String courseRoom;
                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);

                    courseID = object.getString("courseID");
                    courseTitle = object.getString("courseTitle");
                    courseRoom = object.getString("courseRoom");
                    courseTime = object.getString("courseTime");
                    DeleteList deleteList = new DeleteList(courseID, courseTitle, courseRoom, courseTime);
                    DeleteList.add(deleteList);
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeleteListActivity.this);
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