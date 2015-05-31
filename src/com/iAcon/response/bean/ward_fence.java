package com.iAcon.response.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 电子围栏
 * 
 * @author jorry_liu
 * 
 */
public class ward_fence  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public class center {
		public String longS;
		public String latS;
	}

	public class points {
		public String ew;
		public String ns;
		public String longS;
		public String latS;
	}

	List<points> pointsList;

	public List<points> getPointsList() {
		if (pointsList == null) {
			pointsList = new ArrayList<ward_fence.points>();
		}
		return pointsList;
	}

	public void setPointsList(List<points> pointsList) {
		this.pointsList = pointsList;
	}

	private int ward_id;

	public int getWard_id() {
		return ward_id;
	}

	public void setWard_id(int ward_id) {
		this.ward_id = ward_id;
	}

	public String getSet_time() {
		return set_time;
	}

	public void setSet_time(String set_time) {
		this.set_time = set_time;
	}

	public int getFence_type() {
		return fence_type;
	}

	public void setFence_type(int fence_type) {
		this.fence_type = fence_type;
	}

	public int getError_range() {
		return error_range;
	}

	public void setError_range(int error_range) {
		this.error_range = error_range;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public center getCenterPoint() {
		if (centerPoint == null) {
			centerPoint = new center();
		}
		return centerPoint;
	}

	public void setCenterPoint(center centerPoint) {
		this.centerPoint = centerPoint;
	}

	public points getPoint() {
			point = new points();
		return point;
	}

	public void setPoint(points point) {
		this.point = point;
	}

	private String set_time;
	private int fence_type;
	private int error_range;
	private double radius;
	center centerPoint;
	points point;

	/**
	 * "ward_fence": { "ward_id": 1, "set_time": "2014-08-30", "fence_type": 2,
	 * "error_range": 3, "center": { "ew": "E", "ns": "N", "long": "2310.1210",
	 * "lat": "11327.4938" }, "radius": 4, "points": [ { "ew": "E", "ns": "N",
	 * "long": "2310.1210", "lat": "11327.4938" }, { "ew": "E", "ns": "N",
	 * "long": "2310.1210", "lat": "11327.4939" }, { "ew": "E", "ns": "N",
	 * "long": "2310.1210", "lat": "11327.4938" } ] }
	 */
}
