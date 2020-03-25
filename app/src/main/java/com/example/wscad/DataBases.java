package com.example.wscad;

import android.provider.BaseColumns;

public final class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String USERID = "userid";
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String GENDER = "gender";
        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +USERID+" text not null , "
                +NAME+" text not null , "
                +AGE+" integer not null , "
                +GENDER+" text not null );";
    }
}

/*package com.example.wscad;

import android.provider.BaseColumns;

public final class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String USERID = "userid";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String BPM = "bpm";
        public static final String STATUS = "status";
        public static final String _TABLENAME0 = "status";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +USERID+" text not null , "
                +DATE+" text not null , "
                +TIME+" text not null , "
                +BPM+" integer not null , "
                +STATUS+" text not null );";
    }
}
*/