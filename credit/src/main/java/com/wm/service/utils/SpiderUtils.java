package com.wm.service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @note 网络爬虫抓取工具
 * @author wangmeng
 * @date 2016-1-2 下午12:26:33
 */
public class SpiderUtils {

	URLConnection conn = null;
	
	public void spiderData(String urlStr){
		try {
			URL url = new URL(urlStr);
			conn = url.openConnection();
			conn.setDoInput(true);
			conn.connect();
//			conn.setDoOutput(true);
			InputStream in = conn.getInputStream();
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(inReader);
			char[] cArray = new char[100];
			while (bufferReader.read() == -1) {
				bufferReader.read(cArray);
			}
			in.close();
			inReader.close();
			bufferReader.close();
			String ss = new String(cArray);
			
			System.out.println(ss);
//			OutputStream out = conn.getOutputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
		}
	}
	
	@Test
	public void test(){
		spiderData("http://www.iqianjin.com/");
	}
}
