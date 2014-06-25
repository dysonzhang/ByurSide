package com.ysls.imhere.baidu;

import android.content.Context;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ysls.imhere.config.Global;

/**
 * Baidu Location API
 * @author dyson
 *
 */
public class BaiduLocation {
	private static BaiduLocation bl = null;

	public String lat;
	public String lon;

	public LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;

	public BaiduLocation(Context context) {
		 mLocationClient = new LocationClient(context);
		 mMyLocationListener = new MyLocationListener();
		 mLocationClient.registerLocationListener(this.mMyLocationListener);
		setLocationParm();
	}

	public static BaiduLocation getBaiduLocationInstance(Context context) {
		if (bl == null)
			bl = new BaiduLocation(context);
		return bl;
	}

	private void setLocationParm() {
		LocationClientOption lClientOption = new LocationClientOption();
		lClientOption
				.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		lClientOption.setCoorType("bd09ll");
		lClientOption.setScanSpan(20000);
		lClientOption.setIsNeedAddress(true);
		lClientOption.setNeedDeviceDirect(true);
		mLocationClient.setLocOption(lClientOption);
	}

	private void updateMyLocation(Double lon, Double lat) {
		try {
			if ((lon != null) && (lat != null)) {

				Global.lon = lon + "";
				Global.lat = lat + "";
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
	
	public void stopLocationReq() {
		if ((mLocationClient != null) && (mLocationClient.isStarted())) {
			mLocationClient.stop();
		}
		Log.i("LocSDK3", "locClient is null or not stop");
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

//			Log.i("BaiduLocationApi", sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}
}