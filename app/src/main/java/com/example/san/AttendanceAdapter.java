package com.example.san;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class AttendanceAdapter extends BaseAdapter {

    private List<Attendance> Attendance;
    private Context context;
    private Activity parent;
    private String userID = MainActivity.userID;

    public AttendanceAdapter(Context context, List<Attendance> Attendance, AttendanceActivity parent){
        this.context = context;
        this.Attendance = Attendance;
        this.parent = parent;
        new BackgroundTask().execute();
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return Attendance.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return Attendance.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.attendance, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌
        // TextView userID = (TextView)v.findViewById(R.id.userID);
        TextView courseTitle = (TextView)v.findViewById(R.id.courseTitle);
        TextView attdState = (TextView)v.findViewById(R.id.attdState);


        //  userID.setText(userList.get(i).getUserID());
        courseTitle.setText(Attendance.get(i).getCourseTitle());
        attdState.setText(Attendance.get(i).getAttdState());

        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(Attendance.get(i).getCourseTitle());
        v.setTag(Attendance.get(i).getAttdState());

        //만든뷰를 반환함
        return v;
    }


    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/AttdState.php?attdState=" + URLEncoder.encode(userID, "UTF-8");

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
                String attdState;
              //  int userID;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                //    userID = object.getInt("userID");
                    courseTitle = object.getString("courseTitle");
                    attdState = object.getString("attdState");
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
