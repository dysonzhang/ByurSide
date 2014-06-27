package com.ysls.imhere.ibeacon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.ysls.imhere.config.Constants;
import com.ysls.imhere.utils.LogUtil;

/**
 * GeneralBluetoothService Use BT2.1 protocol to adapter other android device
 * 
 * @author dyson
 * 
 */
public class GeneralBluetoothService extends Service {

	public static final String TAG = "GeneralBluetoothService";

	private static GeneralBluetoothService mGeneralBluetoothService;

	public static Context mContext;

	public static BluetoothAdapter mBtAdapter = BluetoothAdapter
			.getDefaultAdapter();

	public static List<BluetoothDevice> DeviceList = new ArrayList<BluetoothDevice>();

	private static Timer timer = new Timer();

	@Override
	public IBinder onBind(Intent intent) {
		return new GeneralBluetoothServiceBinder();
	}

	public static GeneralBluetoothService getInstance() {
		if (mGeneralBluetoothService == null)
			mGeneralBluetoothService = new GeneralBluetoothService();
		return mGeneralBluetoothService;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		// Register for broadcasts when a device is discovered
		IntentFilter discoveryFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, discoveryFilter);

		if (!mBtAdapter.isDiscovering()) {
			mBtAdapter.startDiscovery();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		// Unregister broadcast listeners
		this.unregisterReceiver(mReceiver);
	}

	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				LogUtil.i(TAG, "ACTION_FOUND : " + device.getName() + " "
						+ device.getAddress());

				if (Constants.DEVICE_MAC.equals(device.getAddress())) {
					DeviceList.add(device);
				}

				timer.schedule(new ScanTask(), Constants.BT_GENREAL_SCAN_TIME);
			}
		}
	};

	/**
	 * Restart discovery task
	 * 
	 * @author dyson
	 * 
	 */
	static class ScanTask extends TimerTask {
		public void run() {
			LogUtil.i(TAG, "ScanTask");
			if (DeviceList.size() > 0) {
				// sendRequest to check in API with parameter DeviceList
				BTCheckInUtil.sendCheckInNotice(mContext);
				DeviceList.clear();
			}
			// Discovery again
			if (mBtAdapter.isDiscovering()) {
				mBtAdapter.cancelDiscovery();
			}
			mBtAdapter.startDiscovery();
		}
	}
	/**
	 * Start BT2.1 General service
	 * 
	 * @param context
	 */
	public static void startBackService(Context context) {
		GeneralBluetoothService.mContext = context;
		if (!GeneralBluetoothService.isServiceExisted(context,
				ShopplayIBeaconService.class)) {
			LogUtil.i(TAG, "GeneralBluetoothService begin to start...");
			Intent intent = new Intent(context, GeneralBluetoothService.class);
			context.startService(intent);
		} else {

		}
	}

	/**
	 * Stop BT2.1 General service
	 * 
	 * @param context
	 */
	public static void stopBackService(Context context) {
		if (GeneralBluetoothService.isServiceExisted(context,
				ShopplayIBeaconService.class)) {
			LogUtil.i(TAG, "GeneralBluetoothService begin to stop...");
			Intent intent = new Intent(context, GeneralBluetoothService.class);
			context.stopService(intent);
		}
	}

	public static boolean isServiceExisted(Context context, Class service) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;
			if (serviceName.getClassName().equals(service.getName())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * GeneralBluetoothServiceBinder
	 * 
	 * @author dyson
	 * 
	 */
	public class GeneralBluetoothServiceBinder extends Binder {
		public GeneralBluetoothService getService() {
			return GeneralBluetoothService.this;
		}
	}
}