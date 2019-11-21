package com.example.san;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {

    final static private String URL = "http://san19.dothome.co.kr/CourseAdd.php";
    private Map<String, String> map;

    public AddRequest(String userID, String courseID, String userName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("courseID", courseID);
        map.put("userName", userName);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError { return map;}
}
