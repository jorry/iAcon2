package com.iAcron.data;

import java.util.ArrayList;

public class weilan2 {

	/**
	 * ward_id	Int	被监护人ID。必须
fence_type	Int	电子围栏类型。1：圆形，使用center和radius两个字段。2：多边形，使用points数组形成的多边形，至少3个点。
error_range	Double	围栏误差范围，离开范围外多少公里认为在范围之内
center	String	圆心
radius	Double	半径（公里）
points	String[]	多边形点的经纬度数组，至少三个

	 */
	
	private int ward_id;
	public int getWard_id() {
		return ward_id;
	}

	public void setWard_id(int ward_id) {
		this.ward_id = ward_id;
	}

	public int getFence_type() {
		return fence_type;
	}

	public void setFence_type(int fence_type) {
		this.fence_type = fence_type;
	}

	public double getError_range() {
		return error_range;
	}

	public void setError_range(double error_range) {
		this.error_range = error_range;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public ArrayList<LanLon> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<LanLon> points) {
		this.points = points;
	}

	private int fence_type;
	private double error_range;
	private String radius;
	private ArrayList<LanLon> points =new ArrayList<LanLon>();
	
	@Override
	public String toString() {
		return "{" + 
                "ward_id='" + ward_id + '\'' + 
                ", fence_type=" + fence_type + 
                ", error_range=" + error_range + 
                ", radius=" + radius + 
                ", points=" + points + 
                '}'; 
	}
	
}
