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

import go.demo.Demo;
import go.demo.GoData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
TODO:
background services  ()
push notification  - google service from web app
 */

class UserData {
    String mUuid;
    String mName;

    public UserData(String uuid, String name) {
        mUuid = uuid;
        mName = name;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        if (mName != null) {
            return mName;
        } else {
            return "(unknown)";
        }
    }

    public String getUuid() {
        return mUuid;
    }
}

public class MainActivity extends AppCompatActivity {

    final private boolean USE_BLUETOOTH = false;

    ListView bluelist;
    ListAdapter adapter;
    List mData;
    Button mRegButton;
    Button mButton;
    Map<String, String> mUserMap;

    protected boolean isInList(String uuid) {
        for (Object item: mData) {
            UserData user = (UserData)item;
            if (user.getUuid() == uuid) {
                return true;
            }
        }
        return false;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addUser(device.getAddress(), device.getName());
            }
        }
    };

    private void addUser(String addr, String name) {
        UserData user = new UserData(addr, name);
        if (! isInList(user.getUuid())) {
            mData.add(user);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluelist = (ListView)findViewById(R.id.bluelist);

        mData = new ArrayList<UserData>();
        mData.add(new UserData("123456789", null));
        mData.add(new UserData("deadbeef", "someone else"));
        mData.add(new UserData("foobarbaz", null));

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
        if (USE_BLUETOOTH) {
            BluetoothAdapter adapt = BluetoothAdapter.getDefaultAdapter();
            return adapt.getAddress();
        } else {
            return "123456789";
        }
    }

    public void discover() {
        getUserList();

        if (USE_BLUETOOTH) {
            BluetoothAdapter adapt = BluetoothAdapter.getDefaultAdapter();
            boolean worked = adapt.startDiscovery();
            if (! worked) {
                Toast.makeText(this, "Bluetooth failed", Toast.LENGTH_LONG).show();
            }
        } else {
            GoData data = Demo.demoUser();
            addUser(data.getUuid(), data.getName());
        }
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
            Toast.makeText(this, "Got data", Toast.LENGTH_SHORT).show();
            // refresh mData with mappings....
            for (Object item : mData) {
                UserData user = (UserData)item;
                String uuid = user.getUuid();
                if (users.has(uuid)) {
                    String name = users.getString(uuid);
                    user.setName(name);
                }
            }
            adapter.notifyDataSetChanged();
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
