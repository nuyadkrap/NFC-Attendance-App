package com.example.san;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AttendRequest extends StringRequest {

    final static private String URL = "http://san19.dothome.co.kr/AttendanceList.php";
    private Map<String, String> map;

    public AttendRequest(String userID, String courseID, String courseRoom, String tableNum, String courseTitle, String startTime, String endTime, String attdState, String etc, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("courseID", courseID);
        map.put("courseRoom", courseRoom);
        map.put("tableNum", tableNum);
        map.put("courseTitle", courseTitle);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("attdState", attdState);
        map.put("etc", etc);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
//