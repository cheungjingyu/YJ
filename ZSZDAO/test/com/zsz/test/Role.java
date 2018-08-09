package com.zsz.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.dao.RoleDAO;
import com.zsz.dto.RoleDTO;

public class Role {

	@Test
	public void test() {
		RoleDAO dao=new RoleDAO();
		RoleDTO[] roles=dao.getAll();
	}

}
