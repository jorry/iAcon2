package com.iAcon.response.bean;

public class HistoryBean {

	private String phy_date;
	public String getPhy_date() {
		return phy_date;
	}
	public void setPhy_date(String phy_date) {
		this.phy_date = phy_date;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private String value;
	
	private boolean isShowYline;
	public boolean isShowYline() {
		return isShowYline;
	}
	public void setShowYline(boolean isShowYline) {
		this.isShowYline = isShowYline;
	}
}
