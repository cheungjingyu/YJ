package com.zsz.tools;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;



public class SelectTag extends SimpleTagSupport {
	private String id;
	private String name;
	private Object items;

	private String textName;
	private String valueName;
	private Object selectedValue;
	


	



public Object getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(Object selectedValue) {
		this.selectedValue = selectedValue;
	}

public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Object getItems() {
		return items;
	}

	public void setItems(Object items) {
		this.items = items;
	}

	

	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

@Override
public void doTag() throws JspException, IOException {
	JspWriter out=getJspContext().getOut();
	out.print("<select id='");
	out.print(id);
	out.print("' name='");
	out.print(name);
	out.println("'>");
	Iterable iterable;
	if(items.getClass().isArray()){
		iterable=CommonUtils.toList(items);
	}else if(items instanceof Iterable){
		iterable=(Iterable)items;
		
	}else{
		throw new IllegalArgumentException("items必须是数组或者实现了Iterable接口");
		
	}
	for(Object item:iterable){
		Object textValue=getPropertyValue(item, textName);
		Object valueValue=getPropertyValue(item, valueName);
		out.print("<option value='");
		out.print(valueValue);	
		out.print("'");
		if(selectedValue!=null&&selectedValue.equals(valueValue)){
			out.print(" selected='selected'");
		}
		out.print(">");
		out.print(textValue);
		out.print("</option>");	
		
	}
	out.print("</select>");
}
/**
 * 获得item对象的propName属性的值Object v=getPropertyNameValue(item,"id");
 * @param item
 * @param propName
 * @return
 */
private Object getPropertyValue(Object item,String propName){
	try {
		BeanInfo beanInfo=Introspector.getBeanInfo(item.getClass());
		PropertyDescriptor[] propDescs=beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor propDesc:propDescs){
			if(propDesc.getName().equals(propName)){//找到了属性
				try {
					Object value=propDesc.getReadMethod().invoke(item);//调用get***方法，getId。获取属性值
					return value;
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
				
			}
		}
		throw new IllegalArgumentException("没有找到"+propName+"属性"); 
	} catch (IntrospectionException e) {
		throw new RuntimeException(e);
	}
}
}
