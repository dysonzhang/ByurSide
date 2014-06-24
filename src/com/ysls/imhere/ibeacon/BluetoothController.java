package com.ysls.imhere.ibeacon;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import com.ysls.imhere.utils.LogUtil;

/**
 * Bluetooth Controller
 * @author dyson
 *
 */
public class BluetoothController {

	public static String TAG = "BluetoothController";

	public static BluetoothController btController;

	private Context context;

	public BluetoothAdapter mBtAdapter;

	public BluetoothController(Context context) {
		this.context = context;
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public static BluetoothController getInstance(Context context) {
		if (btController == null)
			btController = new BluetoothController(context);
		return btController;
	}

	/**
	 * true if the local adapter device is turned on
	 * 
	 * @return
	 */
	public boolean bluetoothEnablState() {
		return mBtAdapter.isEnabled();
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
			LogUtil.w(TAG, "Bluetooth LE not supported by this device");

			// start MAC address search service
		} else {
			if (((BluetoothManager) context
					.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter()
					.isEnabled()) {
				// start iBeacon search service
				LogUtil.i(TAG, "ShopplayIBeaconService.startBackService");
				ShopplayIBeaconService.startBackService(context);
			}
		}
	}

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
