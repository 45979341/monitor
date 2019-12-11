package com.zhuzhou.framework.exception;

/**
 * @author chzeting
 */
public enum ErrorResponseMsgEnum {

	// 令牌过期
	TOKEN_EXPIRE("97", "请先登录！","请先登录！"),

	//用户错误，以1开头
	USER_OR_PASS_ERROR("100001","用户名或者密码错误", "用户名或者密码错误"),
	USER_DISABLED("100002","用户名被禁用", "用户名被禁用"),
	USER_EXISTS("100003","用户名已存在","用户名已存在"),
	USER_NOT_EXISTS("100004","用户不存在","用户不存在"),




	TOKEN_INVALID("200002","登录令牌无效","登录令牌无效"),

	// 菜单 暂时写这个代码
	MENU_NOT_EXISTS("30001","菜单不存在","菜单不存在"),
	MENU_HAVE_CHILDREN("30002", "请先删除子菜单", "请先删除子菜单"),
	MENU_IN_USE("30003","该菜单已有角色使用，请先对角色解除该菜单，再进行删除！","该菜单已有角色使用，请先对角色解除该菜单，再进行删除！"),

	ROLE_DEL_EXISTS("110001","该角色已有用户使用，请先对用户解除该角色，再进行删除！","该角色已有用户使用，请先对用户解除该角色，再进行删除！"),

	CONNECTION_TIMEOUT("400011", "请求地址调用超时，请联系管理员！","请求地址调用超时，请联系管理员！"),

	FILE_COPY_ERROR("400012", "图片异常", "图片异常"),
	;

	private final String code;
	private final String message;
	private final String solution;

	ErrorResponseMsgEnum(String code, String message, String solution) {
		this.code = code;
		this.message = message;
		this.solution = solution;
	}

	public static String messageByCode(String code) {
		ErrorResponseMsgEnum fromValue = fromValue(code);
		if (fromValue == null) {
			return "未知类型";
		}
		return fromValue.getMessage();
	}

	public static ErrorResponseMsgEnum fromValue(String code) {
		for (ErrorResponseMsgEnum each : ErrorResponseMsgEnum.values()) {
			if (each.getCode().equalsIgnoreCase(code)) {
				return each;
			}
		}
		return null;
	}

	public String formatSolution(Object... args){
		return String.format(this.solution, args);
	}

	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getSolution() {
		return solution;
	}
}