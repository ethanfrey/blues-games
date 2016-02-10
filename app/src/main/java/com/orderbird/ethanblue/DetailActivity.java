package com.orderbird.ethanblue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTextView = (TextView)findViewById(R.id.detail_name);
        String text = getIntent().getExtras().getString("name");
        mTextView.setText(text);
//        Toast.makeText(this, "Create Detail Activity", Toast.LENGTH_SHORT).show();
    }
}
