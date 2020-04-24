package com.example.wscad;


/**
 * 블루투스 모듈
 * */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
<<<<<<< Updated upstream
=======
=======

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
>>>>>>> Stashed changes

>>>>>>> f8c48378687c403c23fea64a612ad0e2c0749b3c
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/***
 *
 */

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    // PopupMenu
    private Button mButton_pop;

    // test
    byte[] readBuf; // 임시 저장

    // Debugging
    private static final String TAG = "Main";
<<<<<<< HEAD
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

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private TextView mTitle;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

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
    /**
     //DB 테스트
     Button btn_Update;
     Button btn_Insert;
     Button btn_Select;
     EditText edit_ID;
     EditText edit_Name;
     EditText edit_Age;
     TextView text_ID;
     TextView text_Name;
     TextView text_Age;
     TextView text_Gender;
     CheckBox check_Man;
     CheckBox check_Woman;
     CheckBox check_ID;
     CheckBox check_Name;
     CheckBox check_Age;

     long nowIndex;
     String ID;
     String name;
     long age;
     String gender = "";
     String sort = "userid";
     // DB테스트

     ArrayAdapter<String> arrayAdapter;

     static ArrayList<String> arrayIndex =  new ArrayList<String>();
     static ArrayList<String> arrayData = new ArrayList<String>();
     private DbOpenHelper mDbOpenHelper;
     // DB테스트 변수

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
=======
    // 블루투스 시작
    // Intent 요청 코드
    private  static final int REQUEST_CONNECT_DEVICE=1;
    private  static final int REQUEST_ENABLE_BT=2;
    // 테스트용 버튼 변수
    private Button btn_Connect;
    private Button btn_Send;


    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치

    private TextView textViewReceive; // 수신 된 데이터를 표시하기 위한 텍스트 뷰
    private EditText editTextSend; // 송신 할 데이터를 작성하기 위한 에딧 텍스트
    private Button buttonSend; // 송신하기 위한 버튼

    private BluetoothService btService = null;

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
        }
    };//
    // 블루투스 끝
    //DB 테스트
    Button btn_Update;
    Button btn_Insert;
    Button btn_Select;
    EditText edit_ID;
    EditText edit_Name;
    EditText edit_Age;
    TextView text_ID;
    TextView text_Name;
    TextView text_Age;
    TextView text_Gender;
    CheckBox check_Man;
    CheckBox check_Woman;
    CheckBox check_ID;
    CheckBox check_Name;
    CheckBox check_Age;
=======
<<<<<<< Updated upstream
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
>>>>>>> ff40487c20db7ebe170804e33a2a811eaf8d7d87
=======

>>>>>>> Stashed changes

    long nowIndex;
    String ID;
    String name;
    long age;
    String gender = "";
    String sort = "userid";
    // DB테스트
    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;
    // DB테스트 변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
>>>>>>> Stashed changes
=======

>>>>>>> Stashed changes
        /** Main Layout **/
        btn_Connect = (Button) findViewById(R.id.btn_connect);
        textViewReceive = (TextView)findViewById(R.id.textView_receive);
>>>>>>> f8c48378687c403c23fea64a612ad0e2c0749b3c

<<<<<<< Updated upstream
<<<<<<< Updated upstream

        // 블루투스 클래스 생성
        if(btService == null){
            btService = new BluetoothService(this, mHandler);
        }


=======
=======
        if (D) Log.e(TAG, "+++ ON CREATE +++");
>>>>>>> Stashed changes

        // Set up the window layout
        final Window window = getWindow();
        boolean useTitleFeature = false;
        if (window.getContainer() == null) {
            useTitleFeature = window
                    .requestFeature(Window.FEATURE_CUSTOM_TITLE);
        }
<<<<<<< Updated upstream


>>>>>>> Stashed changes
=======
        setContentView(R.layout.activity_main); // 중복이니 코드수정
        if (useTitleFeature) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.custom_title);
            // Set up the custom title

<<<<<<< HEAD
            mTitle = (TextView) findViewById(R.id.title_left_text);
            mTitle.setText(R.string.app_name);
            mTitle = (TextView) findViewById(R.id.title_right_text);

