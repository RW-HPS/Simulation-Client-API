/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.encryption;

/**
 * @author Dr.
 * @Data 2020/6/25 9:28
 */
public class Game {
	public static String connectak(int paramInt) {
		StringBuffer ak = new StringBuffer(16);
		ak.append("c:" + paramInt)
			  .append("m:" + (paramInt * 87 + 24))
			  .append("0:" + (44000 * paramInt))
			  .append("1:" + paramInt)
			  .append("2:" + (13000 * paramInt))
			  .append("3:" + (28000 + paramInt))
			  .append("4:" + (75000 * paramInt))
			  .append("5:" + (160000 + paramInt))
			  .append("6:" + (850000 * paramInt))
			  .append("t1:" + (44000 * paramInt))
			  .append("d:" + (5 * paramInt));
	    return ak.toString();
  	}
}