package com.example.san;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class CourseListActivity extends AppCompatActivity {

    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courselist);

        final String[] data = getResources().getStringArray(R.array.year);
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,data);
        Spinner spinner = (Spinner)findViewById(R.id.yearSpinner);
        spinner.setAdapter(adapter);

        final String[] data2 = getResources().getStringArray(R.array.major);
        ArrayAdapter<String> adapter2 =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,data2);
        Spinner spinner2 = (Spinner)findViewById(R.id.majorSpinner);
        spinner2.setAdapter(adapter2);

        final String[] data3 = getResources().getStringArray(R.array.term);
        ArrayAdapter<String> adapter3 =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,data3);
        Spinner spinner3 = (Spinner)findViewById(R.id.termSpinner);
        spinner3.setAdapter(adapter3);

        final String[] data4 = getResources().getStringArray(R.array.area);
        ArrayAdapter<String> adapter4 =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,data4);
        Spinner spinner4 = (Spinner)findViewById(R.id.areaSpinner);
        spinner4.setAdapter(adapter4);


    }
}
