/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.log;

/**
 * 错误码
 * @author Dr
 */
public enum ErrorCode {

	/*状态码*/
	/**
	 * 通用码
	 */
	SUCCESS(0,"Success"),
	ERROR(-1,"Error"),

	/*网络层*/
	/**
	 * NETWORK_NOT_CONNECTED : 没有网络
	 * DATA_CORRUPTION : 数据在传输中损坏
	 */
	NETWORK_NOT_CONNECTED(10001,"Network not connected"),
	DATA_CORRUPTION(10002,"Data corruption"),
	
	/*客户端层*/
	/**
	 * PASSWORD_ERROR : 密码错误
	 * USER_DOES_NOT_EXIST : 用户不存在
	 * SERVER_CLOSE : 服务器关闭
	 * AUTO_SELECT_FAILED : 自动选择SQL错误
	 */
	PASSWORD_ERROR(20001,"Passwd error"),
	USER_DOES_NOT_EXIST(20002,"User does not exist"),
	SERVER_CLOSE(20003,"Server close"),
	AUTO_SELECT_FAILED(20004,"Auto select failed"),
	DATA_NULL(20005,"Data null"),
	SERVER_KICK(20006,"Server Kick"),
	SERVER_START(20007,"Server Start"),
	SERVER_FULL(20008,"Server full"),
	
	/*文件层*/
	/**
	 * INVALID_PARAMETER : 无效配置
	 */
	INVALID_PARAMETER(30001,"Please check the configuration file"),

	/*WEB层*/
	/**
	 * INCOMPLETE_PARAMETERS : 参数不全
	 * NO_PERMISSION : 无权限
	 * INVALID_VERIFICATION : 无效验证
	 */
	INCOMPLETE_PARAMETERS(40001,"Incomplete parameters"),
	NO_PERMISSION(40002,"No permission"),
	INVALID_VERIFICATION(40003,"Invalid verification"),

	/*加密层*/
	/**
	 * NO_ENCRYPTION : 数据未加密
	 * INVALID_ENCRYPTION : 数据加密无效
	 * ILLEGAL_OPERATION : 操作非法
	 * UNSUPPORTED_ENCRYPTION : JDK不支持的加密方法
	 * DOES_NOT_SUPPORT_AES_256 : JDK不支持AES128+
	 */
	NO_ENCRYPTION(50001,"No Encryption"),
	INVALID_ENCRYPTION(50002,"Invalid encryption"),
	ILLEGAL_OPERATION(50003,"Illegal operation"),
	UNSUPPORTED_ENCRYPTION(50004,"Unsupported encryption"),
	DOES_NOT_SUPPORT_AES_256(50005,"Does not support ASE256");
	
	
	private int errCode;
	private String errMsg;

    private ErrorCode(int errorCode, String errorMsg) {
		this.errCode = errorCode;
		this.errMsg  = errorMsg;
	}

	public String getError() {
		return "["+this.errCode+"] : "+this.errMsg;
	}

	public int getCode() {
		return this.errCode;
	}

}