package com.iAcron.data;

public class ShengliXinxi {

	/**
	 * ward_id	Int	被监护人ID。必须
plan_step_num	Int	计划每天总步数
sleep_time_long	String	计划每天睡觉时间（小时）
heart_rate_low	Int	心率最低值
heart_rate_high	Int	心率最高值
blood_oxygen_low	String	血氧最低百分比
blood_oxygen_high	String	血氧最高百分比
	 */
	
	@Override
	public String toString() {
		return "{" + 
                "ward_id='" + ward_id + '\'' + 
                ", plan_step_num=" + plan_step_num + 
                ", sleep_time_long=" + sleep_time_long + 
                ", heart_rate_low=" + heart_rate_low + 
                ", heart_rate_high=" + heart_rate_high + 
                ", blood_oxygen_low=" + blood_oxygen_low + 
                ", slump_level=" + slump_level + 
                ", blood_oxygen_high=" + blood_oxygen_high + 
                '}'; 
	}
	
	private int ward_id ;
	public int getWard_id() {
		return ward_id;
	}
	public void setWard_id(int ward_id) {
		this.ward_id = ward_id;
	}
	public int getPlan_step_num() {
		return plan_step_num;
	}
	public void setPlan_step_num(int plan_step_num) {
		this.plan_step_num = plan_step_num;
	}
	public String getSleep_time_long() {
		return sleep_time_long;
	}
	public void setSleep_time_long(String sleep_time_long) {
		this.sleep_time_long = sleep_time_long;
	}
	public int getHeart_rate_low() {
		return heart_rate_low;
	}
	public void setHeart_rate_low(int heart_rate_low) {
		this.heart_rate_low = heart_rate_low;
	}
	public int getHeart_rate_high() {
		return heart_rate_high;
	}
	public void setHeart_rate_high(int heart_rate_high) {
		this.heart_rate_high = heart_rate_high;
	}
	public String getBlood_oxygen_low() {
		return blood_oxygen_low;
	}
	public void setBlood_oxygen_low(String blood_oxygen_low) {
		this.blood_oxygen_low = blood_oxygen_low;
		System.out.println("boold_ox"+this.blood_oxygen_low);
	}
	public String getBlood_oxygen_high() {
		return blood_oxygen_high;
	}
	public void setBlood_oxygen_high(String blood_oxygen_high) {
		this.blood_oxygen_high = blood_oxygen_high;
	}
	private int plan_step_num;
	private String sleep_time_long;
	private int heart_rate_low;
	private int heart_rate_high;
	
	private int slump_level;
	
	public int getSlump_level() {
		return slump_level;
	}
	public void setSlump_level(int slump_level) {
		this.slump_level = slump_level;
	}

	private String blood_oxygen_low;
	private String blood_oxygen_high ;
}
