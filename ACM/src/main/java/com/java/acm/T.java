package com.java.acm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class T {

	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("please input:");

			String str = reader.readLine();
			if(str == null || str.trim().length() <= 0) {
				continue;
			}
			
			if(!str.trim().matches("\\d+")) {
				continue;
			}

			List<Integer> result = new ArrayList<Integer>();

			T t = new T();
			result = t.process(str.trim(), result);

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
