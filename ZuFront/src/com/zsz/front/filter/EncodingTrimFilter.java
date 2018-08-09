package com.zsz.front.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.HttpServletRequest;

import com.zsz.tools.TrimHttpRequest;
@WebFilter("/*")
public class EncodingTrimFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		TrimHttpRequest trimReq=new TrimHttpRequest((HttpServletRequest)request);
		chain.doFilter(trimReq,response);
	}

	@Override
	public void destroy() {
		
	}

	
}
