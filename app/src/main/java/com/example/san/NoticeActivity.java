package com.example.san;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.Toast;
import android.widget.ListView;

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


public class NoticeActivity extends AppCompatActivity{
    private ListView noticeListView;
    private NoticeAdapter adapter;
    private List<Notice> Notice;
    private String userID = MainActivity.userID;
    private String userState = MainActivity.userState;
    BackgroundTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticelist);

        noticeListView = (ListView)findViewById(R.id.noticeListView);
        Notice = new ArrayList<Notice>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.
        adapter = new NoticeAdapter(getApplicationContext(), Notice);
        noticeListView.setAdapter(adapter);

        task = new BackgroundTask();
        task.execute();

    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/NoticeList.php?userID=" + URLEncoder.encode(userID, "UTF-8");

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
                Notice.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                String noticeName;
                String noticeContent;
                String noticeDate;
                String userName;
                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);

                    noticeName = object.getString("noticeName");
                    noticeContent = object.getString("noticeContent");
                    noticeDate = object.getString("noticeDate");
                    userName = object.getString("userName");
                    Notice noticeList = new Notice(noticeName, noticeContent, noticeDate, userName);
                    Notice.add(noticeList);
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
                    dialog = builder.setMessage("공지사항이 없습니다")
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
