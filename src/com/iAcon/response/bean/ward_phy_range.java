package com.iAcon.response.bean;

/**
 * 运动计划，生理信息
 * @author jorry_liu
 *
 */
public class ward_phy_range {

	/**
	 * "ward_id": 1, "plan_step_num": 998, "sleep_time_long": 15.5,
	 * "heart_rate_low": 72, "heart_rate_high": 110, "blood_oxygen_low": 77.5,
	 * "blood_oxygen_high": 99.5
	 */
	private int ward_id;

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

	public double getSleep_time_long() {
		return sleep_time_long;
	}

	public void setSleep_time_long(double sleep_time_long) {
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

	public double getBlood_oxygen_low() {
		return blood_oxygen_low;
	}

	public void setBlood_oxygen_low(double blood_oxygen_low) {
		this.blood_oxygen_low = blood_oxygen_low;
	}

	public double getBlood_oxygen_high() {
		return blood_oxygen_high;
	}

	public void setBlood_oxygen_high(double blood_oxygen_high) {
		this.blood_oxygen_high = blood_oxygen_high;
	}

	private int plan_step_num;
	private double sleep_time_long;
	private int heart_rate_low;
	private int heart_rate_high;
	private double blood_oxygen_low;
	private double blood_oxygen_high;
}
