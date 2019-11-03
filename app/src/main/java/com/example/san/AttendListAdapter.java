package com.example.san;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.List;


public class AttendListAdapter extends BaseAdapter {

    private Context context;
    private List<AttendList> AttendList;

    public AttendListAdapter(Context context, List<AttendList> AttendList){
        this.context = context;
        this.AttendList = AttendList;
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return AttendList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return AttendList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.attendlist, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌
        // TextView userID = (TextView)v.findViewById(R.id.userID);
        // TextView courseID = (TextView)v.findViewById(R.id.courseID);
        TextView courseTitle = (TextView)v.findViewById(R.id.courseTitle);
        TextView courseRoom = (TextView)v.findViewById(R.id.courseRoom);
        TextView courseTime = (TextView)v.findViewById(R.id.courseTime) ;


        //  userID.setText(userList.get(i).getUserID());
        courseTitle.setText(AttendList.get(i).getCourseTitle());
        courseRoom.setText(AttendList.get(i).getCourseRoom());
        courseTime.setText(AttendList.get(i).getCourseTime());

        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(AttendList.get(i).getCourseTitle());

        //만든뷰를 반환함
        return v;
    }
}