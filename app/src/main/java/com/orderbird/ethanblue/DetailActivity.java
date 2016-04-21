package com.orderbird.ethanblue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    TextView mNameView;
    TextView mUuidView;

    protected TextView setTextView(int id, String key) {
        TextView view = (TextView)findViewById(id);
        String text = getIntent().getExtras().getString(key);
        view.setText(text);
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mNameView = setTextView(R.id.detail_name, "name");
        mUuidView = setTextView(R.id.detail_uuid, "uuid");
    }
}
