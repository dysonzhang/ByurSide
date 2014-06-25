package com.ysls.imhere.ibeacon;

import com.ysls.imhere.utils.LogUtil;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

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
	
	public static BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	
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
		
		// Register for broadcasts when a device is discovered
		IntentFilter discoveryFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, discoveryFilter);

		// Register for broadcasts when discovery has finished
		IntentFilter foundFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, foundFilter);
		
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
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//					list.add(new SiriListItem(device.getName() + "\n"
//							+ device.getAddress(), false));
//					mAdapter.notifyDataSetChanged();
//					mListView.setSelection(list.size() - 1);
					
					LogUtil.i(TAG, device.getName() + " "+ device.getAddress());
					BTCheckInUtil.sentCheckInNotice(mContext);
					
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
//				setProgressBarIndeterminateVisibility(false);
//				if (mListView.getCount() == 0) {
//					tip_msg.setText("非常抱歉！在您的身边的未发现蓝牙门禁设备或是距离过远。请靠近门禁设备后重试。");
//					mAdapter.notifyDataSetChanged();
//					mListView.setSelection(list.size() - 1);
//				}
//				seachButton.setText("重新搜索");
			}
		}
	};
	
	/**
	 * 
	 * @author Administrator
	 * 
	 */
	public class GeneralBluetoothServiceBinder extends Binder {
		public GeneralBluetoothService getService() {
			return GeneralBluetoothService.this;
		}
	}
}