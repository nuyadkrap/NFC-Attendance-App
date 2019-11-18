package com.example.san;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DeleteListAdapter extends BaseAdapter {

    private Context context;
    private List<DeleteList> DeleteList;
    private Activity parent;
    private String userID = MainActivity.userID;
    private List<String> courseIDLIst;

    public DeleteListAdapter(Context context, List<DeleteList> DeleteList, DeleteListActivity parent){
        this.context = context;
        this.DeleteList = DeleteList;
        this.parent = parent;
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return DeleteList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return DeleteList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)  {
        View v = View.inflate(context, R.layout.activity_delete, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌
        TextView courseTitle = (TextView)v.findViewById(R.id.courseTitle);
        TextView courseRoom = (TextView)v.findViewById(R.id.courseRoom);
        TextView courseTime = (TextView)v.findViewById(R.id.courseTime) ;


        //  userID.setText(userList.get(i).getUserID());
        courseTitle.setText(DeleteList.get(i).getCourseTitle());
        courseRoom.setText(DeleteList.get(i).getCourseRoom());
        courseTime.setText(DeleteList.get(i).getCourseTime());

        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(DeleteList.get(i).getCourseTitle());


        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println(response);
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                                    AlertDialog dialog = builder.setMessage("강의가 삭제되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    notifyDataSetChanged();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                                    AlertDialog dialog = builder.setMessage("강의 삭제가 실패하였습니다 .")
                                            .setPositiveButton("다시 시도", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                            notifyDataSetChanged();
                        }
                    };
                    DeleteRequest deleteRequest = new DeleteRequest(userID, DeleteList.get(i).getCourseID() + "", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(parent);
                    queue.add(deleteRequest);
                }


        });

        return v;


    }

}
//