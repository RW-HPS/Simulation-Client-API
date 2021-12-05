/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.log.exp;

public class RwGamaException {
	public static class KickException extends Exception {
		public KickException(String type) {
	        super(com.github.dr.webapi.util.log.ErrorCode.valueOf(type).getError());
	    }
	}    

	public static class PasswdException extends Exception {
		public PasswdException(String type) {
	        super(com.github.dr.webapi.util.log.ErrorCode.valueOf(type).getError());
	    }
	}

	public static class KickStartException extends KickException {
		public KickStartException(String type) {
	        super(type);
	    }
	}

	public static class KickPullException extends KickException {
		public KickPullException(String type) {
	        super(type);
	    }
	}
}