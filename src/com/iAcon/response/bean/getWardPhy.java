package com.iAcon.response.bean;

public class getWardPhy {

	/**
phy_time	Long	生理信息采集时间（UNIX时间）
step_num	int	运动步数
heart_rate	Double	心率
blood_oxygen	Double	血氧
calorie	Double	卡路里消耗量（千焦）
phy_get_type	Int	信息采集类型。1:平常信息采集的信息；2:紧急呼叫时采集的信息
slump_rate	Double	瘫倒率
sleep_time	Int	睡眠时间长度(分钟)
ward_loc	String	被监护人的位置。经纬度以json形式表示{"ew":"E","ns":"N","long":"2310.1210","lat":"11327.4938"}
watch_take_state	Int	腕表佩戴状态。1:为佩戴状态，2为未佩戴状态
healthy_state	Int	1:被监护人在电子围栏内，2：被监护人不在围栏内
	 */
	
	
	private int healthy_state;
	

	public int getHealthy_state() {
		return healthy_state;
	}
	public void setHealthy_state(int healthy_state) {
		this.healthy_state = healthy_state;
	}
	private long phy_time;
	public long getPhy_time() {
		return phy_time;
	}
	public void setPhy_time(long phy_time) {
		this.phy_time = phy_time;
	}
	public String getStep_num() {
		return step_num;
	}
	public void setStep_num(String step_num) {
		this.step_num = step_num;
	}
	public double getHeart_rate() {
		return heart_rate;
	}
	public void setHeart_rate(double heart_rate) {
		this.heart_rate = heart_rate;
	}
	public double getBlood_oxygen() {
		return blood_oxygen;
	}
	public void setBlood_oxygen(double blood_oxygen) {
		this.blood_oxygen = blood_oxygen;
	}
	public double getCalorie() {
		return calorie;
	}
	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}
	public int getPhy_get_type() {
		return phy_get_type;
	}
	public void setPhy_get_type(int phy_get_type) {
		this.phy_get_type = phy_get_type;
	}
	public String getSlump_rate() {
		return slump_rate;
	}
	public void setSlump_rate(String slump_rate) {
		this.slump_rate = slump_rate;
	}
	public int getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(int sleep_time) {
		this.sleep_time = sleep_time;
	}
	public String getWard_loc() {
		return ward_loc;
	}
	public void setWard_loc(String ward_loc) {
		this.ward_loc = ward_loc;
	}
	public String getWatch_take_state() {
		return watch_take_state;
	}
	public void setWatch_take_state(String watch_take_state) {
		this.watch_take_state = watch_take_state;
	}
	private String step_num;
	private double heart_rate;
	private double blood_oxygen;
	private double calorie;
	private int phy_get_type;
	private String slump_rate;
	private int sleep_time;
	private String ward_loc;
	private String watch_take_state;
	
	
}
