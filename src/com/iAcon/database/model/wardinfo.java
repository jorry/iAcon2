package com.iAcon.database.model;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table WARDINFO.
 */
public class wardinfo implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ward_name;
    private Integer ward_id;
    private String other;

    public wardinfo() {
    }

    public wardinfo(String ward_name, Integer ward_id, String other) {
        this.ward_name = ward_name;
        this.ward_id = ward_id;
        this.other = other;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public Integer getWard_id() {
        return ward_id;
    }

    public void setWard_id(Integer ward_id) {
        this.ward_id = ward_id;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

}
