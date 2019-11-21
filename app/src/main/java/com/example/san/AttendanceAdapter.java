package com.example.san;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends BaseAdapter {

    private List<Attendance> Attendance;
    private Context context;
    private Activity parent;
    private String userID = MainActivity.userID;
    private String userState = MainActivity.userState;

    public AttendanceAdapter(Context context, List<Attendance> Attendance, AttendanceActivity parent){
        this.context = context;
        this.Attendance = Attendance;
        this.parent = parent;
        // new BackgroundTask().execute();
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

        if(userState.equals("교수")){
            View v = View.inflate(context, R.layout.checkattd, null);

            TextView userID = (TextView)v.findViewById(R.id.userID);
            TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
            TextView attdState = (TextView) v.findViewById(R.id.attdState);
            TextView userName = v.findViewById(R.id.userName);


            userID.setText(Attendance.get(i).getUserID());
            courseTitle.setText(Attendance.get(i).getCourseTitle());
            attdState.setText(Attendance.get(i).getAttdState());
            userName.setText(Attendance.get(i).getUserName());

            v.setTag(Attendance.get(i).getUserID());
            v.setTag(Attendance.get(i).getCourseTitle());
            v.setTag(Attendance.get(i).getAttdState());
            v.setTag(Attendance.get(i).getUserName());

            //만든뷰를 반환함
            return v;
        }
        else {
            View v = View.inflate(context, R.layout.attendance, null);

            //뷰에 다음 컴포넌트들을 연결시켜줌
            // TextView userID = (TextView)v.findViewById(R.id.userID);
            TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
            TextView attdState = (TextView) v.findViewById(R.id.attdState);


            //  userID.setText(userList.get(i).getUserID());
            courseTitle.setText(Attendance.get(i).getCourseTitle());
            attdState.setText(Attendance.get(i).getAttdState());

            //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
            v.setTag(Attendance.get(i).getCourseTitle());
            v.setTag(Attendance.get(i).getAttdState());

            //만든뷰를 반환함
            return v;
        }
    }
}
