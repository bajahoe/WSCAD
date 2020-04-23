package com.example.wscad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main";
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
    };
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
        /** Main Layout **/
        btn_Connect = (Button) findViewById(R.id.btn_connect);
        textViewReceive = (TextView)findViewById(R.id.textView_receive);


        // 블루투스 클래스 생성
        if(btService == null){
            btService = new BluetoothService(this, mHandler);
        }


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
}