=======
>>>>>>> Stashed changes
        // DB테스트 버튼
        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        btn_Update = (Button) findViewById(R.id.btn_update);
        btn_Update.setOnClickListener(this);
        btn_Select = (Button) findViewById(R.id.btn_select);
        btn_Select.setOnClickListener(this);
        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        edit_Age = (EditText) findViewById(R.id.edit_age);
        text_ID = (TextView) findViewById(R.id.text_id);
        text_Name = (TextView) findViewById(R.id.text_name);
        text_Age = (TextView) findViewById(R.id.text_age);
        text_Gender= (TextView) findViewById(R.id.text_gender);
        check_Man = (CheckBox) findViewById(R.id.check_man);
        check_Man.setOnClickListener(this);
        check_Woman = (CheckBox) findViewById(R.id.check_woman);
        check_Woman.setOnClickListener(this);
        check_ID = (CheckBox) findViewById(R.id.check_userid);
        check_ID.setOnClickListener(this);
        check_Name = (CheckBox) findViewById(R.id.check_name);
        check_Name.setOnClickListener(this);
        check_Age = (CheckBox) findViewById(R.id.check_age);
        check_Age.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        check_ID.setChecked(true);
        showDatabase(sort);

        btn_Insert.setEnabled(true);
        btn_Update.setEnabled(false);
        // DB
    }

    //DB
    public void setInsertMode(){
        edit_ID.setText("");
        edit_Name.setText("");
        edit_Age.setText("");
        check_Man.setChecked(false);
        check_Woman.setChecked(false);
        btn_Insert.setEnabled(true);
        btn_Update.setEnabled(false);
    }

    //클릭리스너 in DB
    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            Log.e("On Click", "nowIndex = " + nowIndex);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData);
            edit_ID.setText(tempData[0].trim());
            edit_Name.setText(tempData[1].trim());
            edit_Age.setText(tempData[2].trim());
            if(tempData[3].trim().equals("Man")){
                check_Man.setChecked(true);
                gender = "Man";
            }else{
                check_Woman.setChecked(true);
                gender = "Woman";
            }
            btn_Insert.setEnabled(false);
            btn_Update.setEnabled(true);
>>>>>>> f8c48378687c403c23fea64a612ad0e2c0749b3c
        }
        mButton_pop= (Button)findViewById(R.id.button1);
        mButton_pop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시 팝업 메뉴가 나오게 하기
                // PopupMenu 는 API 11 레벨부터 제공한다
                PopupMenu p = new PopupMenu(
                        getApplicationContext(), // 현재 화면의 제어권자
                        v); // anchor : 팝업을 띄울 기준될 위젯
                getMenuInflater().inflate(R.menu.option_menu, p.getMenu());
                // 이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.scan: // 기기 검색
                                // Launch the DeviceListActivity to see devices and do scan
                                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);   // 기기 연결 요청
                                return true;
                            case R.id.discoverable: // 검색 허용
                                // Ensure this device is discoverable by others
                                ensureDiscoverable();   // 300초간 검색 허용
                                return true;
                        }
                        return false;
                    }
                });
                p.show(); // 메뉴를 띄우기
            }
        });
        /**requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        // Set up the custom title
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);
*/
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        /**
         // DB테스트 버튼
         btn_Insert = (Button) findViewById(R.id.btn_insert);
         btn_Insert.setOnClickListener(this);
         btn_Update = (Button) findViewById(R.id.btn_update);
         btn_Update.setOnClickListener(this);
         btn_Select = (Button) findViewById(R.id.btn_select);
         btn_Select.setOnClickListener(this);
         edit_ID = (EditText) findViewById(R.id.edit_id);
         edit_Name = (EditText) findViewById(R.id.edit_name);
         edit_Age = (EditText) findViewById(R.id.edit_age);
         text_ID = (TextView) findViewById(R.id.text_id);
         text_Name = (TextView) findViewById(R.id.text_name);
         text_Age = (TextView) findViewById(R.id.text_age);
         text_Gender= (TextView) findViewById(R.id.text_gender);
         check_Man = (CheckBox) findViewById(R.id.check_man);
         check_Man.setOnClickListener(this);
         check_Woman = (CheckBox) findViewById(R.id.check_woman);
         check_Woman.setOnClickListener(this);
         check_ID = (CheckBox) findViewById(R.id.check_userid);
         check_ID.setOnClickListener(this);
         check_Name = (CheckBox) findViewById(R.id.check_name);
         check_Name.setOnClickListener(this);
         check_Age = (CheckBox) findViewById(R.id.check_age);
         check_Age.setOnClickListener(this);

         arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
         ListView listView = (ListView) findViewById(R.id.db_list_view);
         listView.setAdapter(arrayAdapter);
         listView.setOnItemClickListener(onClickListener);
         listView.setOnItemLongClickListener(longClickListener);

         mDbOpenHelper = new DbOpenHelper(this);
         mDbOpenHelper.open();
         mDbOpenHelper.create();

         check_ID.setChecked(true);
         showDatabase(sort);

         btn_Insert.setEnabled(true);
         btn_Update.setEnabled(false);
         // DB
         */
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });

        // Initialize the BluetoothService to perform bluetooth connections
        mChatService = new BluetoothService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendMessage(message);
                    }
                    if (D) Log.i(TAG, "END onEditorAction");
                    return true;
                }
            };

    // The Handler that gets information back from the BluetoothService
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
                    readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    if (true);
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(readMessage);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    // 드롭다운 메뉴 생성 - res/menu/option_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // 메뉴 선택 시 행동
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan: // 기기 검색
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);   // 기기 연결 요청
                return true;
            case R.id.discoverable: // 검색 허용
                // Ensure this device is discoverable by others
                ensureDiscoverable();   // 300초간 검색 허용
                return true;
        }
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
    }
