package com.zsz.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.dao.HouseDAO;
import com.zsz.dto.HouseDTO;

public class House {

	@Test
	public void testGet() {
		HouseDAO dao=new HouseDAO();
		HouseDTO dto=new HouseDTO(); 
	System.out.println(dao.getById(1).getAddress());
		//System.out.println(dao.getTotalCount(3, 14));
		//dao.markDeleted(1);
	}

}
