/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ysls.imhere;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.easemob.chatuidemo.adapter.NewFriendsMsgAdapter;
import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Constants;
import com.ysls.imhere.db.InviteMessgeDao;
import com.ysls.imhere.domain.InviteMessage;

public class NewFriendsMsgActivity extends BaseActivity {
	private ListView listView;
	private ImageView addContactView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends_msg);
		
		addContactView = (ImageView) findViewById(R.id.iv_new_contact);
		// 进入添加好友页
		addContactView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(NewFriendsMsgActivity.this,
						AddContactActivity.class));
			}
		});
		
		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		// 设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		MyApplication.getInstance().getContactList()
				.get(Constants.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);

	}

	public void back(View view) {
		finish();
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
		
	}

}
