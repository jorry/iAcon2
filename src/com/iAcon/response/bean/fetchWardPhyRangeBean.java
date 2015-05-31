package com.iAcon.response.bean;

import java.io.Serializable;

public class fetchWardPhyRangeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public float getBlood_oxygen_low() {
		return blood_oxygen_low;
	}

	public void setBlood_oxygen_low(float blood_oxygen_low) {
		this.blood_oxygen_low = blood_oxygen_low;
	}

	public float getBlood_oxygen_height() {
		return blood_oxygen_height;
	}

	public void setBlood_oxygen_height(float blood_oxygen_height) {
		this.blood_oxygen_height = blood_oxygen_height;
	}

	public float getSleep_time_long() {
		return sleep_time_long;
	}

	public void setSleep_time_long(float sleep_time_long) {
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

	private int plan_step_num;
	private float blood_oxygen_low;
	private float blood_oxygen_height;
	private float sleep_time_long;
	private int heart_rate_low;
	private int heart_rate_high;

	private int slump_threshold;

	public int getSlump_threshold() {
		return slump_threshold;
	}

	public void setSlump_threshold(int slump_threshold) {
		this.slump_threshold = slump_threshold;
	}

	int slump_level;

	public int getSlump_level() {
		return slump_level;
	}

	public void setSlump_level(int slump_level) {
		this.slump_level = slump_level;
	}

}
