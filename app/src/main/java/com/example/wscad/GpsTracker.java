package com.example.wscad;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;


public class GpsTracker extends Service implements LocationListener {

    private final Context mContext;
    Location mLocation; // 위치 클래스
    double mLatitude;   // 위도
    double mLongitude;   // 경도

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 업데이트 최소거리
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;  // 최소 업데이트 시간
    protected LocationManager mLocationManager;  // 로케이션메니저

    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);    // GPS의 활성화 여부
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);    // 네트워크 활성화 여부

            if (!isGPSEnabled && !isNetworkEnabled) {
            // 둘다 켜지지 않았을 때의 처리코드
            } else {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);  // 위치정보 퍼미션 승인 확인
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);    // 위치정보 지속 퍼미션 승인 확인

                // 퍼미션이 있는 상태
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                    ;
                } else
                    return null;

                // 네트워크가 켜졌다면
                if (isNetworkEnabled) {

                    // 위치정보를 원하는 시간, 거리마다 갱신해줍니다.
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this);

                    if (mLocationManager != null) {
                        // 마지막으로 검색되었던 위치를 얻는다.
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null) {
                            // 위도, 경도 저장
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (mLocation == null) {
                        // 위치정보를 원하는 시간, 거리마다 갱신해줍니다.
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this);
                        if (mLocationManager != null) {
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLocation != null) {
                                mLatitude = mLocation.getLatitude();
                                mLongitude = mLocation.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            Log.d("@@@", ""+e.toString());
        }
        return mLocation;
    }


    public double getLatitude()
    {
        if(mLocation != null)
        {
            mLatitude = mLocation.getLatitude();
        }

        return mLatitude;
    }

    public double getLongitude()
    {
        if(mLocation != null)
        {
            mLongitude = mLocation.getLongitude();
        }

        return mLongitude;
    }

    @Override
    public void onLocationChanged(Location location)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void stopUsingGPS()
    {
        if(mLocationManager != null)
        {
            mLocationManager.removeUpdates(GpsTracker.this);
        }
    }
}