=======
<<<<<<< HEAD
        return false;
=======

    }
  

>>>>>>> Stashed changes
    public void onClick(View button){
        PopupMenu popup=new PopupMenu(this,button);
        popup.getMenuInflater().inflate(R.menu.menu,popup.getMenu());
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getApplicationContext(),"클릭된 팝업 메뉴:"+item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
        popup.show();
<<<<<<< Updated upstream
>>>>>>> ff40487c20db7ebe170804e33a2a811eaf8d7d87
>>>>>>> Stashed changes
=======

>>>>>>> f8c48378687c403c23fea64a612ad0e2c0749b3c
    }

    /**
     //DB
     public void setInsertMode(){
     edit_ID.setText("");
     edit_Name.setText("");
     edit_Age.setText("");
     check_Man.setChecked(false);
     check_Woman.setChecked(false);
     btn_Insert.setEnabled(true);
     btn_Update.setEnabled(false);
     }

     //클릭리스너 in DB
     private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Log.e("On Click", "position = " + position);
    nowIndex = Long.parseLong(arrayIndex.get(position));
    Log.e("On Click", "nowIndex = " + nowIndex);
    Log.e("On Click", "Data: " + arrayData.get(position));
    String[] tempData = arrayData.get(position).split("\\s+");
    Log.e("On Click", "Split Result = " + tempData);
    edit_ID.setText(tempData[0].trim());
    edit_Name.setText(tempData[1].trim());
    edit_Age.setText(tempData[2].trim());
    if(tempData[3].trim().equals("Man")){
    check_Man.setChecked(true);
    gender = "Man";
    }else{
    check_Woman.setChecked(true);
    gender = "Woman";
    }
    btn_Insert.setEnabled(false);
    btn_Update.setEnabled(true);
>>>>>>> Stashed changes
    }
    };

     //롱클릭 리스터 in DB
     private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    Log.d("Long Click", "position = " + position);
    nowIndex = Long.parseLong(arrayIndex.get(position));
    String[] nowData = arrayData.get(position).split("\\s+");
    String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3];
    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
    dialog.setTitle("데이터 삭제")
    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
    .setPositiveButton("네", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
    Toast.makeText(MainActivity.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
    mDbOpenHelper.deleteColumn(nowIndex);
    showDatabase(sort);
    setInsertMode();
    }
    })
    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
    Toast.makeText(MainActivity.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
    setInsertMode();
    }
    })
    .create()
    .show();
    return false;
    }
    };

     //데이터 베이스 출력 함수
     public void showDatabase(String sort){
     Cursor iCursor = mDbOpenHelper.sortColumn(sort);
     Log.d("showDatabase", "DB Size: " + iCursor.getCount());
     arrayData.clear();
     arrayIndex.clear();
     while(iCursor.moveToNext()){
     String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
     String tempID = iCursor.getString(iCursor.getColumnIndex("userid"));
     tempID = setTextLength(tempID,10);
     String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
     tempName = setTextLength(tempName,10);
     String tempAge = iCursor.getString(iCursor.getColumnIndex("age"));
     tempAge = setTextLength(tempAge,10);
     String tempGender = iCursor.getString(iCursor.getColumnIndex("gender"));
     tempGender = setTextLength(tempGender,10);

     String Result = tempID + tempName + tempAge + tempGender;
     arrayData.add(Result);
     arrayIndex.add(tempIndex);
     }
     arrayAdapter.clear();
     arrayAdapter.addAll(arrayData);
     arrayAdapter.notifyDataSetChanged();
     }

     //텍스트 길이 조정 in DB
     public String setTextLength(String text, int length){
     if(text.length()<length){
     int gap = length - text.length();
     for (int i=0; i<gap; i++){
     text = text + " ";
     }
     }
     return text;
     }
     */
    /**
     @Override
     public void onClick(View v) {
     if(btService.getDeviceState()){
     // 블루투스 지원 기기
     btService.enableBluetooth();
     } else {
     Toast.makeText(MainActivity.this, "블루투스를 지원하지 않는 기기입니다. 프로그램을 종료합니다.", Toast.LENGTH_SHORT).show();
     finish();
     }

     //DB
     switch (v.getId()) {
     case R.id.btn_insert:
     ID = edit_ID.getText().toString();
     name = edit_Name.getText().toString();
     age = Long.parseLong(edit_Age.getText().toString());
     mDbOpenHelper.open();
     mDbOpenHelper.insertColumn(ID, name, age, gender);
     showDatabase(sort);
     setInsertMode();
     edit_ID.requestFocus();
     edit_ID.setCursorVisible(true);
     mDbOpenHelper.close();
     break;

     case R.id.btn_update:
     ID = edit_ID.getText().toString();
     name = edit_Name.getText().toString();
     age = Long.parseLong(edit_Age.getText().toString());
     mDbOpenHelper.updateColumn(nowIndex,ID, name, age, gender);
     showDatabase(sort);
     setInsertMode();
     edit_ID.requestFocus();
     edit_ID.setCursorVisible(true);
     break;

     case R.id.btn_select:
     showDatabase(sort);
     break;

     case R.id.check_man:
     check_Woman.setChecked(false);
     gender = "Man";
     break;

     case R.id.check_woman:
     check_Man.setChecked(false);
     gender = "Woman";
     break;

     case R.id.check_userid:
     check_Name.setChecked(false);
     check_Age.setChecked(false);
     sort = "userid";
     break;

     case R.id.check_name:
     check_ID.setChecked(false);
     check_Age.setChecked(false);
     sort = "name";
     break;

     case R.id.check_age:
     check_ID.setChecked(false);
     check_Name.setChecked(false);
     sort = "age";
     break;
     }
     }
     // 블루투스
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
     Log.d(TAG,"onActivityResult " + resultCode);

     switch (requestCode) {

     case REQUEST_CONNECT_DEVICE:
     // When DeviceListActivity returns with a device to connect
     if (resultCode == Activity.RESULT_OK) {
     btService.getDeviceInfo(data);
     }
     break;
     case REQUEST_ENABLE_BT:
     // 블루투스 요청이 가능할 때
     if(resultCode == Activity.RESULT_OK){
     // Next Step
     btService.scanDevice();
     } else {
     Log.d(TAG,"요청 거부");
     Toast.makeText(MainActivity.this, "블루투스를 허용해야 정상적으로 기능이 이용 가능합니다!", Toast.LENGTH_SHORT).show();
     }
     break;
     }
     }
     */
}
