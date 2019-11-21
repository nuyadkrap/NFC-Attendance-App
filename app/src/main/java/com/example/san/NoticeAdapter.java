package com.example.san;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class NoticeAdapter extends BaseAdapter {

    private Context context;
    private List<Notice> NoticeList;

    public NoticeAdapter(Context context, List<Notice> NoticeList){
        this.context = context;
        this.NoticeList = NoticeList;
    }
    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return NoticeList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return NoticeList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.notice, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌

        TextView noticeName = (TextView)v.findViewById(R.id.noticeName);
        TextView noticeContent = (TextView)v.findViewById(R.id.noticeContent);
        TextView noticeDate = (TextView)v.findViewById(R.id.noticeDate) ;
        TextView userName = (TextView)v.findViewById(R.id.userName) ;

        //  userID.setText(userList.get(i).getUserID());
        noticeName.setText(NoticeList.get(i).getNoticeName());
        noticeContent.setText(NoticeList.get(i).getNoticeContent());
        noticeDate.setText(NoticeList.get(i).getNoticeDate());
        userName.setText(NoticeList.get(i).getUserName());
        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        // v.setTag(NoticeList.get(i).getCourseTitle());

        //만든뷰를 반환함
        return v;
    }
}
