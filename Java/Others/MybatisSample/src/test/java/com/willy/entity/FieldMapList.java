package com.willy.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * FieldMapList
 * @author 
 */
public class FieldMapList implements Serializable {
    public int getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(int fieldKey) {
		this.fieldKey = fieldKey;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getFieldKind() {
		return fieldKind;
	}

	public void setFieldKind(String fieldKind) {
		this.fieldKind = fieldKind;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	private String fieldValue;

    private String fieldDesc;

    private String lastModifyUser;

    private Date lastModifyDate;
    private String fieldKind;
    private int fieldKey;

    private static long serialVersionUID = 1L;

}