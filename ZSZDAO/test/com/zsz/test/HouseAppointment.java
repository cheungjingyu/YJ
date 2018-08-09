package com.zsz.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.zsz.dao.HouseAppointmentDAO;

public class HouseAppointment {

	@Test
	public void test() {
		HouseAppointmentDAO houseAppointment=new HouseAppointmentDAO();
	//long hId=	houseAppointment.addnew(null, "嗯", "11111111111", 5, new Date());
	houseAppointment.follow(9, 9);
	}

}
