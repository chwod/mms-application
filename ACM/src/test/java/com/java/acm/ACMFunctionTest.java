package com.java.acm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ACMFunctionTest {
	
	private ACMFunction acmf;
	
	@Before
	public void setup() {
		this.acmf = new ACMFunction();
	}
	
	@Test
	public void testForSuccess() throws Exception{
		
		List<Integer> result = acmf.parseString("123456789");

		Set<Integer> set = new HashSet<Integer>();
		for(Integer n : result) {
			set.add(n);
		}
		
		assertEquals(result.size(), set.size());
		
		StringBuilder newStr = new StringBuilder("");
		for(Integer n : result) {
			newStr.append(n);
		}
		assertEquals("44445",newStr.toString());
		
	}
	
	@Test
	public void testFor4SomeDigitalsAndMore4445() throws Exception{
		
		List<Integer> result = acmf.parseString("44445");

		Set<Integer> set = new HashSet<Integer>();
		for(Integer n : result) {
			set.add(n);
		}
		
		assertEquals(result.size(), set.size());
		
		StringBuilder newStr = new StringBuilder("");
		for(Integer n : result) {
			newStr.append(n);
		}
		assertEquals("44445",newStr.toString());
		
	}
	
	@Test
	public void testFor4SomeDigitalsAndMore144446() throws Exception{
		
		List<Integer> result = acmf.parseString("144446");

		Set<Integer> set = new HashSet<Integer>();
		for(Integer n : result) {
			set.add(n);
		}
		
		assertEquals(result.size(), set.size());
		
		StringBuilder newStr = new StringBuilder("");
		for(Integer n : result) {
			newStr.append(n);
		}
		assertEquals("144446",newStr.toString());
		
	}
	
	@Test(expected = Exception.class)
	public void testFor4SomeDigitals9999() throws Exception{
		
		acmf.parseString("9999");
		
		fail();
	}
	
	@Test(expected = Exception.class)
	public void testFor4SomeDigitals1111() throws Exception{
		
		acmf.parseString("1111");
		
		fail();
	}
	
	@Test(expected = Exception.class)
	public void testForSomeMoreDigitals() throws Exception{
		
		acmf.parseString("999999999");
		
		fail();
	}
	
	@Test(expected = Exception.class)
	@Ignore
	public void testStringA() throws Exception {
		
		acmf.parseString("a");
		
		fail();
	}
	
	@Test(expected = Exception.class)
	public void testStringABC() throws Exception {
		
		acmf.parseString("abc");
		
		fail();
	}
	
	@Test(expected = Exception.class)
	public void testWith00() throws Exception {
		
		acmf.parseString("1001");
		
		fail();
	}
	
	@Test(expected = Exception.class)
	public void testWithSpace() throws Exception {
		
		acmf.parseString("");
		
		fail();
	}
	
//	@Test(expected = Exception.class)
//	public void testWithString() throws Exception {
//		
//		acmf.parseString("+ - . a b c d e f l A B C D E F L x X");
//		
//		fail();
//	}
	
	@Test(expected = Exception.class)
	public void testNullValue() throws Exception {
		
		acmf.parseString(null);
		
		fail();
	}

}
