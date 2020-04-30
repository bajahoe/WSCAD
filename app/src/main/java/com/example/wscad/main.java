package com.example.wscad;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class main extends AppCompatActivity {
    // PopupMenu
    private Button mButton_pop;

    // test
    String read_message = "";
    // Debugging
    private static final String TAG = "Main";
    private static final boolean D = true;
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes, 액티비티 전환 시 처리 결과를 받아오기 위해 사용하는 코드들
    private static final int REQUEST_CONNECT_DEVICE = 1;    // 블루투스 기기연결
    private static final int REQUEST_ENABLE_BT = 2; // 블루투스 켜기
    private static final int RESULT_GPS_REQUEST = 900; // GPS 수신 결과

    // Layout Views
    private TextView mTitle;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    private TextView heartbit;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mChatService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        heartbit=findViewById(R.id.textView);

    }

    public void onClick(View button) {
        PopupMenu popup = new PopupMenu(this, button);
        popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bluetooth: // 기기 검색
                                // Launch the DeviceListActivity to see devices and do scan
                                Intent serverIntent = new Intent(main.this, DeviceListActivity.class);
                                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);   // 기기 연결 요청
                                return true;
                        }
                        return false;
                    }
                });

        popup.show();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            mTitle.append(mConnectedDeviceName);
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //int index = readMessage.indexOf("x");
                    /*if(index>-1) {
                        int length = readMessage.length();
                        if(index == length) {
                            read_message += readMessage.substring(0, length - 1);
                            mConversationArrayAdapter.add(read_message);
                            read_message = "";
                            break;
                        }
                        else if(index == 0) {
                            mConversationArrayAdapter.add(read_message);
                            read_message = "";
                            if(length>1)
                                read_message += readMessage.substring(1,length);
                            break;
                        }
                        else {
                            read_message += readMessage.substring(0,index);
                            mConversationArrayAdapter.add(read_message);
                            read_message = readMessage.substring(index+1,length);
                            break;
                        }
                    }
                    read_message += readMessage;*/
                    // construct a string from the valid bytes in the buffer
                    heartbit.setText(readMessage);
                    heartbit.invalidate();
                    break;

                /**
                 * byte[] readBuf = (byte[])msg.obj;
                 *                     String readMessage = new String(readBuf, 0, msg.arg1);
                 *                     // construct a string from the valid bytes in the buffer
                 *                     mConversationArrayAdapter.add(readMessage); 초기코드
                 * */
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
