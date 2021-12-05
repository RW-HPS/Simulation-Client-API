/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.file;

import com.github.dr.webapi.data.global.Data;
import com.github.dr.webapi.util.log.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;

//Java
//GA-Exted

/*
 *  Config.java
 *	Initialization.java
 */
public class LoadConfig {

	public static String customLoad(String input) {
		return customLoad(input,null);
	}

	public static String customLoad(String input,Object[] params) {
		Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(LoadConfig.class.getResourceAsStream("/language.properties"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
		try {
			return new MessageFormat(properties.get(input).toString()).format(params);
		//防止使读取无效 CALL..
		} catch (MissingResourceException e) {
			//Log.error("NO KEY- Please check the file",e);
		}
		return null;
	}
}