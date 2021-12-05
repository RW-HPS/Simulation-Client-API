/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha {


    public String sha256(final String strText) {
		return toSha(strText, "SHA-256");
	}


    public String sha512(final String strText) {
		return toSha(strText, "SHA-512");
	}

	public byte[] sha256Arry(final String strText) {
		return toShaArry(strText, "SHA-256");
	}


    public byte[] sha512Arry(final String strText) {
		return toShaArry(strText, "SHA-512");
	}

	private byte[] toShaArry(final String strText, final String strType) {
		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			try {
				MessageDigest messageDigest = MessageDigest.getInstance(strType);
				messageDigest.update(strText.getBytes());
				return messageDigest.digest();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


    private String toSha(final String strText, final String strType) {
		// 返回值
		String strResult = null;

		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			byte[] byteBuffer = toShaArry(strText,strType);
			StringBuffer strHexString = new StringBuffer();
			for (int i = 0; i < byteBuffer.length; i++) {
				String hex = Integer.toHexString(0xff & byteBuffer[i]);
				if (hex.length() == 1) {
                    strHexString.append('0');
                }
				strHexString.append(hex);
				strResult = strHexString.toString();
			}
		}
		return strResult;
	}
}