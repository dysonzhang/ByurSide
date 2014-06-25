package com.ysls.imhere.ibeacon;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.ysls.imhere.config.Constants;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.utils.LogUtil;

/**
 * Bluetooth Controller
 * 
 * @author dyson
 * 
 */
public class BluetoothController {

	public static String TAG = "BluetoothController";

	public static BluetoothController btController;

	private Context context;

	public static BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

	public ShopplayIBeaconService mBoundService;

	public BluetoothController(Context context) {
		this.context = context;
	}

	public static BluetoothController getInstance(Context context) {
		if (btController == null)
			btController = new BluetoothController(context);
		return btController;
	}

	/**
	 * turn on the local adapter device
	 * 
	 * @return
	 */
	public static void openBTAdapter() {
		if (!mBtAdapter.isEnabled()) {
			mBtAdapter.enable();
		}
	}

	/**
	 * turn off the local adapter device
	 * 
	 * @return
	 */
	public static void closeBTAdapter() {
		if (mBtAdapter.isEnabled()) {
			mBtAdapter.disable();
		}
	}

	/**
	 * Check if Bluetooth LE is supported by this Android device, and if so,
	 * make sure it is enabled. then state BT2.0 MAC address search service or
	 * BLE4.0 iBeacon search service
	 * 
	 */
	@SuppressLint("NewApi")
	public void startBTCheckInService() {
		if (!context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			LogUtil.i(TAG, "Bluetooth LE not supported");
			// start MAC address search service
			Global.CHECKIN_MODE = Constants.BT_GENREAL_CHECKIN_MODE;

			Intent serviceIntent = new Intent(context,GeneralBluetoothService.class);
			context.startService(serviceIntent);
			
		} else {
			if (((BluetoothManager) context
					.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter()
					.isEnabled()) {
				LogUtil.i(TAG, "Bluetooth LE supported");

				Intent serviceIntent = new Intent(context,
						ShopplayIBeaconService.class);
				context.startService(serviceIntent);
				
				if (context.getApplicationContext().bindService(serviceIntent,
						iBeaconServiceConnection, Context.BIND_AUTO_CREATE)) {
					LogUtil.i(TAG, "Shopplay IBeacon Service start  Success");
					Global.CHECKIN_MODE = Constants.BT_IBEACON_CHECKIN_MODE;
				} else {
					LogUtil.i(TAG, "Shopplay IBeacon Service start  Failed");
				}
			}
		}
	}

	private ServiceConnection iBeaconServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBoundService = ((ShopplayIBeaconService.ShopplayIBeaconServiceBinder) service)
					.getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	/**
	 * Check if Bluetooth LE is supported by this Android device, and if so,
	 * make sure it is enabled.
	 * 
	 * @return false if it is supported and not enabled
	 */
	public static boolean checkSupportBluetoothLe(Context context) {
		if (!context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			return false;
		}
		return true;
	}
}
