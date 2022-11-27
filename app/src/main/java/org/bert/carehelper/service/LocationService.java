package org.bert.carehelper.service;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;



import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;

import org.bert.carehelper.entity.CommandResponse;

import java.io.IOException;
import java.util.List;

public class LocationService  implements TencentLocationListener, Service {

    private final String TAG = "LocationService";

    private Context context;

    private TencentLocation location;

    private TencentLocationManager mLocationManager;

    private boolean canGetLocation = false;

    public LocationService(Context context) {
        this.context = context;
        this.mLocationManager = TencentLocationManager.getInstance(this.context);

        // 初始化地图SDK
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setAllowGPS(true);
        request.setAllowDirection(true);
        request.setIndoorLocationMode(true);
        this.mLocationManager.requestLocationUpdates(request, this, Looper.getMainLooper());
    }

    public void getAddress(double latitude, double longitude) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this.context);
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null) {
            for (Address address : addressList) {
                Log.d(TAG, String.format("address: %s", address.toString()));
            }
        }
    }

    /**
     * 关闭地理位置获取
     */
    public void removeUpdates() {
        this.mLocationManager.removeUpdates(this);
        Log.i(TAG, "关闭成功;");
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if ("OK".equals(s)) {
            this.location = tencentLocation;
            this.setCanGetLocation(true);
            Log.i(TAG, "get location success! location is " + this.location.getAddress() + ";");
        } else {
            Log.e(TAG, "location failed");
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        System.out.println(s);
    }

    @Override
    public CommandResponse doCommand(String content) {

        return null;
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }

    public void setCanGetLocation(boolean canGetLocation) {
        this.canGetLocation = canGetLocation;
    }
}
