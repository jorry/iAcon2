package com.iAcron.data;

import java.util.ArrayList;

public class weilan {

	/**
	 * ward_id	Int	被监护人ID。必须
fence_type	Int	电子围栏类型。1：圆形，使用center和radius两个字段。2：多边形，使用points数组形成的多边形，至少3个点。
error_range	String	围栏误差范围，离开范围外多少公里认为在范围之内
center	String	圆心
radius	String	半径（公里）
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

	public String getError_range() {
		return error_range;
	}

	public void setError_range(String error_range) {
		this.error_range = error_range;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getRadius() {
		return radius;
	}

	
	public void setRadius(String radius) {
		this.radius = radius;
	}


	private int fence_type;
	private String error_range;
	private String radius;
	private String center;
	@Override
	public String toString() {
		return "{" + 
                "ward_id='" + ward_id + '\'' + 
                ", fence_type=" + fence_type + 
                ", error_range=" + error_range + 
                ", radius=" + radius + 
                ", center=" + center + 
                '}'; 
	}
	
}
