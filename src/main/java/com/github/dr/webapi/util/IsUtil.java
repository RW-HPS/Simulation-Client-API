/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util;

import java.util.regex.Pattern;

public class IsUtil {

	private final static Pattern PATTERN = Pattern.compile("[0-9]*");
	

    public static boolean isBlank(Object string) {
		if (string == null || "".equals(string.toString().trim())) {
            return true;
        }
		return false;
	}


    public static boolean notisBlank(Object string) {
		return !isBlank(string);
	}

	public static boolean isNumeric(String string) {
		return PATTERN.matcher(string).matches();
	}


    public static boolean notisNumeric(String string) {
		return !isNumeric(string);
	}
}