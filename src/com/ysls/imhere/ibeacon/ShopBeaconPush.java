package com.ysls.imhere.ibeacon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ShopBeacon
 * 
 * @author dyson
 * @since 2013-12-12
 */
public class ShopBeaconPush implements Parcelable {

	
	private Long pushID;

	private String braLogo;

	private String pushTitle;

	private String proPic;

	private String pushContent;
	public Long getPushID() {
		return pushID;
	}

	public void setPushID(Long pushID) {
		this.pushID = pushID;
	}

	public String getBraLogo() {
		return braLogo;
	}

	public void setBraLogo(String braLogo) {
		this.braLogo = braLogo;
	}

	public String getPushTitle() {
		return pushTitle;
	}

	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}

	public String getProPic() {
		return proPic;
	}

	public void setProPic(String proPic) {
		this.proPic = proPic;
	}

	public String getPushContent() {
		return pushContent;
	}

	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}

	public static final Parcelable.Creator<ShopBeaconPush> CREATOR = new Creator<ShopBeaconPush>() {
		public ShopBeaconPush createFromParcel(Parcel source) {
			ShopBeaconPush mShopBeaconPush = new ShopBeaconPush();

			mShopBeaconPush.pushID = source.readLong();
			mShopBeaconPush.braLogo = source.readString();
			mShopBeaconPush.proPic = source.readString();
			mShopBeaconPush.pushContent = source.readString();
			mShopBeaconPush.pushTitle = source.readString();

			return mShopBeaconPush;
		}

		public ShopBeaconPush[] newArray(int size) {
			return new ShopBeaconPush[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeLong(pushID);
		arg0.writeString(braLogo);
		arg0.writeString(proPic);
		arg0.writeString(pushContent);
		arg0.writeString(pushTitle);

	}
}
