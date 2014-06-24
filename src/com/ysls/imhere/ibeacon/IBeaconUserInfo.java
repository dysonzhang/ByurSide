package com.ysls.imhere.ibeacon;

import android.os.Parcel;
import android.os.Parcelable;

public class IBeaconUserInfo implements Parcelable {
	/**
	 * �û�ID
	 */
	private String userID = "";
	/**
	 * ����
	 */
	private String password = "";
	/**
	 * user key
	 */
	private String userKey = "";

	private String devicesId = "";

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getDevicesId() {
		return devicesId;
	}

	public void setDevicesId(String devicesId) {
		this.devicesId = devicesId;
	}

	public static final Parcelable.Creator<IBeaconUserInfo> CREATOR = new Creator<IBeaconUserInfo>() {
		public IBeaconUserInfo createFromParcel(Parcel source) {
			IBeaconUserInfo mIBeaconUserInfo = new IBeaconUserInfo();

			mIBeaconUserInfo.userID = source.readString();
			mIBeaconUserInfo.password = source.readString();
			mIBeaconUserInfo.userKey = source.readString();

			mIBeaconUserInfo.devicesId = source.readString();

			return mIBeaconUserInfo;
		}

		public IBeaconUserInfo[] newArray(int size) {
			return new IBeaconUserInfo[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(userID);
		arg0.writeString(password);
		arg0.writeString(userKey);
		arg0.writeString(devicesId);
	}
}
