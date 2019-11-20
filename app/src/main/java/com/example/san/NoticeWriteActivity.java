package com.example.san;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoticeWriteActivity extends AppCompatActivity {

    private EditText noticeName, noticeContent;
    private Button notice_write;
    private AlertDialog dialog;
    private String userName;
    private Spinner spinner;
    private ArrayAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);

        spinner = findViewById(R.id.notice_course);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        System.out.println("테스트12345");
        System.out.println(list);

        noticeName = findViewById(R.id.noticeName); //공지 제목
        noticeContent = findViewById(R.id.noticeContent); //공지 내용


        notice_write = findViewById(R.id.notice_write);

        notice_write.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = noticeName.getText().toString();
                String content = noticeContent.getText().toString();

                Intent intent = getIntent();
                String userName = getIntent().getStringExtra("userName");
                if (name.equals("") || content.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoticeWriteActivity.this);
                    dialog = builder.setMessage("빈칸없이 입력해 주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "성공하였습니다.", Toast.LENGTH_SHORT).show();
                                // Intent intent = new Intent(NoticeWriteActivity.this, MainActivity.class); //액티비티를 새로 시작하지 않음
                                Intent intent = new Intent();
                                intent.putExtra("noticeName", noticeName.getText().toString());
                                intent.putExtra("noticeContent", noticeContent.getText().toString());
                                setResult(1, intent);
                                finish();
                                //        startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            setResult(-1);
                            finish();
                            e.printStackTrace();

                        }
                    }
                };
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
                Date Date = new Date();
                String date = dateFormat.format(Date);

                NoticeWriteRequest noticeWriteRequest = new NoticeWriteRequest(name, content, date, userName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(NoticeWriteActivity.this);
                queue.add(noticeWriteRequest);

            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/SpinnerList.php?userName=" + userName;
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

                String courseTitle;

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);

                    courseTitle = object.getString("courseTitle");
                    list.add(courseTitle);
                    System.out.println("xptmxm");
                    System.out.println("courseTitle");
                    count++;
                }

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
}