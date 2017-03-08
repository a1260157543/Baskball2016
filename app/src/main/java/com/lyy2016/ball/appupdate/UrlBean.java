package com.lyy2016.ball.appupdate;

public class UrlBean {
	private String url;// apk的下载路径
	private int versionCode;//新版本号
	private String desc;//描述信息

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	@Override
	public String toString() {
		return "UrlBean [url=" + url + ", versionCode=" + versionCode
				+ ", desc=" + desc + "]";
	}

}