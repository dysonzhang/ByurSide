package com.ysls.imhere.ibeacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;

public class IBeaconRequest {
	public final static String ConnectTimeOutErrorCode = "ConnectTimeOutErrorCode";
	public final static String ReadTimeOutErrorCode = "ReadTimeOutErrorCode";

	public static final String iBeaconPushUrl = "POST_URL"
			+ "shopbeacon/shopBeaconPushService";
	private String requestUrl;
	private String requestParam;
	private int connectTimeout;
	private int readTimeout;
	private boolean isSendFinish = true;

	public boolean isSendFinish() {
		return isSendFinish;
	}

	public void setSendFinish(boolean isSendFinish) {
		this.isSendFinish = isSendFinish;
	}

	private Handler mHandler;

	public Handler getmHandler() {
		return mHandler;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String sendPost() {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {

			isSendFinish = false;

			URL realUrl = new URL(this.requestUrl);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(this.connectTimeout);
			conn.setReadTimeout(this.readTimeout);
			out = new PrintWriter(conn.getOutputStream());
			out.print(this.requestParam);
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			result = ConnectTimeOutErrorCode;
			if (out != null) {
				out.close();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			try {

				isSendFinish = true;

				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
