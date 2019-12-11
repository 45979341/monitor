package com.zhuzhou.framework.result;

import com.zhuzhou.framework.utils.stomp.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 全局返回结果
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonPropertyOrder({"code", "message", "handleTime", "userId", "data"})
@Slf4j
public final class Result<T> implements Serializable{

	private static final long serialVersionUID = -5759964467525426508L;

	//**********************系统全局错误码**************************

	/**
	 * 请求成功状态码
	 */
	public static final String SUCCESS = "0";

	/**
	 *  未授权（未登录）
	 */
	public static final String NOT_OAUTH = "97";

	/**
	 * 无权限访问
	 */
	public static final String PERMISSION_DENIED = "40001";

	/**
	 * 非法请求
	 */
	public static final String ILLEGAL_REQUEST = "40002";

	/**
	 * 无效的请求参数
	 */
	public static final String INVALID_PARAM = "40003";

	/**
	 * 请求接口不存在
	 */
	public static final String API_NOT_EXIST = "40004";

	/**
	 * 无效请求方法
	 */
	public static final String INVALID_REQUEST_METHOD = "40005";

	/**
	 *  不是超级管理员访问
	 */
	public static final String SUPER_PERMISSION_DENIED = "40006";

	/**
	 * 用户被锁定
	 */
	public static final String ACCOUNT_LOCKED = "40007";

	/**
	 * 用户或密码错误
	 */
	public static final String USER_OR_PWD_ERR = "40008";

	/**
	 * 系统异常
	 */
	public static final String SYSTEM_ERROR = "100";

	/**
	 * 系统繁忙
	 */
	public static final String SYSTEM_BUSY = "101";

	/**
	 * 无效的签名
	 */
	private static final String INVALID_SIG = "40006";

	//**************************************************************

	/**
	 * 错误码
	 */
	private String code;

	/**
	 * 错误信息
	 */
	private String message;

	private String userId;

	private Long handleTime;

	@JsonIgnore
	private String resultType;

	/**
	 * 返回数据
	 */
	private T data;

	public Result(){}

	public Result(String code, String msg){
		this.code = code;
		this.message = msg;
	}

	public static <T> Result<T> success(){
		return new Result<>(SUCCESS, "request success");
	}

	public static <T> Result<T> locked(){
		return new Result(ACCOUNT_LOCKED, "用户被锁定");
	}

	public static <T> Result<T> unauthorized(){
		return new Result<>(NOT_OAUTH, "请先登录！");
	}

	public static <T> Result<T> permissionDenied(){
		return new Result<>(PERMISSION_DENIED, "无访问权限");
	}
	public static <T> Result<T> superPermissionDenied(){
		return new Result<>(SUPER_PERMISSION_DENIED, "不是超级管理员权限");
	}

	public static <T> Result<T> illagalRequest(){
		return new Result<>(ILLEGAL_REQUEST, "非法请求");
	}

	public static <T> Result<T> invalidSign() {
		return new Result<>(INVALID_SIG, "无效的签名");
	}

	public static <T> Result<T> invalidParam(){
		return new Result<>(INVALID_PARAM, "参数校验不通过");
	}

	public static <T> Result<T> notFound(){
		return new Result<>(API_NOT_EXIST, "请求地址错误");
	}

	public static <T> Result<T> systemError(){
		return new Result<>(SYSTEM_ERROR, "系统内部错误");
	}

	public static <T> Result<T> systemBusy() {
		return new Result<>(SYSTEM_BUSY, "操作过于频繁，请稍后重试");
	}

	@Override
	public String toString() {
		try {
			return JsonUtils.toJson(this);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "request error";
		}
	}
}
