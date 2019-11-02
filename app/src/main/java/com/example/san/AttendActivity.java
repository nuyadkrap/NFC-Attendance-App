package com.example.san;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Arrays;

public class AttendActivity extends AppCompatActivity {

    TextView before_tag;
    NfcAdapter mNfcAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mIntentFilters;
    String[][] mNFCTechLists;
    TextView courseRoom;
    TextView tableNum;
    TextView startTime;
    String courseRoom2;
    String tableNum2;
    String userID = MainActivity.userID;
    String course_id;
    String title;
    String time;
    String room;
    String attdState;
    int attd_exist;
    String table_Number;
    BackgroundTask task;
    String[] timeTable = {
            "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00"
    };
    String[] timeArr;

    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    // nowDate 변수에 값을 저장한다.
    String formatDate = sdfNow.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
        before_tag = (TextView) findViewById(R.id.before_tag);
        startTime = (TextView) findViewById(R.id.startTime);
        courseRoom = (TextView) findViewById(R.id.courseRoom);
        tableNum = (TextView) findViewById(R.id.tableNum);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNfcAdapter != null){
            before_tag.setText("태그하세요");
        }
        else{
            before_tag.setText("태그가 불가능합니다");
        }

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefIndent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try{
            ndefIndent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] {ndefIndent};
        }
        catch (Exception e){
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String [][] {
                new String[] { NfcF.class.getName() } };

        Intent intent = getIntent();
        course_id = intent.getStringExtra("course_id");
        title = intent.getStringExtra("title");
        room = intent.getStringExtra("room");
        time = intent.getStringExtra("time");

        task = new BackgroundTask();
        task.execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://san19.dothome.co.kr/CountList.php?userID=" + userID + "&courseID=" + course_id;
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
                int count=0;
                while (count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    attd_exist = object.getInt("row_num");
                    table_Number = object.getString("tableNum");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";
        String s1 = "";
        String s2 = "";

        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(data != null){
            try{
                for(int i=0;i<data.length;i++){
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for(int j=0;j<recs.length;j++){
                        if(recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)){
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8":"UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            if (j==0) {
                                courseRoom2 = new String(payload, langCodeLen+1, payload.length - langCodeLen-1, textEncoding);
                                if(room.equals(courseRoom2)){
                                    s1 = ("강의실:")+ courseRoom2;
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "강의실이 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            else {
                                tableNum2 = new String(payload, langCodeLen+1, payload.length - langCodeLen-1, textEncoding);
                                s2 = ("책상번호:") + tableNum2;
                            }
                        }
                    }
                }
            }
            catch (Exception e){
                Log.e("tagdispatch", e.toString());
            }
        }

        s = ("현재시간:")+ formatDate;
        before_tag.setText("태그완료!");
        startTime.setText(s);
        courseRoom.setText(s1);
        tableNum.setText(s2);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "출석 완료!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AttendActivity.this, AttendActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "출석 실패!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //startTime, endTime 구분해서 값 입력(DB에 해당 userID, courseID 없으면 start, 있으면 end
        if(attd_exist == 0 ){
            AttendRequest AttendRequest = new AttendRequest(userID, course_id, courseRoom2,  tableNum2, title, formatDate  , "" , "" , "" , responseListener);
            RequestQueue queue = Volley.newRequestQueue(AttendActivity.this);
            queue.add(AttendRequest);
        }
        else{
            EndTimeRequest endtimeRequest = new EndTimeRequest(userID, course_id, formatDate, "", "", responseListener);
            RequestQueue queue = Volley.newRequestQueue(AttendActivity.this);
            queue.add(endtimeRequest);
        }

    }


}
