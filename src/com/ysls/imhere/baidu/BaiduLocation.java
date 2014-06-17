package com.ysls.imhere.baidu;

import android.content.Context;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ysls.imhere.utils.Golbal;

public class BaiduLocation {
	private static BaiduLocation bl = null;

	public String lat;
	public String lon;

	public LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;

	public BaiduLocation(Context paramContext) {
		this.mLocationClient = new LocationClient(paramContext);
		this.mMyLocationListener = new MyLocationListener();
		this.mLocationClient.registerLocationListener(this.mMyLocationListener);
		setLocationParm();
	}

	public static BaiduLocation getBaiduLocationInstance(Context paramContext) {
		if (bl == null)
			bl = new BaiduLocation(paramContext);
		return bl;
	}

	private void setLocationParm() {
		LocationClientOption localLocationClientOption = new LocationClientOption();
		localLocationClientOption
				.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		localLocationClientOption.setCoorType("bd09ll");
		localLocationClientOption.setScanSpan(10000);
		localLocationClientOption.setIsNeedAddress(true);
		localLocationClientOption.setNeedDeviceDirect(true);
		this.mLocationClient.setLocOption(localLocationClientOption);
	}

	private void updateMyLocation(Double lon, Double lat) {
		try {
			if ((lon != null) && (lat != null)) {

				Golbal.lon = lon + "";
				Golbal.lat = lat + "";
				Log.i("mylocation is : ", lon + "," + lat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendLocationReq() {
		mLocationClient.start();
		if ((mLocationClient != null) && (mLocationClient.isStarted())) {
			mLocationClient.requestLocation();
		}
		Log.i("LocSDK3", "locClient is null or not started");
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());

			updateMyLocation(location.getLongitude(), location.getLatitude());

			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}

			Log.i("BaiduLocationApi", sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}
}