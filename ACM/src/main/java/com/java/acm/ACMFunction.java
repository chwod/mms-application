package com.java.acm;

import java.util.ArrayList;
import java.util.List;

public class ACMFunction {
	
	public List<Integer> parseString(String str) throws Exception{
		
		if(str == null || str.length() == 0) {
			throw new Exception();
		}
		
		if(!str.matches("\\d+")) {
			throw new Exception();
		}
		
		if(str.length() >= 4) {
			boolean flag = true;
			String s = str.substring(0, 1);
			for(int i=1;i<str.length();i++) {
				if(s.equals(str.substring(i,i+1))) {
					continue;
				}
				flag = false;
			}
			if(flag == true) {
				throw new Exception();
			}
		}
		
		throw new Exception();
		
		
//		return this.process(str, new ArrayList<Integer>());
	}
	
	private List<Integer> process(String str, List<Integer> result) {
		if (str == null || str.length() <= 0) {
			return null;
		}

		List<Integer> tempResult = new ArrayList<Integer>();
		tempResult.addAll(result);

		Integer v = Integer.valueOf(str.substring(0, 1));

//		if (v == 0) {
//			return null;
//		}

		if (tempResult.contains(v)) {
			return this.process2(str, tempResult);
		}

		tempResult.add(v);

		if (str.length() <= 1) {
			return tempResult;
		}
		tempResult = this.process(str.substring(1), tempResult);

		if (tempResult == null) {
			return this.process2(str, result);
		}

		return tempResult;
	}

	private List<Integer> process2(String str, List<Integer> result) {
		if (str == null || str.length() < 2) {
			return null;
		}
		
		List<Integer> tempResult = new ArrayList<Integer>();
		tempResult.addAll(result);
		Integer v = Integer.valueOf(str.substring(0, 2));
		if (tempResult.contains(v)) {
			return null;
		}
		tempResult.add(v);
		if (str.length() <= 2) {
			return tempResult;
		}
		tempResult = this.process(str.substring(2), tempResult);
		return tempResult;
	}
	
}
