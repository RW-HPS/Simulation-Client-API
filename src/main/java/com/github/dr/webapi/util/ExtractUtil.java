/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util;

import com.github.dr.webapi.data.global.Data;
import com.github.dr.webapi.util.log.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dr
 */
public class ExtractUtil {

	final static Pattern PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

	public static boolean inttoBoolean(int in) {
		return (in == 1);
	}


    public static int booleantoInt(boolean bl) {
		if(bl) {
            return 1;
        }
		return 0;
	}

	public static long ipToLong(String strIp) {
		String[]ip = strIp.split("\\.");
		return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
	}


    public static String longToIp(long longIp) {
		StringBuffer sb = new StringBuffer("");
		sb.append(String.valueOf((longIp >>> 24)))
		.append(".")
		.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16))
		.append(".")
		.append(String.valueOf((longIp & 0x0000FFFF) >>> 8))
		.append(".")
		.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	public static String unicodeEncode(String string) {
        char[] utfBytes = string.toCharArray();
        StringBuilder unicodeBytes = new StringBuilder();
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
			unicodeBytes.append("\\u").append(hexB);
        }
        return unicodeBytes.toString();
    }

    public static String unicodeDecode(String string) {
		String str = string;
		Matcher matcher = PATTERN.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    public static String languageDetermination(String string) {
		switch(string){
			case "China" :return "zh_CN";
			case "Hong Kong" :return "zh_HK";
			case "Macao" :return "zh_MO";
			case "Taiwan" :return "zh_TW";
			case "Russia" :return "ru_RU";
			default :return "en_US";
			//I didn't find a better way....
		}
	}

    public static String stringToUtf8(String string) {
		try {
			// 用指定编码转换String为byte[]:
			return new String(string.getBytes("ISO-8859-1"),Data.UTF_8);
		} catch (UnsupportedEncodingException e) {
			Log.error("UTF-8",e);
			return new String(string.getBytes(),Data.UTF_8);
		}
    }

	/**使用TreeSet实现List去重(有序)
	 *
	 * @param list
	 * */
	public static List<String> removeDuplicationByTreeSet(List<String> list) {
		TreeSet<String> set = new TreeSet<String>(list);
		//把List集合所有元素清空
		list.clear();
		//把HashSet对象添加至List集合
		list.addAll(set);
		return list;
	}

}