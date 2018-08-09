package com.zsz.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.dao.AdminUserDAO;

public class adminUserDAO {

	@Test
	public void test() {
		AdminUserDAO dao=new AdminUserDAO();
		if(dao.getByPhoneNum("18911275827")!=null){
			System.out.println("ok");
		}else{
			System.err.println("ok");
		}
		
	}
	@Test
	public void test1(){
		AdminUserDAO dao =new AdminUserDAO();
		
			assertTrue(dao.checkLogin("18911275827", "123456"));
		
	}
	@Test
	public void test2(){
		AdminUserDAO dao=new AdminUserDAO();
		Long adminUserId=dao.addAdminUser("ss我","17806096911","123","ww@qq.com",1L);
		System.out.println(adminUserId);
	}

}
