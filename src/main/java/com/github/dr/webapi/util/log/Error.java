/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.log;

public class Error {

    public static String error(String type) {
		return com.github.dr.webapi.util.log.ErrorCode.valueOf(type).getError();
	}

    public static int code(String type) {
		return com.github.dr.webapi.util.log.ErrorCode.valueOf(type).getCode();
	}
}