package com.example.san;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.util.Log;
import java.util.Arrays;

public class AttendActivity extends AppCompatActivity {

    private TextView tv_id, tv_name;
    public static String userID; // 수정 예정

    TextView before_tag;
    TextView info;
    NfcAdapter mNfcAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mIntentFilters;
    String[][] mNFCTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        before_tag = (TextView) findViewById(R.id.before_tag);
        info = (TextView) findViewById(R.id.info);
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

        String s = action + "\n\n" + tag.toString();

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
                                s += ("\n\n강의실:\n\"" + new String(payload, langCodeLen+1, payload.length - langCodeLen-1, textEncoding) + "\"");
                            }
                            else {
                                s += ("\n\n책상번호:\n\"" + new String(payload, langCodeLen+1, payload.length - langCodeLen-1, textEncoding) + "\"");
                            }
                        }
                    }
                }
            }
            catch (Exception e){
                Log.e("tagdispatch", e.toString());
            }
        }
        before_tag.setText("태그완료!");
        info.setText(s);

        /* 수정 예정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        userID = getIntent().getStringExtra("userID");
        tv_id = findViewById(R.id.tv_id);
        tv_name = findViewById(R.id.tv_name);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");

        tv_id.setText(userID);
        tv_name.setText(userName);
        */
    }
}
