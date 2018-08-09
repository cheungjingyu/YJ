package com.zsz.tools;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PagerTag extends SimpleTagSupport {
	private long totalCount;//总数据条数
	private int pageSize;//一页的数据条数
	private long currentPageNum;//当前的页面
	private String urlFormat;//url的格式，页码用[pageNum]来代替 
	
	
public long getTotalCount() {
		return totalCount;
	}


	public String getUrlFormat() {
	return urlFormat;
}


public void setUrlFormat(String urlFormat) {
	this.urlFormat = urlFormat;
}


	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public long getCurrentPageNum() {
		return currentPageNum;
	}


	public void setCurrentPageNum(long currentPageNum) {
		this.currentPageNum = currentPageNum;
	}
 private String getUrl(long pageNum){
	 return urlFormat.replace("{pageNum}", String.valueOf(pageNum));
 }

@Override
public void doTag() throws JspException, IOException {
	JspWriter out=this.getJspContext().getOut();
	out.print("<a href='");
	 out.print(getUrl(1));
	 out.print("'>");
	 out.print("首页");
	 out.println("</a>");
	//当前显示范围内的第一个页码数
	long firstPageNum = Math.max(currentPageNum-5,1);
	//总页数
	long totalPage=(long)Math.ceil(totalCount*1.0f/pageSize);
	//当前显示范围内的最后一个页码数
	long lastPageNum=Math.min(totalPage, currentPageNum+4);
	
	//输出当前页之前的页码
	 for(long i=firstPageNum;i<currentPageNum;i++){
		 out.print("<a href='");
		 out.print(getUrl(i));
		 out.print("'>");
		 out.print(i);
		 out.println("</a>");
	 }
	 //输出当前的页码
	 out.print("<span>");
	 out.print(currentPageNum);
	 out.println("</span>");
	 //输出当前页之后的页码
	 for(long i=currentPageNum+1;i<=lastPageNum;i++){
		 out.print("<a href='");
		 out.print(getUrl(i));
		 out.print("'>");
		 out.print(i);
		 out.println("</a>");
	 }
	 //输出末页
	 out.print("<a href='");
	 out.print(getUrl(lastPageNum));
	 out.print("'>");
	 out.print("末页");
	 out.println("</a>");
}
}
