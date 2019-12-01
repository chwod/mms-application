package com.java.acm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class T2 {

	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true) {
			System.out.println("please input:");

			String str = reader.readLine();
			if(str == null || str.trim().length() <= 0) {
				continue;
			}
			
			if(!str.trim().matches("\\d+")) {
				continue;
			}

			List<List<Integer>> result = new ArrayList<List<Integer>>();

			T2 t = new T2();
			result = t.process(str.trim(), null);

			if (result == null || result.size() <= 0) {
				System.out.println("NO");
				continue;
			}

			for (List<Integer> i : result) {
				System.out.println(Arrays.toString(i.toArray()));
			}
		}

	}

	private List<List<Integer>> process(String str, List<Integer> result) {
		
		if(result == null) {
			result = new ArrayList<Integer>();
		}
		
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		
		Integer v = Integer.valueOf(str.substring(0, 1));
		
		if((v != 0) && (!result.contains(v))) {
			List<Integer> tempResult = new ArrayList<Integer>();
			tempResult.addAll(result);
			tempResult.add(v);
			if(str.length() <= 1) {
				list.add(tempResult);
			}else {
				list.addAll(this.process(str.substring(1), tempResult));
			}
		}
		
		if(str.length() >= 2) {
			Integer v2 = Integer.valueOf(str.substring(0,2));
			if((v2 != 0) && (!result.contains(v2))) {
				List<Integer> tempResult = new ArrayList<Integer>();
				tempResult.addAll(result);
				tempResult.add(v2);
				if(str.length() <= 2) {
					list.add(tempResult);
				} else {
					list.addAll(this.process(str.substring(2), tempResult));
				}
			}
		}
		
		return list;
	}
	
}
