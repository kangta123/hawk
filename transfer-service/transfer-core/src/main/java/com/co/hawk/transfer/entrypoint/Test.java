package com.co.hawk.transfer.entrypoint;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url  ="/aaa/bbb/ccc//ddd";
		String replaceStr = url.replaceAll("http://", "").replaceAll("https://", "");
		System.out.println(replaceStr);
		int pos = replaceStr.indexOf("/");
		System.out.println(replaceStr.substring(pos));
	}

}
