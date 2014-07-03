package com.ysls.imhere.test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ysls.imhere.bean.RecentChat;
import com.ysls.imhere.utils.FileUtil;


public class TestData {
	private static String[] names={"songhuiqiao.jpg","zhangzetian.jpg","songqian.jpg","hangxiaozhu.jpg","jingtian.jpg"
		,"liuyifei.jpg","kangyikun.jpg","dengziqi.jpg"};
	private static String dir=FileUtil.getRecentChatPath();
	public static List<RecentChat> getRecentChats(){
		String path=FileUtil.getRecentChatPath();
		List<RecentChat> chats=new ArrayList<RecentChat>();
		chats.add(new RecentChat("非常紧急任务", "完成服务器项目部署…", "12:30", path+names[0]));
		chats.add(new RecentChat("非常紧急任务", "完成服务器项目部署…", "16:30", path+names[1]));
		chats.add(new RecentChat("非常紧急任务", "完成服务器项目部署…", "17:30", path+names[2]));
		chats.add(new RecentChat("紧急任务", "完成服务器项目部署…", "昨天", path+names[3]));
		chats.add(new RecentChat("紧急任务", "完成服务器项目部署…", "星期一", path+names[4]));
		chats.add(new RecentChat("紧急任务", "完成服务器项目部署…", "17:30", path+names[5]));
		chats.add(new RecentChat("普通任务", "完成服务器项目部署…", "昨天", path+names[6]));
		chats.add(new RecentChat("普通任务", "完成服务器项目部署…", "星期一", path+names[7]));
		return chats;
	}
	
	public static HashMap<String, String> getFriends(){
		HashMap<String, String> maps=new HashMap<String, String>();
		maps.put("宋慧乔", dir+"songhuiqiao.jpg");
		maps.put("章泽天", dir+"zhangzetian.jpg");
		maps.put("宋茜", dir+"songqian.jpg");
		maps.put("韩孝珠", dir+"hangxiaozhu.jpg");
		maps.put("景甜", dir+"jingtian.jpg");
		maps.put("刘亦菲", dir+"liuyifei.jpg");
		maps.put("康逸琨", dir+"kangyikun.jpg");
		maps.put("邓紫棋", dir+"dengziqi.jpg");
		
		return maps;
	}
}
