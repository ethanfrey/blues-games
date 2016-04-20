package com.orderbird.ethanblue;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
TODO:
structure to hold display objects with name and uuid to help mapping them
better display code (new list adaptor)
default data

background services  ()
push notification  - google service from web app
 */

public class MainActivity extends AppCompatActivity {

    ListView bluelist;
    ListAdapter adapter;
    List mData;
    Button mRegButton;
    Button mButton;
    Map<String, String> mUserMap;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String line = device.getName() + "\n" + device.getAddress();
                if (! mData.contains(line)) {
                    mData.add(line);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluelist = (ListView)findViewById(R.id.bluelist);
        mData = new ArrayList<String>();
        mData.add("<BLANK HEADER>");
        adapter = new ListAdapter(this, mData);
        bluelist.setAdapter(adapter);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        mRegButton = (Button)findViewById(R.id.register);
        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mButton = (Button)findViewById(R.id.discovery);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discover();
            }
        });
    }

    public String getAddress() {
//        BluetoothAdapter adapt = BluetoothAdapter.getDefaultAdapter();
//        String myUuid = adapt.getAddress();
        String myUuid = "123456789";
        return myUuid;
    }

    public void discover() {
        getUserList();
//        boolean worked = adapt.startDiscovery();
//        if (! worked) {
//            Toast.makeText(this, "Bluetooth failed", Toast.LENGTH_LONG).show();
//        }
    }

    public void register() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alert.setMessage("What is your name?");
        alert.setTitle("Register for the game");

        alert.setView(edittext);

        alert.setPositiveButton("Meet and Greet", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = edittext.getText().toString();
                String myUuid = getAddress();

                ServerTalk.register(MainActivity.this, name, myUuid);
            }
        });

        alert.setNegativeButton("Back to my bedroom...", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    public void getUserList() {
        ServerTalk.getAllUsers(MainActivity.this);
    }

    public void updateUserList(JSONObject users) {
        try {
            mUserMap = new HashMap<String, String>();
            Iterator keys = users.keys();
            while (keys.hasNext()) {
                String key = (String)keys.next();
                String val = users.getString(key);
                mUserMap.put(key, val);
            }
            Toast.makeText(this, "Got data", Toast.LENGTH_LONG).show();
            // TODO: refresh mData with mappings....
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
