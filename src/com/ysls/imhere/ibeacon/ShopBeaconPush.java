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

	private Long merId;

	private String merName;

	private String merLogo;

	private Long braId;

	private String braName;

	private String braLogo;

	private int isHaveCheckin;

	private int isHaveScan;

	private int isHaveSuprise;

	private int braIsHavePushReward;

	public int getBraIsHavePushReward() {
		return braIsHavePushReward;
	}

	public void setBraIsHavePushReward(int braIsHavePushReward) {
		this.braIsHavePushReward = braIsHavePushReward;
	}

	public String getPushTitle() {
		return pushTitle;
	}

	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}

	public String getPushGuideContent() {
		return pushGuideContent;
	}

	public void setPushGuideContent(String pushGuideContent) {
		this.pushGuideContent = pushGuideContent;
	}

	private String pushTitle;

	private String pushGuideContent;

	private Long proId;

	private String proPic;

	private String pushContent;

	private int pushState;

	private int isHavePushReward;

	public String getBraLogo() {
		return braLogo;
	}

	public void setBraLogo(String braLogo) {
		this.braLogo = braLogo;
	}

	public int getIsHaveCheckin() {
		return isHaveCheckin;
	}

	public void setIsHaveCheckin(int isHaveCheckin) {
		this.isHaveCheckin = isHaveCheckin;
	}

	public int getIsHaveScan() {
		return isHaveScan;
	}

	public void setIsHaveScan(int isHaveScan) {
		this.isHaveScan = isHaveScan;
	}

	public int getIsHaveSuprise() {
		return isHaveSuprise;
	}

	public void setIsHaveSuprise(int isHaveSuprise) {
		this.isHaveSuprise = isHaveSuprise;
	}

	public int getIsHavePushReward() {
		return isHavePushReward;
	}

	public void setIsHavePushReward(int isHavePushReward) {
		this.isHavePushReward = isHavePushReward;
	}

	public int getPushState() {
		return pushState;
	}

	public void setPushState(int pushState) {
		this.pushState = pushState;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public Long getProId() {
		return proId;
	}

	public void setProId(Long proId) {
		this.proId = proId;
	}

	public String getProPic() {
		return proPic;
	}

	public Long getPushID() {
		return pushID;
	}

	public void setPushID(Long pushID) {
		this.pushID = pushID;
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

	public Long getMerId() {
		return merId;
	}

	public void setMerId(Long merId) {
		this.merId = merId;
	}

	public String getMerLogo() {
		return merLogo;
	}

	public void setMerLogo(String merLogo) {
		this.merLogo = merLogo;
	}

	public Long getBraId() {
		return braId;
	}

	public void setBraId(Long braId) {
		this.braId = braId;
	}

	public String getBraName() {
		return braName;
	}

	public void setBraName(String braName) {
		this.braName = braName;
	}

	public static final Parcelable.Creator<ShopBeaconPush> CREATOR = new Creator<ShopBeaconPush>() {
		public ShopBeaconPush createFromParcel(Parcel source) {
			ShopBeaconPush mShopBeaconPush = new ShopBeaconPush();

			mShopBeaconPush.pushID = source.readLong();
			mShopBeaconPush.merId = source.readLong();
			mShopBeaconPush.merName = source.readString();
			mShopBeaconPush.merLogo = source.readString();

			mShopBeaconPush.braId = source.readLong();
			mShopBeaconPush.braName = source.readString();
			mShopBeaconPush.braLogo = source.readString();

			mShopBeaconPush.isHaveCheckin = source.readInt();
			mShopBeaconPush.isHaveScan = source.readInt();
			mShopBeaconPush.isHaveSuprise = source.readInt();

			mShopBeaconPush.proId = source.readLong();
			mShopBeaconPush.proPic = source.readString();
			mShopBeaconPush.pushContent = source.readString();
			mShopBeaconPush.pushState = source.readInt();

			mShopBeaconPush.isHavePushReward = source.readInt();

			mShopBeaconPush.braIsHavePushReward = source.readInt();
			mShopBeaconPush.pushGuideContent = source.readString();
			mShopBeaconPush.pushTitle = source.readString();

			return mShopBeaconPush;
		}

		public ShopBeaconPush[] newArray(int size) {
			return new ShopBeaconPush[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeLong(pushID);
		arg0.writeLong(merId);
		arg0.writeString(merName);
		arg0.writeString(merLogo);

		arg0.writeLong(braId);
		arg0.writeString(braName);
		arg0.writeString(braLogo);

		arg0.writeInt(isHaveCheckin);
		arg0.writeInt(isHaveScan);
		arg0.writeInt(isHaveSuprise);

		arg0.writeLong(proId);
		arg0.writeString(proPic);
		arg0.writeString(pushContent);
		arg0.writeInt(pushState);
		arg0.writeInt(isHavePushReward);

		arg0.writeInt(braIsHavePushReward);
		arg0.writeString(pushGuideContent);
		arg0.writeString(pushTitle);

	}
}
