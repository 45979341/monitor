package com.zhuzhou.framework.validate.validator;

import com.zhuzhou.framework.validate.annotation.NotEmpty;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字段值验证器
 * @author chenzeting
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, Object> {

	private NotEmpty notEmpty;
	
	/**
	 * 初始化验证参数
	 */
	@Override
	public void initialize(NotEmpty notEmpty) {
		this.notEmpty = notEmpty;
	}

	/**
	 * 验证参数是否有效
	 * @param target 验证目标
	 */
	@Override
	public boolean isValid(Object target, ConstraintValidatorContext context) {
		if(target != null && StringUtils.isNotEmpty(target.toString())) {
			return true;
		} else {
			 String messageTemplate = context.getDefaultConstraintMessageTemplate();  
	         context.disableDefaultConstraintViolation();
	         context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
	         return false;
		}
	}

}
