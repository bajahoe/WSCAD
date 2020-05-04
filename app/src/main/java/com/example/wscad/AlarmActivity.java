package com.example.wscad;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {
    Button b1;
    Thread alarmThread;
    Thread smsThread;
    Thread timerThread;

    String address;
    TextView timer_text;

    int time = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // 리소스 얻어오기
        b1 = (Button)findViewById(R.id.button);
        timer_text = (TextView)findViewById(R.id.timer_text);

        // 주변에 도움을 요청하기 위해 링톤 객체 생성
        Uri notification = Uri.parse("android.resource://com.example.wscad/raw/gen");
        final Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),notification);

        // 오디오 설정
        ringtone.setStreamType(AudioManager.STREAM_ALARM);

        // 파라미터 얻어오기
        Intent intent = getIntent();
        address = intent.getExtras().getString("address");

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    for(; time>0 ; time--) {

                        timer_text.setText(time+"초후 메시지가 전송됩니다.");
                        Thread.sleep(1000);

                    }
                    smsThread.start();
                } catch (Exception e) { }

            }
        });

        smsThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10000);    // 10초 이내로 취소 버튼을 누르지 않으면 심정지로 간주하고 메시지를 전송한다.
                    new SmsWrite("01068608374", "심정지 환자가\n\n" + address + "\n에서 발생했습니다.");
                } catch (Exception e) {  }

            }
        });


        alarmThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while(true) {
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(500); // 0.5초간 진동
                        ringtone.play();
                        Thread.sleep(5000);
                    }
                } catch (Exception e) {
                    ringtone.stop();
                }
            }
        });
        timerThread.start();
        alarmThread.start();


    }

    public void onClick1(View view) {
        finish();
    }

    @Override
    public void onDestroy( ) {

        super.onDestroy( );
        smsThread.interrupt();
        alarmThread.interrupt();

    }
}
