/**
 * 메시지 송신을 담당하는 클래스입니다.
 * */

package com.example.wscad;

import android.telephony.SmsManager;

public class SmsWrite {
    SmsWrite(String phoneNumber, String msg) {
        String phone = phoneNumber;
        String message = msg;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,message,null,null);
            /**
             * destinationAddress : 받는사람의 Phone Number입니다 신기하게도 String형식입니다
             * scAddress : 이건 잘 모르겠습니다 일단 null을 입력해 주세요 (구글API : is the service center address or null to use the current default SMSC)
             * text : 문자의 내용입니다
             * sentIntent : 문자 전송에 관련한 PendingIntent입니다 null을 넣어도 되지만 저는 전송 확인결과를 알아보기 위해 이것도 사용할 예정입니다
             * deliveryIntent : 문자 도착에 관련한 PendingIntent라고 합니다 null을 넣어도 되지만 한번 이것도 사용해 보겠습니다
             *
             *
             **/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
