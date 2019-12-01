package com.java.acm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class T3 {
	
	private String str;

	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("please input:");
			T3 t3 = new T3();

			String str = reader.readLine();
			if(str == null || str.trim().length() <= 0) {
				continue;
			}
			if(!str.trim().matches("\\d+")) {
				continue;
			}
			t3.setStr(str.trim());

			List<Integer> result = new ArrayList<Integer>();

			result = t3.process(0, result);

			if (result == null || result.size() <= 0) {
				System.out.println("NO");
				continue;
			}

			for (Integer i : result) {
				System.out.print(i);
				System.out.print(" ");
			}
			System.out.println();
		}

	}


	private List<Integer> process(int position, List<Integer> result) {

		List<Integer> tempResult = new ArrayList<Integer>();
		tempResult.addAll(result);

		Integer v = Integer.valueOf(str.substring(position, position + 1));

		if (v == 0) {
			return null;
		}

		if (tempResult.contains(v)) {
			if(str.length() <= position + 2) {
				return null;
			}
			return this.process2(position, tempResult);
		}

		tempResult.add(v);

		if (str.length() <= position + 1) {
			return tempResult;
		}
		tempResult = this.process(position + 1, tempResult);

		if (tempResult == null) {
			if (str.length() <= position + 2) {
				return null;
			}
			return this.process2(position + 1, result);
		}

		return tempResult;
	}

	private List<Integer> process2(int position, List<Integer> result) {
		
		List<Integer> tempResult = new ArrayList<Integer>();
		tempResult.addAll(result);
		Integer v = Integer.valueOf(str.substring(position, position + 2));
		if (tempResult.contains(v)) {
			return null;
		}
		tempResult.add(v);
		if (str.length() <= position + 2) {
			return tempResult;
		}
		tempResult = this.process(position + 2, tempResult);
		return tempResult;
	}
	
	public String getStr() {
		return str;
	}
	
	public void setStr(String str) {
		this.str = str;
	}
}
