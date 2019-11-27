package com.example.san;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class  NoticeFragment extends Fragment {
    private ListView noticeListView;
    private NoticeAdapter adapter;
    private List<Notice> Notice = new ArrayList<>();
    private String userID;
    BackgroundTask task;


    public NoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Notice = new ArrayList<Notice>();


        Intent intent = getActivity().getIntent();
        try {
            userID = intent.getStringExtra("userID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("userID: userID: userID");
        System.out.println(userID);

        task = new BackgroundTask();
        task.execute();

        View v = inflater.inflate(R.layout.fragment_notice, container, false);
        noticeListView = (ListView) v.findViewById(R.id.noticeListView);
        //Notice = new ArrayList<Notice>();

        System.out.println("Notice 길이");
        System.out.println(Notice.size());
//        adapter = new NoticeAdapter(getContext().getApplicationContext(), Notice);
//        noticeListView.setAdapter(adapter);

        return v;
    }


    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target = "";

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/NoticeList.php?userID=" + userID;

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                System.out.println("제발");
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
                System.out.println("알아보기 쉽게");
                System.out.println(result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                System.out.println(jsonObject);

                String noticeName;
                String noticeContent;
                String noticeDate;
                String pfName;
                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);

                    noticeName = object.getString("noticeName");
                    noticeContent = object.getString("noticeContent");
                    noticeDate = object.getString("noticeDate");
                    pfName = object.getString("pfName");
                    System.out.println("공지사항");
                    System.out.println(noticeName);
                    System.out.println(noticeContent);
                    System.out.println(noticeDate);
                    System.out.println(pfName);
                    Notice noticeList = new Notice(noticeName, noticeContent, noticeDate, pfName);
                    Notice.add(noticeList);
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("공지사항이 없습니다")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
                System.out.println("rrrrrrrrr");
                System.out.println(Notice.size());
                adapter = new NoticeAdapter(getContext().getApplicationContext(), Notice);
                noticeListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

