package com.example.san;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    Spinner spinner;
    private ArrayAdapter yearAdapter;
    private Spinner yearSpinner;
    private ArrayAdapter termAdapter;
    private Spinner termSpinner;
    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;
    private ArrayAdapter majorAdapter;
    private Spinner majorSpinner;

    private String courseUniversity = "";
    private String courseYear = "";
    private String courseTerm = "";
    private String courseArea = "";
    private String courseMajor = "";

    private ListView courseListView;
    private CourseListAdapter adapter;
    private List<Course> courseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courselist);

        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        termSpinner = (Spinner) findViewById(R.id.termSpinner);
        areaSpinner = (Spinner) findViewById(R.id.areaSpinner);
        majorSpinner = (Spinner) findViewById(R.id.majorSpinner);

        yearAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.year));
        yearSpinner.setAdapter(yearAdapter);

        majorAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.major));
        majorSpinner.setAdapter(majorAdapter);

        termAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.term));
        termSpinner.setAdapter(termAdapter);

        areaAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.area));
        areaSpinner.setAdapter(areaAdapter);

        courseListView = (ListView) findViewById(R.id.courseListView);
        courseList = new ArrayList<Course>();
        adapter = new CourseListAdapter(getApplicationContext(), courseList, this);
        courseListView.setAdapter(adapter);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });

    }


        class BackgroundTask extends AsyncTask<Void, Void, String>
        {
            String target;

            @Override
            protected  void onPreExecute() {
                try {
                    target = "http://san19.dothome.co.kr/CourseList.php?courseYear=" + URLEncoder.encode(yearSpinner.getSelectedItem().toString().substring(0, 4), "UTF-8") +
                            "&courseArea=" + URLEncoder.encode(areaSpinner.getSelectedItem().toString(), "UTF-8") +
                            "&courseTerm=" + URLEncoder.encode(termSpinner.getSelectedItem().toString(), "UTF-8") +
                            "&courseMajor=" + URLEncoder.encode(majorSpinner.getSelectedItem().toString(), "UTF-8");
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
                    courseList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    int courseID;
                    String courseUniversity;
                    int courseYear;
                    String courseTerm;
                    String courseArea;
                    String courseMajor;
                    String courseGrade;
                    String courseTitle;
                    String courseCredit;
                    int courseDivide;
                    String courseProfessor;
                    String courseTime;
                    String courseRoom;
                    while (count < jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);
                        courseID = object.getInt("courseID");
                        courseUniversity = object.getString("courseUniversity");
                        courseYear = object.getInt("courseYear");
                        courseTerm = object.getString("courseTerm");
                        courseArea = object.getString("courseArea");
                        courseMajor = object.getString("courseMajor");
                        courseGrade = object.getString("courseGrade");
                        courseTitle = object.getString("courseTitle");
                        courseCredit = object.getString("courseCredit");
                        courseDivide = object.getInt("courseDivide");
                        courseProfessor = object.getString("courseProfessor");
                        courseTime = object.getString("courseTime");
                        courseRoom = object.getString("courseRoom");
                        Course course = new Course(courseID, courseUniversity, courseYear, courseTerm, courseArea, courseMajor, courseGrade, courseTitle, courseCredit, courseDivide, courseProfessor, courseTime, courseRoom);
                        courseList.add(course);
                        count++;
                    }
                    if(count == 0)
                    {
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseListActivity.this);
                        dialog = builder.setMessage("조회된 강의가 없습니다.\n날짜를 확인하세요.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


    }


}
