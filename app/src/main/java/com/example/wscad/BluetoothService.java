/*
블루투스 관련 기능을 모아둔 파일
 */

package com.example.wscad;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    private static final String TAG = "BluetoothService";

    // Intent 결과 코드
    private  static final int REQUEST_CONNECT_DEVICE=1;
    private  static final int REQUEST_ENABLE_BT=2;  // 블루투스 활성화 상태

    // RFCOMM Protocol UUID는 블루투스의 통신 프로토콜.
    private  static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button buttonSend; // 송신하기 위한 버튼
    private EditText textViewWrite; // 송신 할 데이터를 작성하기 위한 에딧 텍스트

    private BluetoothAdapter btAdapter; // 블루투스 어댑터

    private Activity mActivity;
    private Handler mHandler;

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    // 기본 예제 블루투스 챗 내의 변수들
    private int mState;
    // 상태를 나타내는 상태 변수
    private static final int STATE_NONE = 0; // we're doing nothing
    private static final int STATE_LISTEN = 1; // now listening for incoming connections
    private static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    private static final int STATE_CONNECTED = 3; // now connected to a remote device

    // 블투 상태 set
    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }
    // 블투 상태 get
    public synchronized int getState() {
        return mState;
    }

    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread == null) {}
        else {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) { }
        else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    // 커넥트 스레드 초기화, 기기의 모든 연결 제거
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread == null) { }
            else {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) { }
        else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);

        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    // 커넥티드 스레드 초기화
    public synchronized void connected(BluetoothSocket socket,
                                       BluetoothDevice device) {
        Log.d(TAG,"connected");
        // Cancel the thread that completed the connection
        if (mConnectThread == null) {}
        else {
            mConnectThread.cancel();
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {}
        else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissios
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    // 모든 스레드 정지
    public synchronized void stop() {
        Log.d(TAG, "stop");
        if(mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    // 값을 보내는 부분
    public void write(byte[] out) { // Create temporary object
        ConnectedThread r; //Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
        } // Perform the write unsynchronized r.write(out);
    }

    // 연결 실패했을 때
    private void connectionFailed() {
        Log.d(TAG,"Connection failed.");
        setState(STATE_LISTEN);
    }

    // 연결 잃었을 때
    private void connectionLost() {
        Log.d(TAG,"Connection Lost.");
        setState(STATE_LISTEN);
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket; // 블루투스 소켓
        private final BluetoothDevice mmDevice; // 블루투스 디바이스

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // 디바이스 정보를 얻어서 Bluetooth Socket 생성
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG,"create() failed", e);
            }

            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG,"BEGIN mConnecetThread");
            setName("ConnectThread");
            // 연결을 시도하기 전에는 항상 기기 검색을 중지한다.
            // 기기 검색이 계속되면 연결속도가 느려지기 때문이다.
            btAdapter.cancelDiscovery();

            // 블루투스소켓 연결 시도
            try {
                // 블투 소켓 연결
                mmSocket.connect();
                Log.d(TAG, "Connect Success");
            } catch (IOException e) {
                connectionFailed(); // 연결 실패시 호출 메서드
                Log.d(TAG, "Connect Fail");
                // 소켓을 닫는다.
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // 연결중 혹은 연결 대기상태인 메서드 호출
                BluetoothService.this.start();
                return;
            }

            // 스레드 연결 클래스를 초기화
            synchronized (BluetoothService.this) {
                mConnectedThread = null;
            }

            // 스레드 연결을 시작
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket; // 블루투스 소켓
        private final InputStream mmInStream; // 블루투스에 데이터를 입력하기 위한 입력 스트림
        private final OutputStream mmOutStream; // 블루투스에 데이터를 출력하기 위한 출력 스트림

        // 생성 후 인, 아웃풋 스트림 연결.
        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // 블투 소켓의 인풋과 아웃풋 스트림을 얻는다.
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "Begin mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;
            TextView textViewReceive = (TextView)mActivity.findViewById(R.id.textView_receive);
            // 연결된 동안 수신을 유지.
            while (true) {
                try {
                    // 데이터 수신 확인
                    int bytesAvailable = mmInStream.available();
                    if(bytesAvailable>0) {
                        bytes = mmInStream.read(buffer);
                        Log.i(TAG, Integer.toString(bytes));
                        //textViewReceive.append(bytes + "\n");
                    }

                } catch(IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }
        /**
         * Write to the connected OutStream.
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                // 값을 보내는 부분
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    public BluetoothService(Activity ac, Handler h) {
        // 메인으로부터 액티비티와 핸들러 전달받는 생성자.
        mActivity = ac;
        mHandler = h;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean getDeviceState() {
        // 연결 시도 기기가 블루투스를 지원하는지 확인하는 메서드
        Log.d(TAG, "블루투스 지원 여부 확인중..");


        if(btAdapter == null) {
            Log.d(TAG, "블루투스를 지원하지 않는 기기입니다!");
            return false;
        } else {
            Log.d(TAG,"블루투스 사용이 가능합니다.");

            return true;
        }
    }

    public void enableBluetooth() {
        // 블루투스의 온 오프 여부 확인
        Log.d(TAG, "Check the enabled Bluetooth");

        if(btAdapter.isEnabled()) {
            // 기기의 블루투스 상태가 On인 경우
            Log.d(TAG, "블루투스 사용중 입니다.");

            //Next Step
            scanDevice();
        } else {
            // Off인 경우
            Log.d(TAG, "블루투스를 켜주세요.");

            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(i,REQUEST_ENABLE_BT);
        }
    }

    public void scanDevice() {
        /** 블루투스 기기 검색 메서드 **/
        Log.d(TAG, "Scan Device");
        Intent serverIntent = new Intent(mActivity, DeviceListActivity.class);
        mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void getDeviceInfo(Intent data) {
        /** 검색된 기기에 접속하기 위한 메서드 **/
        // 기기의 MAC 주소 전달
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // 블루투스 기기 객체 전달
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        Log.d(TAG,"Get Device Info \n" + "address : "+ address);
        // 기기 연결.
        connect(device);
    }
}
