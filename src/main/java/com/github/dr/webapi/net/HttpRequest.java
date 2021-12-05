/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.net;

import com.github.dr.webapi.util.log.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import static com.github.dr.webapi.util.IsUtil.isBlank;
//GA-Exted

public class HttpRequest {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
	private static final String USER_TESTS = "Mozilla/5.0 (JAVA 11; x64) HI JAVA TO WEB";
	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static String doGet(String url) {
		return doGet(url,null);
	}

	public static String doGet(String url,String tok) {
	HttpURLConnection con = null;
	BufferedReader in = null;
	StringBuffer result = new StringBuffer();
	String line = null;
	try {
		URL conn = new URL(url);
		con = (HttpURLConnection) conn.openConnection();
		con.setConnectTimeout(3000);
		con.setReadTimeout(3000);
		con.setRequestMethod("GET");
		con.addRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Encoding", "gzip,deflate");
		if (tok != null) {
			con.setRequestProperty("Tonken-ASD", tok);
		}
		int responseCode = con.getResponseCode();
		String contentEncoding = con.getContentEncoding();
		if (null != contentEncoding && contentEncoding.indexOf("gzip") != -1) {
			GZIPInputStream gZIPInputStream = new GZIPInputStream(con.getInputStream());
			in = new BufferedReader(new InputStreamReader(gZIPInputStream));
			while ((line = in.readLine()) != null) {
				result.append(new String(line.getBytes("ISO-8859-1"),UTF_8));
			}
		} else {
			in = new BufferedReader(new InputStreamReader(con.getInputStream(),UTF_8));
			while ((line = in.readLine()) != null) {
				result.append("\n"+line);
			}
		}
	} catch (IOException e) {
		Log.error("doGet!",e);
	} finally{
		if(in != null) {
			try {
				in.close();
			} catch (IOException e) {
				in = null;
			}
		}
		if(con != null) {
			con.disconnect();
		}
	}
	return result.toString();
	}

	public static String doPost(String url, String param) {
		return doPost(url,param,null);
	}

	public static String doPost(String url, String param, String tok) {
		StringBuilder result = new StringBuilder();
		PrintWriter out = null;
		BufferedReader in = null;
		String line = null;
		try{
			URL realUrl = new URL(url);
			URLConnection conn =  realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.addRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",USER_AGENT);
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			if (tok != null) {
				conn.setRequestProperty("Tonken-ASD", tok);
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			String contentEncoding = conn.getContentEncoding(); 
			if (null != contentEncoding && contentEncoding.indexOf("gzip") != -1) {
				GZIPInputStream gZIPInputStream = new GZIPInputStream(conn.getInputStream());
				in = new BufferedReader(new InputStreamReader(gZIPInputStream));
	            while ((line = in.readLine()) != null) {
                    result.append(new String(line.getBytes("ISO-8859-1"),UTF_8));
                }
			} else {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(),UTF_8));
				while ((line = in.readLine()) != null) {
                    result.append("\n"+line);
                }
			}
		} catch (IOException e) {
			Log.error("doPost!",e);
		} finally{
			if(out != null) {
                out.close();
            }
			if(in != null) {
                try{
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }
		}
		return result.toString();
	}

	public static void Url302(String url,String file) {
		HttpURLConnection conn = null;
		try {
			URL serverUrl = new URL(url);
			conn = (HttpURLConnection) serverUrl.openConnection();
			conn.setRequestMethod("GET");
			// 必须设置false，否则会自动redirect到Location的地址
			conn.setInstanceFollowRedirects(false);
			conn.addRequestProperty("Accept-Charset", "UTF-8;");
			conn.addRequestProperty("User-Agent",USER_AGENT);
			conn.connect();
			String location = conn.getHeaderField("Location");
			Log.debug("URL-302",location);
			//downUrl(location,file);
		} catch (IOException e) {
			Log.error("Url302",e);
		} finally{
			if(conn != null) {
                conn.disconnect();
            }
		}
	}

	public static boolean downUrl(final String url,final File file) {
		try{
			File filepath=file.getParentFile();
			if(!filepath.exists()) {
				filepath.mkdirs();
			}
			URL httpUrl=new URL(url);
			HttpURLConnection conn;
			while (true) {
				conn = (HttpURLConnection) httpUrl.openConnection();
				conn.setRequestProperty("User-Agent",USER_AGENT);
				if (conn.getResponseCode() == 301 || conn.getResponseCode() == 302) {
					final String newUrl = conn.getHeaderField("Location");
					if (isBlank(newUrl)) {
						Log.error("Download Fail: Empty Redirect");
						return false;
					}
					httpUrl = new URL(newUrl);
				} else {
					break;
				}
			}
			try (BufferedInputStream bis = new BufferedInputStream(conn.getInputStream())) {
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
					byte[] buf = new byte[4096];
					int length = bis.read(buf);
					while (length != -1) {
						bos.write(buf, 0, length);
						length = bis.read(buf);
					}
				}
			}
			return true;
		} catch (Exception e) {
			Log.error("downUrl",e);
		}
		return false;
	}

}