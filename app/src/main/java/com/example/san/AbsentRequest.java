package com.example.san;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AbsentRequest extends StringRequest {

    final static private String URL = "http://san19.dothome.co.kr/AbsentUpdate.php";
    private Map<String, String> map;

    public AbsentRequest(String userID, String courseID, String courseRoom, String courseTitle, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("courseID", courseID);
        map.put("courseRoom", courseRoom);
        map.put("courseTitle", courseTitle);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
