package com.example.wscad;

import android.app.Activity;
import android.os.Bundle;
import android.provider.BaseColumns;

public final class DataBases {
    public static final class CreateDB implements BaseColumns {

        public static final String USERID = "userid";   // 유저 코드
        public static final String DATE = "date";   // 생성 날짜
        public static final String TIME = "time";   // 생성 시간
        public static final String BPM = "bpm"; // 측정된 BPM
        public static final String STATUS = "status"; // 측정된 BPM
        public static final String _TABLENAME0 = "userstatus";  // 유저 상태정보 테이블
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +USERID+" text not null , "
                +DATE+" text not null , "
                +TIME+" text not null , "
                +BPM+" text not null , "
                +STATUS+" text not null );";

    }
}