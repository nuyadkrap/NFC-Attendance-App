package com.example.san;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeWriteActivity extends AppCompatActivity {

    private EditText noticeName, noticeContent;
    private Button notice_write;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);

        noticeName = findViewById(R.id.noticeName);
        noticeContent = findViewById(R.id.noticeContent);



        notice_write = findViewById(R.id.notice_write);

        notice_write.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = noticeName.getText().toString();
                String content = noticeContent.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NoticeWriteActivity.this, NoticeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                };
                NoticeWriteRequest noticeWriteRequest = new NoticeWriteRequest(name, content,responseListener);
                RequestQueue queue = Volley.newRequestQueue(NoticeWriteActivity.this);
                queue.add(noticeWriteRequest);

            }
        });

    }
}

