package com.orderbird.ethanblue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
TODO:
http calls
background services
bluetooth queries
 */

public class MainActivity extends AppCompatActivity {

    ListView bluelist;
    ListAdapter adapter;
    List mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluelist = (ListView)findViewById(R.id.bluelist);
        mData = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            mData.add("Child " + String.valueOf(i));
        }
        adapter = new ListAdapter(this, mData);
        bluelist.setAdapter(adapter);
//        Toast.makeText(this, "Create Main Activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
