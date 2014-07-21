package com.ysls.imhere.test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ysls.imhere.bean.TodoModel;


public class TestData {
	private static String[] names={"songhuiqiao.jpg","zhangzetian.jpg","songqian.jpg","hangxiaozhu.jpg","jingtian.jpg"
		,"liuyifei.jpg","kangyikun.jpg","dengziqi.jpg"};
	
	public static List<TodoModel> getRecentChats(){
		List<TodoModel> chats=new ArrayList<TodoModel>();
		chats.add(new TodoModel("非常紧急任务", "完成服务器项目部署…", "12:30","剩余3小时"));
		chats.add(new TodoModel("非常紧急任务", "完成后台项目开发…", "16:30","剩余1天"));
		chats.add(new TodoModel("非常紧急任务", "完成前台开发计划安排…", "17:30","剩余2天"));
		chats.add(new TodoModel("紧急任务", "完成APP主页UI设计…", "昨天" ,"剩余3天"));
		chats.add(new TodoModel("紧急任务", "完成移动端APP接口设计文档…", "星期一", "剩余4天"));
		chats.add(new TodoModel("紧急任务", "完成网监项目PPT方案…", "17:30", "剩余2天"));
		chats.add(new TodoModel("普通任务", "完成iBeacon考勤设备测试…", "昨天" ,"剩余3天"));
		chats.add(new TodoModel("普通任务", "完成项目计划书…", "星期一", "剩余4天"));
		return chats;
	}
	
}
