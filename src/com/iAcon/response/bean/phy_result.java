package com.iAcon.response.bean;

/**
 * 生理信息
 * @author jorry_liu
 *
 */
public class phy_result {
	private int phy_time;
	public int getPhy_time() {
		return phy_time;
	}
	public void setPhy_time(int phy_time) {
		this.phy_time = phy_time;
	}
	public int getWard_id() {
		return ward_id;
	}
	public void setWard_id(int ward_id) {
		this.ward_id = ward_id;
	}
	public int getStep_num() {
		return step_num;
	}
	public void setStep_num(int step_num) {
		this.step_num = step_num;
	}
	public int getHeart_rate() {
		return heart_rate;
	}
	public void setHeart_rate(int heart_rate) {
		this.heart_rate = heart_rate;
	}
	public int getBlood_oxygen() {
		return blood_oxygen;
	}
	public void setBlood_oxygen(int blood_oxygen) {
		this.blood_oxygen = blood_oxygen;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	public int getSlump_rate() {
		return slump_rate;
	}
	public void setSlump_rate(int slump_rate) {
		this.slump_rate = slump_rate;
	}
	public double getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(double sleep_time) {
		this.sleep_time = sleep_time;
	}
	public int getWatch_take_state() {
		return watch_take_state;
	}
	public void setWatch_take_state(int watch_take_state) {
		this.watch_take_state = watch_take_state;
	}
	public int getPhy_get_type() {
		return phy_get_type;
	}
	public void setPhy_get_type(int phy_get_type) {
		this.phy_get_type = phy_get_type;
	}
	public int getHealthy_state() {
		return healthy_state;
	}
	public void setHealthy_state(int healthy_state) {
		this.healthy_state = healthy_state;
	}
	private int ward_id;
	private int step_num;
	private int heart_rate;
	private int blood_oxygen;
	private int calorie;
	private int slump_rate;
	private double sleep_time;
	private int watch_take_state;
	private int phy_get_type;
	private int healthy_state;
	private	center myCenter;
	public center getMyCenter() {
		if(myCenter==null){
			myCenter = new center();
		}
		return myCenter;
	}
	public void setMyCenter(center myCenter) {
		this.myCenter = myCenter;
	}
	public class center {
		public String ew;
		public String ns;
		public String longS;
		public String latS;
	}

	
	/**
	 * "phy_result": {
        "phy_time": 4567896,
        "ward_id": 1,
        "step_num": 0,
        "heart_rate": 80,
        "blood_oxygen": 80,
        "calorie": 456,
        "phy_get_type": 1,
        "slump_rate": 50,
        "sleep_time": "5.00",
        "ward_loc": {
            "ew": "E",
            "ns": "N",
            "long": "2310.1210",
            "lat": "11327.4938"
        },
        "watch_take_state": 1,
        "healthy_state": 1
    }
	 */
}
