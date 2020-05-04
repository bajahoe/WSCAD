package com.example.wscad;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    // PopupMenu
    private Button mButton_pop;

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
    private final int MY_PERMISSION_REQUEST_SMS = 1001; // SMS 퍼미션 요청 결과
    private static final int GPS_ENABLE_REQUEST_CODE = 2001; // GPS 켜기 요청 결과
    private static final int PERMISSIONS_REQUEST_CODE = 100; // GPS 퍼미션 요청 결과
    String[] REQUIRED_PERMISSIONS  = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

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

    // DB 변수 및 테스트를 위한 디폴트값
    long nowIndex;
    int bpm;
    String status = "위험";
    String sort = "userid";
    // DB테스트

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        final Window window = getWindow();
        boolean useTitleFeature = false;
        if (window.getContainer() == null) {
            useTitleFeature = window
                    .requestFeature(Window.FEATURE_CUSTOM_TITLE);
        }
        setContentView(R.layout.activity_main); // 중복이니 코드수정
        if (useTitleFeature) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.custom_title);
            // Set up the custom title

            mTitle = (TextView) findViewById(R.id.title_left_text);
            //   mTitle.setText(R.string.app_name);//!@#$
            mTitle = (TextView) findViewById(R.id.title_right_text);

        }

        mButton_pop = (Button) findViewById(R.id.button1);
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

                            case R.id.diagnose:
                                Intent intent = new Intent(MainActivity.this, DBActivity.class);
                                startActivity(intent);
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
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        mDbOpenHelper.close();


        heartbit = (TextView) findViewById(R.id.textView2);
        permissionCheck(Manifest.permission.SEND_SMS, "메시지 전송");
        check_Status(5);
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
        //   mConversationView = (ListView) findViewById(R.id.in);
        //   mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key

        // Initialize the send button with a listener that for click events

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
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    heartbit.setText(readMessage);
                    bpm=Integer.parseInt(readMessage);
                    status = check_Status(bpm);
                    mDbOpenHelper.open();
                    mDbOpenHelper.insertColumn(Integer.toString(bpm), status);
                    mDbOpenHelper.close();
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

    // 상태 체크 함수. BPM 에 따라 결과 반환
    private String check_Status(int BPM) {
        String status = "";
        if (BPM <= 5) {
            status = "응급";
            Intent intent = new Intent(this, GPSActivity.class);
            startActivityForResult(intent, RESULT_GPS_REQUEST); // go to onActivityResult => RESULT_GPS_REQUEST
        } else if (BPM < 60) {   // 서맥 관측
            status = "주의";
        } else if (BPM <= 100) {
            status = "정상";
        } else if (BPM > 100) {   // 빈맥 관측
            status = "주의";
        }
        return status;
    }

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
            case RESULT_GPS_REQUEST:
                String mLocation;
                double mLatitude;
                double mLongitude;
                // 성공적으로 위치를 조회했다면
                if (resultCode == RESULT_OK) {
                    // data를 이용한 처리과정
                    mLocation = data.getExtras().getString("Location");
                    mLatitude = data.getExtras().getDouble("latitude");
                    mLongitude = data.getExtras().getDouble("longitude");
                    new SmsWrite("01068608374", "심정지 환자가\n\n"+mLocation+"\n에서 발생했습니다.");
                    // 주변에 도움을 요청하기 위해 링톤 객체 생성
                    Uri notification = Uri.parse("android.resource://com.example.wscad/raw/gen");
                    final Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),notification);
                    // 알람 재생
                    ringtone.setStreamType(AudioManager.STREAM_ALARM);
                    ringtone.play();
/*
                    // 정지를 위한 코드일 뿐 나중에 버튼을 눌러 정지하는 방향으로 변경할 예정.
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable()  {
                        public void run() {
                            // 시간 지난 후 실행할 코딩
                            ringtone.stop();
                        }
                    }, 500); // 0.5초후*/
                } else {   // RESULT_CANCEL
                    Toast.makeText(MainActivity.this, "위치 조회에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
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

            case R.id.diagnose:
                Intent intent = new Intent(MainActivity.this, DBActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    // 실행 중 퍼미션 권한 획득 메서드. 메신저와 위치
    private void permissionCheck(String per, String msg){
        final String permission = per;
        final String message = msg;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Android provides a utility method, shouldShowRequestPermissionRationale(), that returns true if the user has previously
                // denied the request, and returns false if a user has denied a permission and selected the Don't ask again option in the
                // permission request dialog, or if a device policy prohibits the permission. If a user keeps trying to use functionality that
                // requires a permission, but keeps denying the permission request, that probably means the user doesn't understand why
                // the app needs the permission to provide that functionality. In a situation like that, it's probably a good idea to show an
                // explanation.

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("권한 설정");
                builder.setMessage("이 앱을 실행하려면 "+message+" 권한이 필요합니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {permission}, MY_PERMISSION_REQUEST_SMS);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {permission}, MY_PERMISSION_REQUEST_SMS);
            }
        }

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }
    }

    // 퍼미션 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permission granted.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"Permission denied.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        if ( requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if ( check_result ) {
                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}