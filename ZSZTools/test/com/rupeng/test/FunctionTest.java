package com.rupeng.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.tools.Functions;

public class FunctionTest {

	@Test
	public void test() {
		String s1=Functions.addQueryStringPart("action=search&name=4&value=3", "value", "6");
		System.out.println(s1);
		assertEquals(s1, "action=search&name=4&value=6");
		String s2=Functions.addQueryStringPart("action=search&name=4&value=3", "valu", "6");
		System.out.println(s2);
		assertEquals(s2, "action=search&name=4&value=3&valu=6");
	}

}
