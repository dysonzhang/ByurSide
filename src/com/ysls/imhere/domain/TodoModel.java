package com.ysls.imhere.domain;

public class TodoModel {
	private String todoTitle;
	private String todoContent;
	private String todoTime;
	private String todoOutTime;

	public TodoModel(String todoTitle, String todoContent, String todoTime,
			String todoOutTime) {
		super();
		this.todoTitle = todoTitle;
		this.todoContent = todoContent;
		this.todoTime = todoTime;
		this.todoOutTime = todoOutTime;
	}

	public String getTodoTitle() {
		return todoTitle;
	}

	public void setTodoTitle(String todoTitle) {
		this.todoTitle = todoTitle;
	}

	public String getTodoContent() {
		return todoContent;
	}

	public void setTodoContent(String todoContent) {
		this.todoContent = todoContent;
	}

	public String getTodoTime() {
		return todoTime;
	}

	public void setTodoTime(String todoTime) {
		this.todoTime = todoTime;
	}

	public String getTodoOutTime() {
		return todoOutTime;
	}

	public void setTodoOutTime(String todoOutTime) {
		this.todoOutTime = todoOutTime;
	}
}
