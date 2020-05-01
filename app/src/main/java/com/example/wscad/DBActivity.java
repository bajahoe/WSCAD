package com.example.wscad;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import static com.example.wscad.MainActivity.arrayData;
import static com.example.wscad.MainActivity.arrayIndex;

public class DBActivity extends AppCompatActivity {

    private DbOpenHelper mDbOpenHelper;
    String sort = "userid";
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_data);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        showDatabase(sort);
    }

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempUser = iCursor.getString(iCursor.getColumnIndex("userid"));
            tempUser = setTextLength(tempUser,10);
            String tempDate = iCursor.getString(iCursor.getColumnIndex("date"));
            tempDate = setTextLength(tempDate,15);
            String tempTime = iCursor.getString(iCursor.getColumnIndex("time"));
            tempTime = setTextLength(tempTime,12);
            String tempBPM = iCursor.getString(iCursor.getColumnIndex("bpm"));
            tempBPM = setTextLength(tempBPM,5);
            String tempStatus = iCursor.getString(iCursor.getColumnIndex("status"));
            tempStatus = setTextLength(tempStatus,10);

            String Result = tempUser + tempDate + tempTime + tempBPM + tempStatus;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }
}
