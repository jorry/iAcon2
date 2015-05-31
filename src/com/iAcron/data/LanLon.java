package com.iAcron.data;

public class LanLon {

	// ”lat:5.2,lot:2.5
	private double lat;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLot() {
		return lot;
	}

	public void setLot(double lot) {
		this.lot = lot;
	}

	private double lot;

	@Override
	public String toString() {
		return "{" + "lat='" + lat + '\'' + ", lot=" + lot + '}';
	}
}
