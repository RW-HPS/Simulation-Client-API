/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */
/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi;

import com.github.dr.webapi.dependent.LibraryManager;
import com.github.dr.webapi.net.server.Start;
import com.github.dr.webapi.util.log.Log;

import java.math.BigInteger;

/**
 * @author Dr
 */
public class Main {

	static String bbb(byte[] paramArrayOfbyte) {
		return String.format("%0" + (paramArrayOfbyte.length * 2) + "X", new Object[] { new BigInteger(1, paramArrayOfbyte) });
	}
	public static void main(String[] args) {
		
		Log.set("ALL");

		LibraryManager lib = new LibraryManager("libs");

		lib.importLib("com.alibaba","fastjson","1.2.58");
		lib.importLib("org.eclipse.jetty","jetty-server","9.4.28.v20200408");
		lib.importLib("org.eclipse.jetty","jetty-servlet","9.4.28.v20200408");
		lib.importLib("org.eclipse.jetty","jetty-http","9.4.28.v20200408");
		lib.importLib("org.eclipse.jetty","jetty-security","9.4.28.v20200408");
		lib.importLib("org.eclipse.jetty","jetty-io","9.4.28.v20200408");
		lib.importLib("org.eclipse.jetty","jetty-util","9.4.28.v20200408");
		lib.importLib("com.google.code.gson","gson","2.8.7");
		lib.loadToClassLoader();
		lib.removeOldLib();

		new Start();
	}
}
