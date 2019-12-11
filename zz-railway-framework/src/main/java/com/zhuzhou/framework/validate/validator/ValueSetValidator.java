package com.zhuzhou.framework.validate.validator;

import com.zhuzhou.framework.validate.annotation.ValueSet;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * 枚举值验证器
 * @author chenzeting
 */
public class ValueSetValidator implements ConstraintValidator<ValueSet, Object> {

	private ValueSet valueSet;
	
	/**
	 * 初始化验证参数
	 */
	@Override
	public void initialize(ValueSet valueSet) {
		this.valueSet = valueSet;
	}

	/**
	 * 验证参数是否有效;
	 * @param target 验证目标
	 */
	@Override
	public boolean isValid(Object target, ConstraintValidatorContext context) {
		if(target == null){
			return true;
		}
		if(Arrays.asList(valueSet.value()).contains(target.toString())){
			return true;
		}else{
			 String messageTemplate = context.getDefaultConstraintMessageTemplate();  
	         context.disableDefaultConstraintViolation();
	         context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
	         return false;
		}
	}

}
