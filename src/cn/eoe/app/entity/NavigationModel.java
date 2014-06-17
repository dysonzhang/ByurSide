package cn.eoe.app.entity;

public class NavigationModel {
	private String name;
	private String tags;

	public NavigationModel(String paramString1, String paramString2) {
		this.name = paramString1;
		this.tags = paramString2;
	}

	public String getName() {
		return this.name;
	}

	public String getTags() {
		return this.tags;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setTags(String paramString) {
		this.tags = paramString;
	}
}