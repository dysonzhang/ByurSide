package com.ysls.imhere.task.param;

import com.litesuits.http.request.param.HttpParam;

public class Man implements HttpParam {
	private String userEmail;
	private String userPassword;

	public Man(String userEmail, String userPassword) {
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}
}
