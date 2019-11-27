package com.example.san;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    String loginID, loginPW, loginState, loginName;
    String userID, userName, userState;

    public static Activity BActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        BActivity = LoginActivity.this;

        Intent intent2 = new Intent(LoginActivity.this, NoticeFragment.class);
        intent2.putExtra("userID",userID);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginID = auto.getString("inputID", null);
        loginPW = auto.getString("inputPW", null);
        loginState = auto.getString("inputState", null);
        loginName = auto.getString("inputName", null);

        if(loginID != null && loginPW != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userID", loginID);
            intent.putExtra("userName", loginName);
            intent.putExtra("userState", loginState);
            startActivity(intent);
        }
        else if(loginID == null && loginPW == null){
            btn_login.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    final String userID_2 = et_id.getText().toString();
                    final String userPass_2 = et_pass.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println(response);
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    userID = jsonObject.getString("userID");
                                    userName = jsonObject.getString("userName");
                                    userState = jsonObject.getString("userState");
                                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("inputID", userID_2);
                                    autoLogin.putString("inputPW", userPass_2);
                                    autoLogin.putString("inputState", userState);
                                    autoLogin.putString("inputName", userName);
                                    autoLogin.commit();

                                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("userState", userState);

                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;

                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(userID_2, userPass_2, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);

                }

            });
        }

        btn_login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String userID_2 = et_id.getText().toString();
                final String userPass_2 = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                userID = jsonObject.getString("userID");
                                userName = jsonObject.getString("userName");
                                userState = jsonObject.getString("userState");
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputID", userID_2);
                                autoLogin.putString("inputPW", userPass_2);
                                autoLogin.putString("inputState", userState);
                                autoLogin.putString("inputName", userName);
                                autoLogin.commit();

                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userName", userName);
                                intent.putExtra("userState", userState);

                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID_2, userPass_2, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }

        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
