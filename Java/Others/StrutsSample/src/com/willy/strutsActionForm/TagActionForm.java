package com.willy.strutsActionForm;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;



public class TagActionForm extends ActionForm{
private String[] checkbox;
private String file;
private String hidden;
private String[] multiboxlist;
private String password;
private String radio;
private String select;
private String select1;
private String text;
private String textarea;
private String people;



public String[] getMultiboxlist() {
	return multiboxlist;
}
public void setMultiboxlist(String[] multiboxlist) {
	this.multiboxlist = multiboxlist;
}
public String getPeople() {
	return people;
}
public void setPeople(String people) {
	this.people = people;
}
public String[] getCheckbox() {
	return checkbox;
}
public void setCheckbox(String[] checkbox) {
	this.checkbox = checkbox;
}


public String getFile() {
	return file;
}
public void setFile(String file) {
	this.file = file;
}
public String getHidden() {
	return hidden;
}
public void setHidden(String hidden) {
	this.hidden = hidden;
}

public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getRadio() {
	return radio;
}
public void setRadio(String radio) {
	this.radio = radio;
}
public String getSelect() {
	return select;
}
public void setSelect(String select) {
	this.select = select;
}
public String getSelect1() {
	return select1;
}
public void setSelect1(String select1) {
	this.select1 = select1;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getTextarea() {
	return textarea;
}
public void setTextarea(String textarea) {
	this.textarea = textarea;
}
@Override
public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
	// TODO Auto-generated method stub
	return super.validate(mapping, request);
}

}
