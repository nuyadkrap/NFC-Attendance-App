package com.example.san;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
public class NoticeWriteRequest extends StringRequest {

    final static private String URL = "http://san19.dothome.co.kr/notice.php";
    private Map<String, String> map;

    public NoticeWriteRequest(String name, String content, String date, String userName, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("noticeName", name);
        map.put("noticeContent", content);
        map.put("noticeDate", date);
        map.put("userName", userName);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
